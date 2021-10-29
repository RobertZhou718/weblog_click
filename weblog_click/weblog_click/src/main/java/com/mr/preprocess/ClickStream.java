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
				// 将bean按时间先后顺序排序
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
				 * 以下逻辑为：从有序bean中分辨出各次visit，并对一次visit中所访问的page按顺序标号step
				 */

				int step = 1;
				String session = UUID.randomUUID().toString();
				for (int i = 0; i < beans.size(); i++) {
					WebLogBean bean = beans.get(i);
					// 如果仅有1条数据，则直接输出
					if (1 == beans.size()) {

						// 设置默认停留时长为60s
						v.set(session + "," + bean.getIp() + "," + bean.getTimeStr() + "," + bean.getRequest_url() + "," + step
						    + "," + (60) + "," + bean.getFrom_url() + "," + bean.getPlatform() + "," + bean.getBytes() + ","
						    + bean.getStatus());
						context.write(NullWritable.get(), v);
						session = UUID.randomUUID().toString();
						break;
					}

					// 如果不止1条数据，则将第一条跳过不输出，遍历第二条时再输出
					if (i == 0) {
						continue;
					}

					// 求近两次时间差
					long timeDiff = timeDiff(toDate(bean.getTimeStr()), toDate(beans.get(i - 1).getTimeStr()));
					// 如果本次-上次时间差<30分钟，则输出前一次的页面访问信息

					if (timeDiff < 30 * 60 * 1000) {

						v.set(session + "," + beans.get(i - 1).getIp() + "," + beans.get(i - 1).getTimeStr() + ","
						    + beans.get(i - 1).getRequest_url() + "," + step + "," + (timeDiff / 1000) + ","
						    + beans.get(i - 1).getFrom_url() + "," + beans.get(i - 1).getPlatform() + ","
						    + beans.get(i - 1).getBytes() + "," + beans.get(i - 1).getStatus());
						context.write(NullWritable.get(), v);
						step++;
					} else {

						// 如果本次-上次时间差>30分钟，则输出前一次的页面访问信息且将step重置，以分隔为新的visit
						v.set(session + "," + beans.get(i - 1).getIp() + "," + beans.get(i - 1).getTimeStr() + ","
						    + beans.get(i - 1).getRequest_url() + "," + step + "," + (60) + "," + beans.get(i - 1).getFrom_url()
						    + "," + beans.get(i - 1).getPlatform() + "," + beans.get(i - 1).getBytes() + ","
						    + beans.get(i - 1).getStatus());
						context.write(NullWritable.get(), v);
						// 输出完上一条之后，重置step编号
						step = 1;
						session = UUID.randomUUID().toString();
					}

					// 如果此次遍历的是最后一条，则将本条直接输出
					if (i == beans.size() - 1) {
						// 设置默认停留市场为60s
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

		// 设置job的mapper和reducer
		job.setMapperClass(ClickStreamMapper.class);
		job.setReducerClass(ClickStreamReducer.class);

		// 设置mapper过后的细节
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(WebLogBean.class);
		// 设置Reducer细节
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);

		job.setNumReduceTasks(4);

		// 设置文件输出路径
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		boolean flag = job.waitForCompletion(true);

		System.exit(flag ? 0 : 1);
	}
}