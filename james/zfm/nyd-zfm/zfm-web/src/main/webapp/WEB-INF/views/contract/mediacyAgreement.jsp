<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
	<meta id="WebViewport" name="viewport" content="width=750,initial-scale=0.5,target-densitydpi=device-dpi,minimum-scale=0.5,maximum-scale=1,user-scalable=1">
	<title>居间服务协议</title>
	<style>
	* {
		margin: 0;
		padding: 0;
		border: 0;
		font-size: 10.5px;
		line-height: 22px;
	}
     body {  
         font-family: SimSun;
     }
	.art_wrap{
		width: 660px;
		margin: 0 auto;
	}
	p {
		word-break:break-all;
        word-wrap:break-word;
	}
	.titL2 {
		font-size: 14px;
		font-weight: bold;
		margin-top:5px;
	}
	.underline{
		padding: 0 10px;
		border-bottom: #000 1px solid;
	}
	.table_N {
		border-left: #000 1px solid;
		border-top: #000 1px solid;
		border-collapse: collapse;
		word-break:break-all;
	}
	td {
		border-right: #000 1px solid;
		border-bottom: #000 1px solid;
		padding: 0 10px;
	}
	.padLeft2{
		padding-left:26px;
	}
	.bold{
		font-weight:bold;
	}
	h1{
		font-size: 16px;
		font-weight: bold;
		text-align: center;
		margin: 20px 0;
	}
	@page {
            size: 8.5in 11in;
            @bottom-center {
            	font-family :SimSun;
                content: " 第 " counter(page) " 页 ，共 " counter(pages)" 页 ";
            }
        }
        #pagenumber:before {
            content: counter(page);
        }
	.gefang{ display:inline-block; width:180px; margin-right:20px;}
	.fr{ float:right;padding-top:180px;}
	</style>
</head>
  <body>
     <div class="art_wrap">
<h1>居间服务协议</h1>
<p class="indent2" style="padding-left:400px;">协议编号：<span class="underline">${po.contractNumber}</span></p>
<p class="indent2">甲方（借款人）：${po.borrowName}<span style = "margin-left:100px"></span>身份证号：${po.borrowIdNo}</p>
<p class="indent2">电子邮箱：${po.email}</p>
<p class="indent2">乙方（服务方）： ${po.lendName}</p>
<p class="indent2">鉴于：</p>
<p class="indent2">1、甲方有一定的资金需要；</p>
<p class="indent2">2、乙方为甲方提供以下服务；</p>
<p class="indent2">2.1 办理借款的信息咨询，并在甲方申请借款过程中协助其办理各种手续；</p>
<p class="indent2">2.2 为甲方借款出具审核意见，提供信用审核以及贷后管理等服务；</p>
<p class="indent2">2.3 为甲方提供出借人推荐，促成交易、账户管理等服务；</p>
<p class="indent2">现甲方愿意接受乙方的服务并达成一致意见。</p>
<p class="titL2">第一条 名称释义</p>
<p class="indent2">1.1 《借款协议》：指甲方与乙方推荐的出借人签署的全部《借款协议》；</p>
<p class="indent2">1.2 出借人：是指与甲方签署《借款协议》的出借人；</p>
<p class="indent2">1.3 支付机构：指在甲方及本协议各方、出借人之间作为中介机构提供资金转移服务的银行或其他机构；</p>
<p class="indent2">1.4 债权：指在本协议项下出借人或其他出借人（视特定合同情况而定）所拥有的全部本金、利息、逾期罚息、违约金等债权，以人民币计价；</p>
<p class="indent2">1.5 服务期：指从甲方向乙方申请借款服务之日起至《借款协议》约定的还款之日或甲方提前还清借款之日止。</p>
<p class="titL2">第二条 甲方的权利及义务</p>
<p class="indent2">2.1 甲方有权向乙方了解其在借款过程中的各种进度及服务；</p>
<p class="indent2">2.2 甲方在申请及实现借款的全过程中，必须如实向乙方提供所要求提供的个人信息；</p>
<p class="indent2">2.3 借款人应按照本协议的规定，在借款成功后一次性向乙方支付借款咨询服务费（包括但不限于信用审核咨询费、风险拨备金、出借信息咨询费）；</p>
<p class="indent2">2.4 对于本协议中规定的任何应从借款人账户划扣款项，借款人在此同意乙方委托合作机构从其指定账户中准确划扣相应数额；如果出现多划扣现象，由乙方负责将多划扣的部分退还给借款人；</p>
<p class="indent2">2.5 甲方同意乙方委托合作机构从出借款项中扣除协议约定的相应费用后，将剩余出借款项划拨至甲方的收款账户；</p>
<p class="indent2">2.6 在本协议约定的服务期结束后，甲方可按最终支付的金额向乙方申请开具相关发票；</p>
<p class="indent2">2.7 甲方理解并接受在接受乙方服务过程中将其信息提供给多个出借人，而获取甲方个人信息的出借人可能拒绝出借，且甲方在履行《借款协议》的过程中出现违约行为，乙方可能将甲方不良信息予以公开披露或向征信机构提供。</p>
<p class="titL2">第三条 乙方的权利及义务</p>
<p class="indent2">3.1 乙方应当按照本协议的规定，为借款人进行服务，必须恪尽职守，履行诚实、信用、谨慎、有效管理义务；</p>
<p class="titL2">3.2 乙方有权基于为借款人提供服务而使用借款人个人信用信息，并可在借款人逾期还款的时候对外（包括但不限于媒体、报纸、网络平台等）披露和向征信机构提供；</p>
<p class="indent2">3.3 乙方须对借款人信息、资产情况及其他服务相关事务的情况和资料依法保密；</p>
<p class="indent2">3.4 乙方须为借款人提供借款相关的全程信息咨询服务，并在借款人申请借款过程中协助其办理各项手续；</p>
<p class="indent2">3.5 乙方有权通过借款人提供的个人信用信息及行为记录进行审核，为借款人提供还款方案建议，包括借款额度、还款期限等建议，协助借款人实现借款，并应向借款人提供完善的贷后管理服务；</p>
<p class="indent2">3.6 乙方有权向借款人收取双方约定的费用；</p>
<p class="indent2">3.7 乙方有权根据对借款人的评审结果，决定是否将借款人的借款需求向出借人进行推荐，并由出借人自行投标，以协助借款人取得资金来源，促成交易。</p>
<p class="titL2">第四条 出借信息咨询服务手续费</p>
<p class="indent2">出借信息咨询服务手续费为甲方为达成借款需求，乙方为甲方提供相关服务收取的费用，由甲方一次性缴纳，并不进行退还，金额为<span class="underline bold">${po.serverFund}</span>元 。</p>
<p class="titL2">第五条 信息变更</p>
<p class="indent2">5.1 如借款人欲变更借款协议载明的银行账户，则其须在还款日前至少3个工作日向乙方提出申请，并签署书面文件，否则因此而造成借款人未能及时还款、产生逾期违约金、罚息的由借款人自行承担；</p>
<p class="indent2">5.2 还款过程中，借款人有义务配合为达成还款而进行的包括但不限于账户验证、账户变更、身份验证等事项，否则因此而造成借款人未能及时还款、产生逾期违约金、罚息的由借款人自行承担；</p>
<p class="indent2">5.3 本协议签订之日起至借款全部清偿之日止，借款人有义务在下列信息变更三日内提供更新后的信息给乙方和出借人（包含但不限于）：借款人本人、借款人的家庭联系人及紧急联系人工作单位、居住地址、住所电话、手机号码、电子邮箱的变更。若因借款人不及时提供上述变更信息而带来的出借人、乙方的调查及诉讼等其他相关催收费用将由借款人承担。</p>
<p class="titL2">第六条 违约责任</p>
<p class="indent2">6.1 若借款人在还款过程中出现违约，则应按照（逾期违约金${po.overcost}元 +借款合同金额*${po.overcostRate}% ）/天，支付合同违约金；</p>
<p class="indent2">6.2 任何一方违反本协议的约定，使得本协议的全部或部分不能履行，均应承担违约责任，并赔偿对方因此遭受的直接损失（包括由此产生的诉讼费和律师费）；如均违约，根据实际情况各自承担相应的责任。</p>
<p class="titL2">第七条 证据和计算</p>
<p class="indent2">本协议甲乙双方确认并同意，委托乙方对本协议项下的任何金额进行计算；在无明显错误的情况下，乙方对本协议项下任何金额的任何证明或确定，应作为该金额有关事项的终局证明。</p>
<p class="titL2">第八条 法律适用及管辖</p>
<p class="indent2">8.1 甲乙双方均确认，本协议的签署、生效和履行以不违反中国的法律法规为前提。如果本协议中的任何一条或多条违反适用的法律法规，则该条将视为无效，但该无效条款并不影响本协议其他条款的效力；</p>
<p class="indent2">8.2 凡因本合同引起的或与本合同有关的任何争议，均应提交北海国际仲裁委员会按照该会仲裁规则进行仲裁。仲裁裁决是终局的，对双方当事人均有约束力；</p>
<p class="indent2">8.3 双方均同意由仲裁院采用书面审理的方式通过网络仲裁方式解决纠纷。届时仲裁院将通过电子邮件或者短信等电子方式通知被申请人案件受理情况及答辩要求。被申请人一方应当在收到仲裁院立案通知邮件三日内通过邮件方式进行答辩、提交相关证据、选择仲裁员，逾期不答辩、不提交相关证据、选择仲裁员的，将由仲裁院指定仲裁员依法作出裁决；</p>
<p class="indent2">8.4 双方均同意本案裁决书可不写明事实和理由，由仲裁庭根据相关证据直接作出仲裁裁决，并同意由仲裁院通过电子邮件方式送达裁决书；</p>
<p class="indent2">8.5 双方均同意仲裁院可以其认为的适当方式公布裁决书。</p>
<p class="titL2">第九条 其他</p>
<p class="indent2">9.1 乙方在申请贷款成功前已对本协议各条款进行确认，自甲乙双方达成一致时成立，自甲方或甲方委托的第三方将出借款项支付到乙方收款账户时生效；</p>
<p class="indent2">9.2 本协议的任何修改、补充、变更均需以书面(含电子协议)形式签订；</p>
<p class="indent2">9.3 本协议的传真件、复印件、电子协议和影印件等复本的效力与本协议原件具有同等法律效力。</p>
<br/>
<p class="indent2 gefang" style = "margin-left:5px">
<img src="${po.writeSignImg}" class="image"/></p>
<div class="fr">合同签订日期：<span class="underline">${po.startYear}</span>年<span class="underline">${po.startMonth}</span>月<span class="underline">${po.startDay}</span>日</div>
</div>
  </body>
  <script type="text/javascript">
  	document.getElementById('WebViewport').setAttribute('content', 'width=750,initial-scale=' + screen.width / 750 + ',target-densitydpi=device-dpi,minimum-scale=0.5,maximum-scale=1,user-scalable=1'); 
  
  </script>
</html>
