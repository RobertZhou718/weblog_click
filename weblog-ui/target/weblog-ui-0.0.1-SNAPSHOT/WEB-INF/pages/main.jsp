<%@ page language="Java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" isELIgnored="false"%> 
 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<base href=" <%=basePath%>"> 
    <title>首页</title>
    <script src="js/jquery-3.3.1.min.js"></script>
    <script src="js/echarts.min.js"></script>
    <script type="text/javascript">
    var base = "http://localhost:8080/weblog-ui"
    </script>
    
</head>
<body>
    <div>
	<a href="page/os">按照操作系统统计</a>
    <a href="page/browser">按照浏览器统计</a>
    <a href="page/province">按照地域统计</a>
    <a href="page/stay">按照停留时间统计</a>
</div>
   	
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="height:400px"></div>
    <script type="text/javascript">
       
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));

        option = {
            title : {
                text: '每小时网站PV',
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['最高访问量','最低访问量']
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            xAxis : [
                {
                    type : 'category',
                    name: "hour",
                    splitLine: {show: false},
                    boundaryGap : false,
                    data : []
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    axisLabel : {
                        formatter: '{value}'
                    }
                }
            ],
            series : [
                {
                    name:'PV',
                    type:'line',
                    data:[],
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name: '平均值'}
                        ]
                    }
                }
            ]
        };

        // 异步加载数据
        $.get(base+"/time/hour").done(function (data) {
            // 填入数据
            myChart.setOption({
                xAxis: {
                    data: data.categories
                },
                series: [{
                    // 根据名字对应到相应的系列
                    name: 'PV',
                    data: data.datas
                }]
            });
        });
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    </script>
</body>
</html>
