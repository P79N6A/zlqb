<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
 <!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>404</title>
	<style type="text/css">
		* {
			margin: 0;
			padding: 0;
		}
		body {
			background-color: #FFF;
		}
		.wrap404 img {
			width: 540px;
			height: 280px;
			text-align: center;
			margin: 10% auto 0;
		}
	</style>
</head>

<body>
	<div class="wrap404">
		<img src="${pageContext.request.contextPath}/common/images/404.png"/>
	</div>
</body>
</html>
