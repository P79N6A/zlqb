<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>牛贷注册</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
  <style>
     /* æ¸é¤åå¤è¾¹è· */
      body {
        width:750px;height:100%;margin:0;padding:0;
      }
      .container{
        width: 100%;height: 100%;text-align: center;
      }
      .container img{
        margin: 300px 0 50px;
      }
      .container div{
        width:100%;text-align: center;font-size: 36px;color:#595959;
      }

  </style>
    <script src="http://code.jquery.com/jquery-2.1.1.min.js"></script>
    <script>
      var mengvalue = -1;
        //if(mengvalue<0){mengvalue=0;}
        var phoneWidth = parseInt(window.screen.width);
        var phoneScale = phoneWidth / 750;
        var ua = navigator.userAgent;
        if (/Android (\d+\.\d+)/.test(ua)) {
            var version = parseFloat(RegExp.$1);
            // andriod 2.3
            if (version > 2.3) {
                document.write('<meta name="viewport" content="width=750, minimum-scale = ' + phoneScale + ', maximum-scale = ' + phoneScale + ', target-densitydpi=device-dpi">');
                // andriod 2.3ä»¥ä¸
            } else {
                document.write('<meta name="viewport" content="width=750, target-densitydpi=device-dpi">');
            }
            // å¶ä»ç³»ç»
        } else {
            document.write('<meta name="viewport" content="width=750, user-scalable=no, target-densitydpi=device-dpi">');
        }
    </script>                                  
</head>                           

<body>
  <div class="container">
     <img src="${pageContext.request.contextPath}/common/images/notFound.png" alt="">
     <div>该链接已失效~</div>
  </div>
</body>
</html>