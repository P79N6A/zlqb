<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <title></title>
  <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
</head>
<style>
  * {
    padding: 0;
    margin: 0;
  }

  body {
    background-color: #F0F1F5;
  }

  .exception_wrap {
    text-align: center;
    position: absolute;
    top: 50%;
    left: 50%;
    width: 100%;
    transform: translateX(-50%) translateY(-50%);
    -ms-transform: translateX(-50%) translateY(-50%);
    -moz-transform: translateX(-50%) translateY(-50%);
    -webkit-transform: translateX(-50%) translateY(-50%);
    -o-transform: translateX(-50%) translateY(-50%);
  }

  .exception_wrap .wordCon {
    font-size: 17px;
    color: #595959;
    margin-top: 16px;
    padding: 0 20px;
  }

  .exception_wrap img {
    width: 60%;
  }
</style>

<body>
  <div class="exception_wrap">
    <img src="${pageContext.request.contextPath}/common/images/aapH5/exception.png" />
    <p class="wordCon">异常页面</p>
  </div>
</body>

</html>