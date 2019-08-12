<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>现金贷注册</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/alert.css">
    <script src="http://code.jquery.com/jquery-2.1.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/common/js/popups.js"></script>
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
  <!-- 注册 -->
  <div id="content" class="ly_content">
      <div class="banner"><img src="${pageContext.request.contextPath}/common/images/banner.png"></div>
      <div class="registe">
          <div><input type="hidden" name="channelType" maxlength="11" placeholder="注册渠道" value = "${channelType}"></div> 
          <div><input type="hidden" name="inviterType" maxlength="11" placeholder="邀请来源" value = "${inviterType}"></div> 
      
          <div class="word top"><input type="text" name="phone" maxlength="11" placeholder="手机号码"></div>
          <div class="word"><input type="password" name="password" placeholder="登录密码，6-20位数字和字母组合"></div>
          <div class="word code"><input type="text" maxlength="4" name="imgCode" placeholder="图形验证码"><span class="imgCode"><img src="" alt=""></span></div>
          <div class="word code"><input type="text" maxlength="6" name="msgCode" placeholder="短信验证码"><button class="megCode">获取验证码</button></div>
          <div style="font-size:24px;height:68px;line-height:50px;text-align:center;color:#fff">立即注册即代表同意 <span style="color:#FA8343;font-size:24px;" class="xieyi">《用户注册服务协议》</span></div>
          <div class="registe_go">立即注册</div>      
      </div>
  </div>
  <!-- 下载 -->
  <div id="contentDown" class="ly_contentDown">
      <div class="registe_success">
         <img class="btn" src="${pageContext.request.contextPath}/common/images/btn.png" alt="">
         <img class="redCard" src="${pageContext.request.contextPath}/common/images/redCard.png" alt="">
         <img class="successWord" src="${pageContext.request.contextPath}/common/images/successWord.png" alt="">
         
        <div class="download" id="app_forAndroid" style="display:none;">
            <a href="javascript:void(0)"></a>
        </div> 
        <div class="downloads" id="app_forIos" style="display:none;">
          <a href="javascript:void(0)"></a>
        </div> 
      </div>
      <div class="step">
         <img src="${pageContext.request.contextPath}/common/images/buzhou.png" alt="">
      </div>
  </div>
  <div class="tipBox_x1" id="tipBox_x1"><img src="${pageContext.request.contextPath}/common/images/open_tip_n1.png"></div>
  
   <!-- 注册协议 -->
  <div class="ly_contentXieYi">
    <div class="ly_layout">
     <div class="close" style=""> X</div>
        <div class="title">
          <h3>用户注册协议</h3>
        </div>
        <h3>一、总则</h3>
        <p class="indent">1.1 ${product}（以下简称“本产品”）各项服务的所有权和运营权归${company}（以下简称“本公司”）所有。</p>      
        <p class="indent">1.2 在注册成为本产品用户（以下简称“用户”）前，请用户务必认真、仔细阅读，并对本协议全部内容作充分理解。用户成功注册或使用本产品，即视为用户已经充分理解和同意本协议全部内容，本协议立即在用户与本公司之间产生法律效力，用户注册使用本产品服务的全部活动将受到本协议的约束并承担相应的责任和义务。如用户不同意本协议内容，请不要注册或使用本产品。</p>
        <p class="indent">1.3 用户须保证在注册或使用本产品时，已经年满18周岁且具备完全民事行为能力。如用户不具备前述条件，用户应终止注册或停止使用本产品。用户若通过本人注册的账户为其他不具备前述条件的任何第三方借款，本公司有权拒绝提供服务，已提供服务的，本公司有权终止并保留追究责任的权利，因此产生的任何法律责任由用户自行承担。</p>
        <p class="indent">1.4 用户在此确认知悉并同意本公司有权根据需要不时修改、增加或删减本协议。本公司将采用在本产品公示的方式通知用户该等修改、增加或删减，用户有义务注意该等公示。一经本产品公示，视为已经通知到用户。用户同意并确认，本公司可能以页面消息、微信、短消息等方式向用户发送将来可能发布的各类规则，该等规则构成本协议的一部分。若用户在本协议及各类规则变更后继续使用本产品服务的，视为用户已仔细认真阅读、充分理解并同意接受修改、增加或删减后的本协议及各类规则，且用户承诺遵守修改、增加或删减后的本协议及各类规则内容，并承担相应的责任和义务。若用户不同意修改、增加或删减后的本协议或各类规则内容，应立即停止使用本产品服务，本公司保留中止、终止或限制用户继续使用本产品服务的权利，但该等终止、中止或限制行为并不豁免用户在本产品已经发生的行为下所应承担的责任和义务。本公司不承担任何因此导致的法律责任。</p>
        <h3>二、使用说明</h3>
        <p class="indent">2.1 用户必须为符合中华人民共和国法律规定的具有完全民事权利和民事行为能力，能够独立承担民事责任的自然人。若用户违反前述限制注册使用本产品的，其监护人应承担所有责任。</p>  
        <p class="indent">2.2 用户注册时请按照本产品要求准确提供个人信息，并在取得注册账户（下称“该账户”）后及时更新用户准确、最新、完整的身份信息及相关资料，包括不限于手机号码、身份证号码、亲属联系人及社会联系人姓名、职业、银行账户等信息，以便本公司与用户进行及时、有效联系。</p>  
        <p class="indent">2.3 用户保证注册时提供的个人信息真实，本产品不向学生开放。</p>
        <p class="indent">2.4 该账户仅供用户本人使用，用户对使用该账户或密码进行的一切操作及言论负完全的责任。用户须对该账户、密码、身份信息等进行妥善保管，对于因密码、身份信息、校验码等泄露所致的损失由用户自行承担。如用户存在遗失手机或身份证件或银行卡以及其他可能危及本产品账户资金安全或发现有他人冒用或盗用用户的账户登录名及密码或任何其他未经合法授权的情形，应立即以有效方式通知本公司，向本公司申请暂停相关服务。除非另有法律规定或经司法裁判，且征得本公司同意，否则用户不得以任何方式转让、赠与或继承（相关的财产权益除外）其账号及密码等个人信息。</p>
        <p class="indent">2.5 用户不得通过本人注册的账户为任何第三方借款，用户充分知悉并承诺，不得以本人的账户出租、出借给他人，且用户充分知悉：若用户以本人账户出租、出借给他人使用，用户仍应承担《借款服务协议》项下的还款及其他义务。</p>
        <p class="indent">2.6 若用户有上述违反本协议约定情形的，产生的任何法律责任均由用户承担，本公司对此不承担任何法律责任。</p>
        <p class="indent">2.7 在需要终止使用本产品时，用户可以申请注销本产品账户，用户应当依照本产品规定的程序进行账户注销。本产品账户注销将导致本公司终止为用户提供本产品及相关服务，本协议约定的双方的权利义务终止，但依本协议其他条款另行约定不得终止的或依其性质不能终止的除外。</p>
        <p class="indent">2.8 存在以下情形的，本公司有权拒绝用户注销账户的申请并应将拒绝理由告知用户：1.该账户尚存在未了结的权利义务关系；2.注销该账户会损害本公司、本公司用户或他人的合法权益；3.本公司认为不适宜注销该账户的其他情形。</p>
         <h3>三、服务内容</h3>
        <p class="indent">本产品是由本公司投资并运营的提供自然人间借款信息及撮合服务平台。本公司为用户提供的服务包括但不限于：借款审批、合同管理、资金代管、还款管理，以及促成用户与第三方出借人达成交易的居间服务，具体内容以本产品当时提供的服务内容为准。</p>
        <p class="indent">3.1 借款申请审批：用户应当按照本产品要求的程序进行申请，包括但不限于银行储蓄卡绑定、持证自拍、本公司工作人员或本公司指定的机构及人员与用户通过QQ、电话进行核实等。用户完成上述申请程序后，本公司将对用户的申请进行审批。</p>
        <p class="indent">3.2 代付：在订立借款合同后，本公司接受第三方出借人委托，将用户借款款项存入用户指定的账户内。</p>
        <p class="indent">3.3 代扣：在订立借款合同后，用户委托本公司及本公司授权/聘请的具备相关业务资质的第三方从用户银行账户上代为扣取应还/应付款项，并用于向第三方出借人支付还款。</p>
        <p class="indent">3.4 查询：本公司将对用户在本产品中的所有操作进行记录，不论该操作之目的最终是否实现。用户可以在本产品中查询其注册用户名下的个人信息及借贷交易记录。</p>
        <p class="indent">3.5 广告：提供服务的过程中，本公司可以自行或由第三方广告商向用户发送广告、推广或宣传信息（包括商业与非商业信息），其方式和范围可不经向用户特别通知而变更。对服务中出现的广告信息，用户应审慎判断其真实性和可靠性，除法律明确规定外，用户应对依该广告信息进行的交易负责。</p>
        <h3>四、信息授权</h3>
        <p class="indent">为了更加充分的了解用户情况，以便向用户提供更好的服务，用户同意本公司向服务方提供其个人资料信息；用户充分理解并授权，服务方有权从用户在本产品的活动追踪用户个人资料信息，以及自行通过公开渠道、第三方渠道或私人渠道等合法渠道查询、收集用户必要的个人资料信息。用户确认，服务方根据本协议提供、追踪、查询和收集的用户“个人资料信息”包括但不限于以下各项：</p>
        <p class="indent">4.1 用户的身份信息，包括但不限于姓名、住所、电话号码、学历、工作状况、社会关系等</p>
        <p class="indent">4.2 用户访问本产品的设备信息，包括但不限于设备型号、设备标识、系统版本、IP地址、位置轨迹；</p>
        <p class="indent">4.3 用户的通讯信息，包括但不限于手机通讯录、短信记录、通话记录、手机运营商提供的通话及短信详单。用户须在申请借款的过程中根据平台提示输入个人手机通讯运营商的服务密码、验证码等信息，知悉且同意授权本公司或本公司聘用的其他第三方机构使用上述服务密码、验证码等信息获取用户的手机消费账单、清单、实名制等；</p>
        <p class="indent">4.4 用户的信用信息，包括但不限于征信记录、信用报告、信用评分；</p>
        <p class="indent">4.5 用户的财产信息，包括但不限于银行卡信息、信用卡信息、社会保险信息、住房公积金信息、房产信息、车辆信息、投资（包括股票、债券、基金、保险等）信息、经营信息、债务信息、逾期信息；</p>
        <p class="indent">4.6 用户的网络活动信息，包括但不限于网络银行、第三方支付机构、微信、支付宝、淘宝、京东等社交及/或交易平台的账户信息及交易详单；</p>
        <p class="indent">4.7 用户在服务方的关联方、合作方的网络活动信息，与该等关联方、合作方之间存在的合同及用户在该等合同项下的履约情况；</p>
        <p class="indent">4.8 用户在行政机关、司法机关、行业协会留存的信息，包括但不限于户籍信息、工商信息、诉讼信息、执行信息和违法犯罪信息等；</p>
        <p class="indent">4.9 与本协议项下服务相关的、用户留存在其他自然人、法人和组织的其他相关信息。</p>
        <p class="indent">4.10 在本产品使用中，如用户同意向本公司提交、绑定或授权用户的银行卡信息／账户，本公司将可能：</p>
        <p class="indent">1）查询并核对用户的账户信息。</p>
        <p class="indent">2）查询并读取用户银行卡账户中的交易信息。</p>
        <p class="indent">3）通过用户所授权或绑定的银行卡账户进行代收与代付服务。</p>
        <p class="indent">4.11 用户授权服务方，基于履行协议、提供服务、保障安全等目的，对于用户提供的个人资料信息及第三方收集的信息，在法律允许的范围内，具有合理使用、披露的权利。</p>
        <p class="indent">4.12 用户确认，其授权服务方通过以下渠道追踪、查询及收集用户个人资料信息：</p>
        <p class="indent">1)行政机关、司法机关、行业协会等类似政府、监管机构；</p>
        <p class="indent">2)依法设立的征信机构、信用评级机构、资信评估机构等类似机构；</p>
        <p class="indent">3)合法获取用户个人资料信息的其他自然人、法人以及组织（包括但不限于手机运营商、银行、服务方的关联方、合作方以及其他互联网信息服务提供商如微信、支付宝、淘宝、京东等）</p>
        <p class="indent">4.13 用户如通过本产品进行借款，应当依据《借款服务协议》中约定进行还款，本公司有权通过电话、短信、微信、手机应用通知、发律师函、上门等途径对用户进行服务与还款提醒。用户理解并同意，如用户未按期履行还款义务，本公司将依照《借款服务协议》约定公开用户的逾期信息。</p>
        <h3>五、使用规则</h3>
        <p class="indent">为有效保障用户使用本产品时的合法权益，用户理解并同意接受以下规则：</p>
        <p class="indent">5.1 用户应按照平台要求实名注册并完整填写相关个人信息，否则用户可能会受到借款、提现、还款的限制，且本公司有权对用户的账户进行冻结，直至用户达到实名。用户须保证所提交用户信息的真实性，如有虚假，本公司有权拒绝提供服务，涉嫌欺诈或盗用他人信息的，将可能记入网络征信系统，影响用户的征信记录，同时本公司将保留追究用户相应法律责任的权利。</p>
        <p class="indent">5.2 本公司并非银行或其他金融机构，本产品也非金融业务，本协议项下的资金移转均通过银行或第三方支付公司来实现，用户理解并同意其资金于流转途中的合理时间。</span>2.政策风险：有关法律、法规及相关政策、规则发生变化，可能引起价格等方面异常波动，个人会员有可能遭受损失；</p>
        <p class="indent">5.3 因用户的过错导致的任何损失均由用户自行承担，该过错包括但不限于：不按照交易提示操作，未及时进行交易操作，遗忘或泄漏密码、校验码等，密码被他人破解等。</p>
        <p class="indent">5.4 在用户使用本产品时，本公司有权收取相关服务费用。本公司拥有制订及调整服务费之权利，具体服务费用以用户使用本产品时产品页面上所列之收费方式公告或用户与本公司达成的其他电子或书面协议为准。</p>
        <h3>六、用户守法承诺</h3>
        <p class="indent">6.1 用户在使用本产品时应遵守中华人民共和国相关法律法规、用户所在国家或地区之法令及相关国际惯例，不将本产品用于任何非法目的，也不以任何非法方式使用本产品，否则本产品有权拒绝提供服务，或提前终止协议并追回借款，且用户应承担所有相关法律责任。</p>
        <p class="indent">6.2 用户承诺严格按照本协议履行义务。如未完全履行义务，本产品有权将其违约行为记入信用资料，有权在任何形式的媒体上公布其违约行为。</p>
        <h3>七、隐私权保护</h3>
        <p class="indent">本公司重视对用户隐私的保护。仅限于向用户提供服务及提升服务质量的目的和以下情形，本公司收集或使用用户的个人信息：</p>
        <p class="indent">7.1 经过用户同意或授权，本公司可从公开及私人资料中收集用户信息，为用户提供更合理的服务。</p>
        <p class="indent">7.2 某些服务和（或）产品由本公司的合作伙伴提供或由本公司与合作伙伴共同提供，本公司会与其共享提供服务和（或）产品需要的信息，且在此情形下，本公司需要求该等合作伙伴承担相应的保密义务。</p>
        <p class="indent">7.3 本公司有义务根据有关法律要求向司法机关和政府部门提供用户的个人资料。</p>
        <p class="indent">7.4 根据法律规定及合理商业习惯，在本公司计划与其他公司合并或被其收购或进行其他资本市场活动（包括但不限于IPO，债券发行）时，以及其他情形下本公司需要接受来自其他主体的尽职调查时，本公司会把用户的信息提供给必要的主体，但本公司会通过和这些主体签署保密协议等方式要求其对用户的个人信息采取合理的保密措施。</p>
        <h3>八、平台中断或故障</h3>
        <p class="indent">8.1 用户理解并同意，在使用本产品的过程中，由于互联网平台的特殊性，本公司不承诺服务不发生中断或者故障。由于系统维护、设备故障、 黑客攻击及其他不可抗据因素造成的影响，导致用户无法使用相关服务，本公司无需承担任何法律责任。</p>
        <h3>九、 责任范围及责任限制</h3>
        <p class="indent">9.1 用户信息是由用户本人自行提供的，本公司无法保证该信息之准确、及时和完整。本公司仅对本协议中列明的责任承担范围负责。</p>
        <p class="indent">9.2 用户由本公司任何工作人员处所取得的建议、说明，均不构成本公司对服务的承诺和保证。由此产生的法律后果，本公司将不承担任何法律责任。</p>
        <h3>十、知识产权保护</h3>
        <p class="indent">10.1 本公司平台上所有内容，包括但不限于著作、图片、档案、资讯、资料、网站架构、网站画面的安排、网页设计，均由本公司或关联公司依法拥有其知识产权，包括但不限于商标权、专利权、著作权、商业秘密等。</p>
        <p class="indent">10.2 非经本公司或关联企业书面同意，任何人不得擅自使用、修改、复制、公开传播、改变、散布、发行或公开发表本网站程序或内容。</p>
        <p class="indent">10.3 尊重知识产权是用户应尽的义务，如有违反，用户应承担损害赔偿责任。</p>
        <h3>十一、法律适用与管辖</h3>
        <p class="indent">本协议之效力、解释、变更、执行与争议解决均适用中华人民共和国法律，没有相关法律规定的，参照通用国际商业惯例和（或）行业惯例。因本协议产生之争议，均应依照中华人民共和国法律予以处理，并由本公司所在地的人民法院管辖。</p>
    </div> 
  </div>
  <!-- loading页 -->
  <div class = 'loading_wrap'>
    <div class="loadEffect">
       <span></span>
       <span></span>
       <span></span>
       <span></span>
       <span></span>
       <span></span>
       <span></span>
       <span></span>
    </div>
  </div>
</body>
<script>
$(function(){
    var localObj = window.location;
    var contextPath = localObj.pathname.split("/")[1];
    var basePath = localObj.protocol + "//" + localObj.host + "/"+ contextPath;
  /*   $('.code input').on('blur',function(){
        setTimeout(function(){
            window.scrollTo(0,  document.documentElement.clientHeight)
        },100)
    }) */
    //图形验证码
    var imgCode
    function imgcodes(){
      imgCode=basePath+"/web/mobile/validcode/createcaptcha?"+Math.random();
         $('.imgCode img').attr('src',imgCode);
    }
    imgcodes()
    $('.imgCode').click(function(){
         imgCode=basePath+"/web/mobile/validcode/createcaptcha?"+Math.random();
         $('.imgCode img').attr('src',imgCode);
    })
    //短信验证码
    $('.megCode').click(function(){
        var phone = $('input[name=phone]').val();
        var password = $('input[name=password]').val();
        var imgCode = $('input[name=imgCode]').val();
        if(!phone){
          validate=false; 
          jqalert({
            title:'提示',
            content:'请输入手机号码',
            click_bg:false
          }) 
          return false
         }
        if(!(/^(13[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|14[0-9]|19[0-9])[0-9]{8}|1110[0-9]{7}$/.test(phone))){
           validate=false; 
            jqalert({
              title:'提示',
              content:'请输入正确的11位数字手机号码'
            })
            return false
        }
        if(!password){
           validate=false; 
           jqalert({
              title:'提示',
              content:'请输入密码',
              click_bg:false
            })
           return false
         }
        if( !(/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/.test(password))){
           validate=false; 
            jqalert({
              title:'提示',
              content:'请输入正确的6-20位的数字和字母组合的密码',
              click_bg:false
            })
            return false
        }
        if(!imgCode ){
           validate=false; 
            jqalert({
              title:'提示',
              content:'请输入图形验证码',
              click_bg:false
            })
            return false
        }
        $('.megCode').attr('disabled',"disabled");
        chkMobile(phone,imgCode)
        
    })
    //校验图形验证码
    function checkImgCode(phone,imgCode){
        $.ajax({
            url:basePath+"/web/mobile/validcode/chkCaptcha",
            type:'POST',
            data:{
                phone:phone,
                param:imgCode
            },
            success:function(res){
            	/* var a = JSON.parse(res);
                 if(a.status=='y'){
                    sedMessgae(phone)
                }else{
                   $('.errCode').text(a.msg);
                   setTimeout(function(){
                        $('.errCode').text('');},3000)
                    
                } */
                if(res.success){
                	if(res.code == "1"){
                		sedMessgae(phone)
                	}else{
                		$('.megCode').removeAttr('disabled');
                		jqalert({
                            title:'提示',
                            content:res.msg,
                            click_bg:false
                          })
                	}
                }else{
                	$('.megCode').removeAttr('disabled');
                	jqalert({
                       title:'提示',
                       content:res.msg,
                       click_bg:false
                     })
                }
            }
        });
    }
    //发送短信验证码
    function sedMessgae(phone){
       $.ajax({
            url:basePath+"/web/mobile/account/sendMobileCode",
            type:'POST',
            data:{
                phone:phone,
                codeType:'701'
            },
            success:function(res){
                if(res.success){
                    countDown(89,".megCode");
                }else{
                	$('.megCode').removeAttr('disabled');
                	jqalert({
                       title:'提示',
                       content:res.msg,
                       click_bg:false
                     }) 
                }
            }
        });
    }
    //验证手机号唯一性
    function chkMobile(phone,imgCode){
       $.ajax({
            url:basePath+"/web/mobile/account/chkMobile",
            type:'POST',
            data:{
                phone:phone
            },
            success:function(res){
                if(res.success){
                  checkImgCode(phone,imgCode)
                }else{
                   $('.megCode').removeAttr('disabled');
                   jqalert({
                    title:'提示',
                    content:res.msg,
                    click_bg:false
                  })
                }
            }
        });
    }
    //验证码倒计时
    function countDown(time,selector) {  
      time = time || 59;
      var countNum = setInterval(function () {
        $(selector).text(time+'s').attr('disabled',"disabled");
        time--;
        if ( time <= 0 ){
          time = 89;
          clearInterval(countNum);
          $(selector).text("重新获取").removeAttr('disabled');
        }
      }, 1000);
    }
    
    $('.registe_go').click(function(){
        var phone = $('input[name=phone]').val();
        var password = $('input[name=password]').val();
        var imgCode = $('input[name=imgCode]').val();
        var msgCode = $('input[name=msgCode]').val();
        var channelType = $('input[name=channelType]').val();
        var inviterType = $('input[name=inviterType]').val();
        var validate=true;

        if(!phone){
           validate=false; 
           jqalert({
              title:'提示',
              content:'请输入手机号码',
              click_bg:false
            })
            return false
        }
       if(!(/^(13[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|14[0-9]|19[0-9])[0-9]{8}|1110[0-9]{7}$/.test(phone))){
           validate=false; 
            jqalert({
              title:'提示',
              content:'请输入正确的11位数字手机号码',
              click_bg:false
            })
            return false
        }
       if(!password){
         validate=false; 
          jqalert({
            title:'提示',
            content:'请输入密码',
            click_bg:false
          })
          return false
      }
        if( !(/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/.test(password))){
           validate=false; 
            jqalert({
              title:'提示',
              content:'请输入正确的6-20位的数字和字母组合的密码',
              click_bg:false
            })
            return false
        }
        if(!imgCode){
           validate=false; 
           $('.errCode').text("请输入图形验证码");
            jqalert({
              title:'提示',
              content:'请输入图形验证码',
              click_bg:false
            })
            return false
        }
        if(!msgCode){
           validate=false; 
            jqalert({
              title:'提示',
              content:'请输入短信验证码',
              click_bg:false
            })
            return false
        }
        //注册接口
        if(validate){
            $.ajax({
                url:basePath+"/web/mobile/account/register",
                type:'POST',
                data:{
                    mobile:phone,
                    password:password,
                    msgCode:msgCode,
                    channelType:channelType,
                    inviterType:inviterType
                },
                success:function(res){
                  $('.loading_wrap').hide()
                    if(res.success){
                      $('.ly_content').hide()
                      $('.ly_contentDown').show()
                    }else{
                       jqalert({
                        title:'提示',
                        content:res.msg,
                        click_bg:false
                      })
                    }
                }
            });
        }
    })
    //下载
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
//      $("#app_forIos a").attr("href",'itms-services://?action=download-manifest&url=https://www.51bel.com/files/zhiyun/manifest.plist');
    	$("#app_forIos a").attr("href",'https://www.baidu.com/');
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
    
  //注册协议
    $('.xieyi').click(function(){
      $('.ly_contentXieYi').show()
    })
    $('.close').click(function(){
      $('.ly_contentXieYi').hide()
    })
    
    
})
</script>

</html>