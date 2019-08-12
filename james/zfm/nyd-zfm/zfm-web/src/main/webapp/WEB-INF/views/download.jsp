<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>App下载</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
  <style>
       /* 清除内外边距 */
        body {
            background-color: #117CFF;
            width:750px;
            height:100%;
        }
        body, h1, h2, h3, h4, h5, h6, hr, p, blockquote, 
        dl, dt, dd, ul, ol, li, 
        pre,
        fieldset,button, input, textarea,th, td { 
            font-size: 14px;
            margin: 0;
            padding: 0;
            font-family:"PingFangSC-Regular",Helvetica Neue,Tahoma,Arial,'Hiragino Sans GB','Microsoft Yahei',sans-serif;
        }
        a, abbr, acronym, address, applet, article, aside, audio, b, big, blockquote, body, canvas, caption, center, cite, code, dd, del, details, dfn, div, dl, dt, em, embed, fieldset, figcaption, figure, footer, form, h1, h2, h3, h4, h5, h6, header, html, i, iframe, img, input, ins, kbd, label, legend, li, mark, menu, nav, object, ol, output, p, pre, q, ruby, s, samp, section, small, span, strike, strong, sub, summary, sup, table, tbody, td, tfoot, th, thead, time, tr, tt, u, ul, var, video{
            font-size: 14px;
            font-family:"PingFangSC-Regular",Helvetica Neue,Tahoma,Arial,'Hiragino Sans GB','Microsoft Yahei',sans-serif;
        }
        *{ margin:0; padding:0;}
        textarea{ resize:none;}
        ul, li, ol { list-style: none; }
        a { text-decoration: none; cursor: pointer;}
        a:hover { text-decoration: none; }
      
        .registe_success{
          margin:0 11.5px;
          height:765px;
          position: relative;
        }
        .registe_success .redCard{
           position: absolute;
           top:0px;
           left:0px;
        }
        .registe_success .successWord{
           position: absolute;
           top:92px;
           left:246px;
        }
        .registe_success .btn{
          position: absolute;
          top:423px;
          left:22.5px;
        }
        .step{
          margin: 0 34px 109px;

        }
        .download,.downloads{
          width:682px;
          height:100px;
          line-height: 100px;
          font-size: 29px;
          border-radius: 50px;
          position: absolute;
          top:508px;
          left:22px;
        }
        .download a,.downloads a{
          font-size: 29px;
          color:#fff;
        }
        .success_tips{
          height:350px;
          display: flex;
          display: -webkit-flex;
          align-items:center;
          font-size: 29px;
        }
        .tipBox_x1{
          background-color:#000;
          opacity:0.8;
          position: absolute;
          /*width:750;*/
          height:100%;
          left:0;
          top:0;
          display: none;
        }
        .tipBox_x1 img{
         width:100%;
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
                // andriod 2.3以上
            } else {
                document.write('<meta name="viewport" content="width=750, target-densitydpi=device-dpi">');
            }
            // 其他系统
        } else {
            document.write('<meta name="viewport" content="width=750, user-scalable=no, target-densitydpi=device-dpi">');
        }
    </script>                                  
</head>                           

<body>
  <div id="content" class="ly_content">
      <div class="registe_success">
         <img class="btn" src="images/btn.png" alt="">
         <img class="redCard" src="images/redCard.png" alt="">
         <img class="successWord" src="images/successWord.png" alt="">
         
        <div class="download" id="app_forAndroid" style="display:none;">
            <a href="javascript:void(0)"></a>
        </div> 
        <div class="downloads" id="app_forIos" style="display:none;">
          <a href="javascript:void(0)"></a>
        </div> 
      </div>
      <div class="step">
         <img src="images/buzhou.png" alt="">
      </div>

  </div>

  <div class="tipBox_x1" id="tipBox_x1"><img src="images/open_tip_n1.png"></div>
</body>
<script>

$(function(){
    //判断客户端是iphone还是android
    function clients_type(){
      var userAgent={};
      var u = navigator.userAgent;
      var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端
      var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
      userAgent.isAndroid=isAndroid;
      userAgent.isiOS=isiOS;
      return userAgent;
    } 
    var userAgent=clients_type();
    if(userAgent.isAndroid){
      $("#app_forAndroid").show();
      $.get(basePath+"/web/mobile/public/down/queryDownPath",function(data){
        var success=data.success;
        if(success){
          $("#app_forAndroid a").attr("href",data.data);
        }
      });
    }else if(userAgent.isiOS){
      $("#app_forIos").show();
      $("#app_forIos a").attr("href",'itms-services://?action=download-manifest&url=https://www.51bel.com/files/zhiyun/manifest.plist');
    }
     //当点击下载时，判断是否是微信浏览器
    function isWeiXin(){
        var ua = window.navigator.userAgent.toLowerCase();
        if(ua.match(/MicroMessenger/i) == 'micromessenger'){
            return true;
        }else{
            return false;
        }
    }
    //下载或则跳转
    $(".download",".downloads").click(function(){
      if(isWeiXin()){
        $("#tipBox_x1").show();
      }else{

      }
      $("#tipBox_x1").click('click',function(){
        $(this).hide();
      });
    });
})
</script>

</html>