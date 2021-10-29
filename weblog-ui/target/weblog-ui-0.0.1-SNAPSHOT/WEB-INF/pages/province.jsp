
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="en">
<head>
 	<base href=" <%=basePath%>"> 
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="ECharts">
    <title>China Map</title>
    <script src="js/echarts.min.js"></script>
    <script src="js/china.js"></script>
    <script src="js/jquery-3.3.1.min.js"></script>
     <script type="text/javascript">
    	var base = "http://localhost:8080/weblog-ui"
    </script>
    <style>#china-map {width:800px; height: 550px;margin: auto;}</style>
</head>
<body>
<div>
	<a href="page/main">按照操作系统统计</a>
	<a href="page/os">按照操作系统统计</a>
    <a href="page/browser">按照浏览器统计</a>
    <a href="page/stay">按照停留时间统计</a>
</div>
<div id="china-map"></div>

<script>
    var myChart = echarts.init(document.getElementById('china-map'));
    function randomData() {
        return Math.round(Math.random()*1000);
    }

    option = {
        title: {
            text: '访问的地域分布',
            subtext: '2012-01-04',
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data:['PV']
        },
        visualMap: {
            left: 'left',
            top: 'bottom',
            text: ['高','低'],           // 文本，默认为数值文本
            calculable: true,
            color: ['#5475f5', '#313695','#74e2ca', '#74add1', '#85daef','#e6ac53'] 
        },
        toolbox: {
            show: true,
            orient: 'vertical',
            left: 'right',
            top: 'center',
            feature: {
                dataView: {readOnly: false},
                restore: {},
                saveAsImage: {}
            }
        },
        series: [
            {
                name: 'Province',
                type: 'map',
                mapType: 'china',
                roam: false,
                label: {
                    normal: {
                        show: true
                    },
                    emphasis: {
                        show: true
                    }
                },
                data:[]
            }
        ]
    };
    // 异步加载数据
    $.get(base+"/refer/province").done(function (data) {
        // 填入数据
        myChart.setOption({
            visualMap:[{
                min:data.min,
                max:data.max
            }],
            series: [{
                // 根据名字对应到相应的系列
                name: 'Province',
                data: data.keyValues2
            }]
        });
    });
    myChart.setOption(option);

</script>

</body>
</html>