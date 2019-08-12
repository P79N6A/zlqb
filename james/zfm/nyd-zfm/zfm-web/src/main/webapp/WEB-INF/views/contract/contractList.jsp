<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>借款协议</title>
	<style>
	* {
		padding: 0;
		margin: 0;
	}
		a {
		    text-decoration: none;
		    color: #666;
		    padding:0;
		    display: block;
		    height:60px;
		    line-height:60px;
		}
		.userfunction li {
		    border-bottom: 1px solid #dfdfdf;
		    height:60px;
		    line-height: 60px;
		}
		.userfunction ul {
		list-style-type:none;
		padding: 0;
		margin: 0;
		}
	</style>
</head>
<body>

<!--协议列表-->
<article class="artic">
    <div class="userfunction">
        <ul>
            <li class="clearfix"><a href="${pageContext.request.contextPath}/web/contract/borrowAgreement">《借款服务协议 》</a></li>
            <li class="clearfix"><a href="${pageContext.request.contextPath}/web/contract/mediacyAgreement">《居间服务协议》</a></li>
        </ul>
    </div>
</article>
<!--协议列表end-->

</body>
</html>
