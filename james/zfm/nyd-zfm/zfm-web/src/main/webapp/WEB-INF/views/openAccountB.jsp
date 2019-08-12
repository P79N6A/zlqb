<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>银行卡授权</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="https://unpkg.com/cube-ui/lib/cube.min.css"> 
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/openAccountB.css">                            
</head>                           

<body>
	<input type='hidden' value='${user.custName}' id='custName'/>
	<input type='hidden' value='${user.custMobile}' id='custMobile'/>
	<input type='hidden' value='${user.custIc}' id='custIc'/>
	<input type='hidden' value='${user.custInfoId}' id='custInfoId'/>
	<input type='hidden' value='${user.orderId}' id='orderId'/>
	<input type='hidden' value='${user.type}' id='type'/>
	<input type='hidden' value='${user.cardNumber}' id='cardNumber'/>
	<input type='hidden' value='${user.bankName}' id='bankName'/>
  <div id="app" class="ly_app">
    <div class="comtent_top">
      <div class="hig60"><div class="wid">开户银行</div><input v-model="bankName" type="text" @click="selectBank" placeholder="请选择" readonly /></div>
      <div class="hig60"><div class="wid">客户姓名</div><input v-model="custName" :readonly='readyCust' type="text"></div>
      <div class="hig60"><div class="wid">身份证号码</div><input v-model="custIc" :readonly='readyIc' type="text"></div>
      <div class="hig60"><div class="wid">银行卡号</div><input v-model="cardNumber" type="text"></div>  
    </div>
    <div class="comtent_middle">
      <div class="hig60"><div class="wid">银行预留手机号</div><input v-model="custMobile" type="text"></div>
      <div class="hig60"><div class="wid">图形验证码</div><input v-model="param" placeholder="请输入" type="text"><img @click="changeImg" class="imgCode" :src="imgSrc"></div>
      <div class="hig60"><div class="wid">短信验证码</div><input v-model="verifyCode" placeholder="请输入" type="text"><button @click="sendCode" class="megCode">获取验证码</button></div>
    </div>
    <div class="comtent_bottom">
      <p>该卡用于提现及还款，请确认银行卡信息准确无误。</p>
      <div class="btn" @click="submit">确认</div>
    </div>
    
  </div>
 
</body>
<script>

</script>
  <script src="https://cdn.jsdelivr.net/npm/vue@2.5.17/dist/vue.js"></script>
  <script src="https://unpkg.com/cube-ui/lib/cube.min.js"></script>
  <script src="${pageContext.request.contextPath}/common/js/jquery-2.1.1.min.js"></script>
  <script src="${pageContext.request.contextPath}/common/js/openAccountB.js"></script>

</html>