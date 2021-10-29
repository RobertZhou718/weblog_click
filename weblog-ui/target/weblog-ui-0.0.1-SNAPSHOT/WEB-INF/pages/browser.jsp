
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<base href=" <%=basePath%>"> 
    <title>浏览器占比</title>
    <script src="js/jquery-3.3.1.min.js"></script>
    <script src="js/echarts.min.js"></script>
     <script type="text/javascript">
    var base = "http://localhost:8080/weblog-ui"
    </script>
</head>
<body>
<div>
	<a href="page/main">按照小时统计</a>
	<a href="page/os">按照操作系统统计</a>
    <a href="page/browser">按照浏览器统计</a>
    <a href="page/stay">按照停留时间统计</a>
</div>
<div id="main" style="height:400px"></div>
<script type="text/javascript">
    var myChart = echarts.init(document.getElementById('main'));
    option = {
        title : {
            text: '访问论坛浏览器占比',
            subtext: '2012-01-04',
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:[]
        },
        toolbox: {
            show : true,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {
                    show: true,
                    type: ['pie', 'funnel'],
                    option: {
                        funnel: {
                            x: '25%',
                            width: '50%',
                            funnelAlign: 'left',
                            max: 1548
                        }
                    }
                },
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        series : [
            {
                name:'Browser',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[]
            }
        ]
    };

    // 异步加载数据
    $.get(base+"/refer/browser").done(function (data) {
        // 填入数据
        myChart.setOption({
            legend: {
                data: data.categories
            },
            series: [{
                // 根据名字对应到相应的系列
                name: 'Browser',
                data: data.keyValues
            }]
        });
    });
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
</script>
</body>
</html>
