<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<title>首页</title>
<head>
<base href=" <%=basePath%>"> 
 <link rel="stylesheet" href="css/index.css">
</head>
<body>
<%-- <jsp:forward page="/WEB-INF/pages/main.jsp"></jsp:forward> --%>
<!--  <div>
 	<a href="page/main">按照小时系统统计</a>
	<a href="page/os">按照操作系统统计</a>
    <a href="page/browser">按照浏览器统计</a>
    <a href="page/province">按照地域统计</a>
    <a href="page/stay">按照停留时间统计</a>
</div> -->
<div class="t_container">
    <header class="t_h_bg">
            <span>基于大数据技术的网站点击流数据分析</span>
    </header>
     <main class="t_main">
	    <div class="t_title">
	    	
	    	<ul>
	    	<li><a href="page/main">按照小时系统统计</a></li>
			<li><a href="page/os">按照操作系统统计</a></li>
			<li><a href="page/province">按照地域统计</a></li>
		    <li><a href="page/browser">按照浏览器统计</a></li>
		   <li> <a href="page/stay">按照停留时间统计</a></li>
		   <li> <a href="page/referer">访问页面来源统计</a></li>
		    </ul>
		   
	    </div>

	</main>
</div>
</body>
</html>
