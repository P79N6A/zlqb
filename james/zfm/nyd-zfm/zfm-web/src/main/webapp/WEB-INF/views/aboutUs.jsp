<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!doctype html>
<html>

    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>客服中心</title>
        <script src='${pageContext.request.contextPath}/common/js/jquery-2.1.1.min.js'></script>
    </head>

    <body>
    <div id='htmlCon'>
    
    </div>
    </body>

</html>
<script>
var localObj = window.location;
var contextPath = localObj.pathname.split("/")[1];
var basePath = localObj.protocol + "//" + localObj.host + "/"+ contextPath;
$(function(){
	$.ajax({
		type:'get',
		url : basePath+"/web/About/queryAbout",
		success:function(data){
 			if(data.code == 1){
 				$('#htmlCon').html(data.data.data);
 			}
		}
});
})
</script>