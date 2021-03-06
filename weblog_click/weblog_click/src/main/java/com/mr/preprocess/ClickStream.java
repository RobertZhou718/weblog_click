package com.mr.preprocess;

import com.mr.bean.WebLogBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClickStream {
	static class ClickStreamMapper extends Mapper<LongWritable, Text, Text, WebLogBean> {
		public static String formatDate(String dateStr) {
			if (dateStr == null || StringUtils.isBlank(dateStr))
				return "2012-04-04 12.00.00";
			SimpleDateFormat format = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
			String result = null;
			try {
				Date date = format.parse(dateStr);
				result = format1.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
			} finally {
				return result;
			}

		}

		static Text k = new Text();

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			try {
				String message = value.toString();
				String[] splits = message.split(" ");
				if (splits.length < 12)
					return;
				String time = formatDate(splits[3].substring(1));
				String method = splits[5].substring(1);
				String protocol = StringUtils.isBlank(splits[7]) ? "HTTP/1.1" : splits[7].substring(0, splits[7].length() - 1);
				int status = StringUtils.isBlank(splits[8]) ? 0 : Integer.parseInt(splits[8]);
				int bytes = StringUtils.isBlank(splits[9]) ? 0 : Integer.parseInt(splits[9]);
				String from_url = StringUtils.isBlank(splits[9]) ? "-" : splits[10].substring(1, splits[10].length() - 1);
				StringBuilder sb = new StringBuilder();
				for (int i = 11; i < splits.length; i++) {
					sb.append(splits[i]);
				}
				String s = sb.toString();
				String platform = s.substring(1, s.length() - 1);
				WebLogBean ms = new WebLogBean(splits[0], time, method, splits[6], protocol, status, bytes, from_url, platform);
				k.set(splits[0]);
				context.write(k, ms);
			} catch (Exception e) {
				return;
			}
		}
	}

	static class ClickStreamReducer extends Reducer<Text, WebLogBean, NullWritable, Text> {
		Text v = new Text();

		@Override
		protected void reduce(Text key, Iterable<WebLogBean> values, Context context)
		    throws IOException, InterruptedException {
			ArrayList<WebLogBean> beans = new ArrayList<WebLogBean>();
			try {
				for (WebLogBean bean : values) {
					WebLogBean webLogBean = new WebLogBean();
					try {
						BeanUtils.copyProperties(webLogBean, bean);
					} catch (Exception e) {
						e.printStackTrace();
					}
					beans.add(webLogBean);
				}
				// ???bean???????????????????????????
				Collections.sort(beans, new Comparator<WebLogBean>() {
					public int compare(WebLogBean o1, WebLogBean o2) {
						try {
							Date d1 = toDate(o1.getTimeStr());
							Date d2 = toDate(o2.getTimeStr());
							if (d1 == null || d2 == null)
								return 0;
							return d1.compareTo(d2);
						} catch (Exception e) {
							e.printStackTrace();
							return 0;
						}
					}

				});

				/**
				 * ???????????????????????????bean??????????????????visit???????????????visit???????????????page???????????????step
				 */

				int step = 1;
				String session = UUID.randomUUID().toString();
				for (int i = 0; i < beans.size(); i++) {
					WebLogBean bean = beans.get(i);
					// ????????????1???????????????????????????
					if (1 == beans.size()) {

						// ???????????????????????????60s
						v.set(session + "," + bean.getIp() + "," + bean.getTimeStr() + "," + bean.getRequest_url() + "," + step
						    + "," + (60) + "," + bean.getFrom_url() + "," + bean.getPlatform() + "," + bean.getBytes() + ","
						    + bean.getStatus());
						context.write(NullWritable.get(), v);
						session = UUID.randomUUID().toString();
						break;
					}

					// ????????????1????????????????????????????????????????????????????????????????????????
					if (i == 0) {
						continue;
					}

					// ?????????????????????
					long timeDiff = timeDiff(toDate(bean.getTimeStr()), toDate(beans.get(i - 1).getTimeStr()));
					// ????????????-???????????????<30????????????????????????????????????????????????

					if (timeDiff < 30 * 60 * 1000) {

						v.set(session + "," + beans.get(i - 1).getIp() + "," + beans.get(i - 1).getTimeStr() + ","
						    + beans.get(i - 1).getRequest_url() + "," + step + "," + (timeDiff / 1000) + ","
						    + beans.get(i - 1).getFrom_url() + "," + beans.get(i - 1).getPlatform() + ","
						    + beans.get(i - 1).getBytes() + "," + beans.get(i - 1).getStatus());
						context.write(NullWritable.get(), v);
						step++;
					} else {

						// ????????????-???????????????>30??????????????????????????????????????????????????????step???????????????????????????visit
						v.set(session + "," + beans.get(i - 1).getIp() + "," + beans.get(i - 1).getTimeStr() + ","
						    + beans.get(i - 1).getRequest_url() + "," + step + "," + (60) + "," + beans.get(i - 1).getFrom_url()
						    + "," + beans.get(i - 1).getPlatform() + "," + beans.get(i - 1).getBytes() + ","
						    + beans.get(i - 1).getStatus());
						context.write(NullWritable.get(), v);
						// ?????????????????????????????????step??????
						step = 1;
						session = UUID.randomUUID().toString();
					}

					// ???????????????????????????????????????????????????????????????
					if (i == beans.size() - 1) {
						// ???????????????????????????60s
						v.set(session + "," + bean.getIp() + "," + bean.getTimeStr() + "," + bean.getRequest_url() + "," + step
						    + "," + (60) + "," + bean.getFrom_url() + "," + bean.getPlatform() + "," + bean.getBytes() + ","
						    + bean.getStatus());
						context.write(NullWritable.get(), v);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static Date toDate(String timeStr) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		return df.parse(timeStr);
	}

	private static long timeDiff(String time1, String time2) throws ParseException {
		Date d1 = toDate(time1);
		Date d2 = toDate(time2);
		return d1.getTime() - d2.getTime();

	}

	private static long timeDiff(Date time1, Date time2) throws ParseException {

		return time1.getTime() - time2.getTime();
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		job.setJarByClass(ClickStream.class);

		// ??????job???mapper???reducer
		job.setMapperClass(ClickStreamMapper.class);
		job.setReducerClass(ClickStreamReducer.class);

		// ??????mapper???????????????
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(WebLogBean.class);
		// ??????Reducer??????
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);

		job.setNumReduceTasks(4);

		// ????????????????????????
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		boolean flag = job.waitForCompletion(true);

		System.exit(flag ? 0 : 1);
	}
}