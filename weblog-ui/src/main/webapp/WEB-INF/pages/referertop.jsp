
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<base href=" <%=basePath%>"> 
    <title>页面来源TOP10</title>
    <script src="js/jquery-3.3.1.min.js"></script>
    <script src="js/echarts.min.js"></script>
    <script src="js/shine.js"></script>
    <script src="js/rem.js"></script>
     <script type="text/javascript">
    var base = "http://localhost:8080/weblog-ui"
    </script>
    <link rel="stylesheet" href="css/index.css">
</head>
<body>
<div class="t_container">
    <header class="t_h_bg">
            <span>基于大数据技术的网站点击流数据分析</span>
    </header>
     <main class="t_main">
	    <div class="t_title">
	    	<span>【</span>
			<a href="page/main">按照每小时统计</a>
			<a href="page/os">按照操作系统统计</a>
		    <a href="page/browser">按照浏览器统计</a>
		    <a href="page/province">按照地域统计</a>
		    <a href="page/stay">按照停留时间统计</a>
		    <span>】</span>
	    </div>

		<div id="referer" style="height:500px" class="echart"> </div>
	</main>
</div>
<script type="text/javascript">
    var myChart = echarts.init(document.getElementById('referer'),'shine');
    option = {
    	    title: {
    	        text: '页面来源top10',
    	        textStyle: {
                    color: '#fff'
                },
    			x:'center'
    	    },
    	    tooltip: {
    	        trigger: 'axis',
    	        axisPointer: {
    	            type: 'shadow'
    	        }
    	    },
    	    legend: {
    	        data: ['页面来源top10']
    	    },
    	    grid: {
    	        left: '3%',
    	        right: '4%',
    	        bottom: '1%',
    	        containLabel: true
    	    },
    	    xAxis: {
    	        type: 'value',
    	        boundaryGap: [0, 0.01],
    	        axisLabel: {
                    textStyle: {
                        color: '#fff',
                        fontSize: 12
                    }
                },
    	    },
    	    yAxis: {
    	        type: 'category',
    	        data: [],
    	        axisLabel: {
                    textStyle: {
                        color: '#fff',
                        fontSize: 12
                    }
                },
    	    },
    	    series: [
    	        {
    	            name: '访问来源 ',
    	            type: 'bar',
    	            data: []
    	           
    	        }
    	    ]
    	};

    // 异步加载数据
    $.get(base+"/refer/referertop").done(function (data) {
        // 填入数据
        myChart.setOption({
        	yAxis: {
                data: data.categories
            },
            series: [{
                // 根据名字对应到相应的系列
                name: 'top10',
                data: data.datas
            }]
        });
    });
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
</script>
</body>
</html>
