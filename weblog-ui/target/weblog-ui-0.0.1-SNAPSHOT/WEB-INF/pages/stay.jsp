
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<base href=" <%=basePath%>"> 
    <title>页面停留时间TOP20</title>
    <script src="js/jquery-3.3.1.min.js"></script>
    <script src="js/echarts.min.js"></script>
     <script type="text/javascript">
    var base = "http://localhost:8080/weblog-ui"
    </script>
    
</head>
<body>
<div>
	<a href="page/main">按照操作系统统计</a>
	<a href="page/os">按照操作系统统计</a>
    <a href="page/browser">按照浏览器统计</a>
    <a href="page/province">按照地域统计</a>
</div>
<div id="stay" style="height:650px"></div>
<script type="text/javascript">
    var myChart = echarts.init(document.getElementById('stay'));
    option = {
    	    title: {
    	        text: '页面停留时间分析TOP20',
    	    },
    	    tooltip: {
    	        trigger: 'axis',
    	        axisPointer: {
    	            type: 'shadow'
    	        }
    	    },
    	    legend: {
    	        data: ['页面停留时间 top20']
    	    },
    	    grid: {
    	        left: '3%',
    	        right: '4%',
    	        bottom: '1%',
    	        containLabel: true
    	    },
    	    xAxis: {
    	        type: 'value',
    	        boundaryGap: [0, 0.01]
    	    },
    	    yAxis: {
    	        type: 'category',
    	        data: []
    	    },
    	    series: [
    	        {
    	            name: '页面停留时间 (S)',
    	            type: 'bar',
    	            data: []
    	           
    	        }
    	    ]
    	};

    // 异步加载数据
    $.get(base+"/refer/staytime").done(function (data) {
        // 填入数据
        myChart.setOption({
        	yAxis: {
                data: data.categories
            },
            series: [{
                // 根据名字对应到相应的系列
                name: 'Stay Time',
                data: data.datas
            }]
        });
    });
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
</script>
</body>
</html>
