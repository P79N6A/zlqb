<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
	<meta id="WebViewport" name="viewport" content="width=750,initial-scale=0.5,target-densitydpi=device-dpi,minimum-scale=0.5,maximum-scale=1,user-scalable=1">
	<title>借款服务协议</title>
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
		width: 700px;
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
	.indent2{
		text-indent:2em;
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
<h1>借款协议</h1>
<p class="indent2" style="padding-left:490px;"><span class="bold">合同编号：</span><span class="underline">${po.contractNumber}</span></p>
<table style="width:100%;margin: 0px auto;text-align:left; font-size:12px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0); width:100%; " class="tb_ccc0">
    <tr>
        <td style="border: 1px solid rgb(0, 0, 0);width: 100px;" class="bold">贷款方（甲方）</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">${po.lendName}</td> 
    </tr>
    <tr>
        <td style="border: 1px solid rgb(0, 0, 0);">电子邮箱</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">${po.lendEmail}</td> 
    </tr>
    <tr>
        <td style="border: 1px solid rgb(0, 0, 0);" class="bold">借款方（乙方）</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">${po.borrowName}</td> 
    </tr>
    <tr>
        <td style="border: 1px solid rgb(0, 0, 0);">身份证号</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">${po.borrowIdNo}</td> 
    </tr>
    <tr>
        <td style="border: 1px solid rgb(0, 0, 0);">联系电话</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">${po.borrowMobile}</td> 
    </tr>
    <tr>
        <td style="border: 1px solid rgb(0, 0, 0);">送达地址</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">${po.borrowAdrees}</td> 
    </tr>
    <tr>
        <td style="border: 1px solid rgb(0, 0, 0);">电子邮箱</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">${po.borrowEmail}</td> 
    </tr>
</table>
<p class="indent2">鉴于乙方向甲方申请贷款，为明确双方权利义务，依照相关法律法规之规定，甲、乙双方经平等协商一致，特订立本贷款合同，以昭信守。</p>
<p class="titL2">第一条 贷款信息</p>
<table style="width:100%;margin: 0px auto;text-align:center  ; font-size:12px; border-collapse: collapse; border: 1px solid rgb(0, 0, 0); width:100%; " class="tb_ccc0">
    <tr>
        <td style="border: 1px solid rgb(0, 0, 0);">贷款本金（人民币）</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">（小写）:${po.lowerLoanMoney}</td> 
        <td style="border: 1px solid rgb(0, 0, 0);" colspan="4">（大写）:${po.capitalLoanMoney}</td> 
    </tr>
    <tr>
        <td style="border: 1px solid rgb(0, 0, 0);">到期利息</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">${po.expireInterest}</td>
        <td style="border: 1px solid rgb(0, 0, 0);">本息总额</td> 
        <td style="border: 1px solid rgb(0, 0, 0);" colspan="2">${po.totalMoney}</td>  
    </tr>
    <tr>
        <td style="border: 1px solid rgb(0, 0, 0);">贷款用途</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">${po.loanPurpose}</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">起止时间</td> 
        <td style="border: 1px solid rgb(0, 0, 0);" colspan="2">
        <span class="underline bold">${po.startYear}</span>年<span class="underline bold">${po.startMonth}</span>月<span class="underline bold">${po.startDay}</span>日至
        <span class="underline bold">${po.endYear}</span>年<span class="underline bold">${po.endMonth}</span>月<span class="underline bold">${po.endDay}</span>日
        </td> 
    </tr>
    <tr>
        <td style="border: 1px solid rgb(0, 0, 0);" rowspan="2">收款/还款账户</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">开户行</td> 
        <td style="border: 1px solid rgb(0, 0, 0);" colspan="4">${po.bankName}</td> 
    </tr>
    <tr>
        <td style="border: 1px solid rgb(0, 0, 0);">户名</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">${po.borrowName}</td>
        <td style="border: 1px solid rgb(0, 0, 0);">账号</td> 
        <td style="border: 1px solid rgb(0, 0, 0);">${po.cardNumber}</td> 
    </tr>
</table>
<p class="titL2">第二条 贷款发放</p>
<p class="indent2">甲方或甲方委托的第三方应按合同约定及时将出借资金扣划至乙方上述收款账户，如有延迟的，贷款期限及利息以合同签订日为准。</p>
<p class="titL2">第三条 还款信息</p>
<p class="indent2">3.1 乙方应保证还款日前还款账户中的资金余额不低于还款金额；</p>
<p class="indent2">3.2 乙方已委托并授权甲方指定的第三方支付机构从上述还款账户中扣划到期应偿还的本息金额，从而履行相应的还款义务；</p>
<p class="indent2">3.3 如乙方提前还款的或因违约而产生逾期利息、违约金等，乙方已委托并授权甲方指定的支付机构将相应金额从上述还款账户中扣划给甲方；</p>
<p class="indent2">3.4 如还款日遇到法定节假日或公休日，还款日不顺延，还款日即为到期日。</p>
<p class="titL2">第四条 提前还款</p>
<p class="indent2">若乙方提前还款，则贷款合同提前到期，乙方应向甲方支付全额本金和利息。</p>
<p class="titL2">第五条 还款顺序 </p> 
<p class="indent2">如乙方的还款金额不足以清偿全部到期本息及逾期利息等其他债务的，则乙方的还款首先用于清偿乙方须承担的各项费用（包括不限于逾期利息、违约金等），剩余款项按照先还利息后还本金的顺序清偿。</p>
<p class="titL2">第六条 乙方的陈述与保证 </p> 
<p class="indent2">6.1 乙方具有民事权利能力及完全民事行为能力，能以自身名义履行本合同约定的义务并承担民事责任；</p>
<p class="indent2">6.2 签署和履行本合同是乙方真实意思表示，不存在任何形式的欺诈、胁迫、重大误解等影响乙方意思表示的情形；</p>
<p class="indent2">6.3 乙方在签署和履行本合同过程中向甲方提供的全部文件、资料及信息是真实、合法、完整和有效的，未向甲方虚构、隐瞒、伪造及遗漏可能影响其还款能力的任何信息；</p>
<p class="indent2">6.4 乙方未隐瞒所涉及调解、仲裁、诉讼、索赔、强制执行或是可能危及甲方权益的违纪违法等事件；</p>
<p class="indent2">6.5 乙方信用状况良好，无重大不良记录；</p>
<p class="indent2">6.6 本合同项下的债务可由乙方的家庭财产、共有财产等财产进行清偿；</p>
<p class="indent2">6.7 乙方保证按时足额清偿贷款本息及本合同约定的相关费用；</p>
<p class="indent2">6.8 为甲方贷款审查及债权实现之目的，乙方同意并授权甲方及甲方委托的第三方可向不动产登记中心、电信业务部门等相关机关（或第三方机构）查询乙方个人有关信息（包括但不限于家庭住址、通讯电话、工作单位、财产情况等）。</p>
<p class="titL2">第七条 违约责任</p>
<p class="indent2">7.1 出现下列任一情形，甲方有权决定本合同项下已发放的贷款全部提前到期，并要求乙方立即偿还全部贷款本金和利息：</p>
<p class="indent2">7.1.1 乙方的还款能力已经发生或者可能发生重大不利变化；</p>
<p class="indent2">7.1.2 乙方违反本合同项下的陈述与保证；</p>
<p class="indent2">7.1.3 乙方未履行本合同项下的任何一项义务；</p>
<p class="indent2">7.1.4 甲方认为出现或存在影响乙方还款的其他情形，经甲方通知后，乙方在通知期限内未能消除或纠正的；</p>
<p class="indent2">7.2 出现下列任一情形，自违约出现之日甲方有权按照【合同金额*（0.05）%/天】的标准向乙方收取违约金，直至乙方正常履行义务之日止：</p>
<p class="indent2">7.2.1 乙方未按本合同约定的期限归还贷款本息的；</p>
<p class="indent2">7.2.2 乙方未按本合同约定的用途使用贷款的；</p>
<p class="indent2">7.3 因乙方违约而致甲方为维护和实现债权发生的损失，包括但不限于仲裁/诉讼费用、调查费用、律师费用、评估费用、公告费用、差旅费用、上门催收费用（200元/次）等均由乙方承担。</p>
<p class="titL2">第八条 债权转让</p>
<p class="indent2">各方同意并确认，若甲方转让其债权的，可通过包括但不限于邮寄送达、电子邮件、手机短信等方式及时通知乙方。该通知自甲方发送后即视为送达，相关债权转让即对乙方发生法律效力。乙方应向债权受让人继续履行本协议的后续还款义务。</p>
<p class="titL2">第九条 通知</p>
<p class="indent2">9.1 双方确认以合同中预留的电子邮箱，作为双方之间往来以及仲裁机构仲裁和法院执行时的相关资料、通知、法律文书（包括裁决书）的送达地址,并以预留的手机号码为短信的通知号码;</p>
<p class="indent2">9.2 双方确认任何一方向对方、仲裁机构或者法院执行机构向双方发出的任何通知，以电子邮件或者短信方式发出，发送一方正确填写接受预留的电子邮箱或手机号码即视为送达;</p>
<p class="indent2">9.3 任何一方送达地址或电子邮箱、通讯号码发生变化时，应在该变更发生后的3日内以书面形式通知另一方，如一方需变更邮箱或者手机号码，应当与对方协商一致。如一方当事人变更邮箱或者手机号码未书面告知对方，任何一方当事人、仲裁机构或者执行法院向上述原邮箱或者原手机号码，发送法律文件被邮件系统或者短信系统退回的，均视为已经完成送达;</p>
<p class="indent2">9.4 甲乙双方一致同意，若因履行本合同产生纠纷而发生诉讼/仲裁/执行的，本合同约定的送达地址将作为人民法院或仲裁机构法律文书的送达地址。</p>
<p class="titL2">第十条 争议解决</p>
<p class="indent2">本合同项下发生的争议，首先由双方协商解决；协商不成的，依下列条款内容解决。争议期间，各方仍应继续履行未涉争议的条款。</p>
<p class="indent2">10.1 因本合同引起或者与本合同有关的所有争议，双方均同意提交北海国际仲裁院仲裁；</p>
<p class="indent2">10.2 双方同意由仲裁院采用书面审理的方式通过网络仲裁方式解决纠纷。届时仲裁院将通过电子邮件或者短信等电子方式通知被申请人案件受理情况以及答辩要求。被申请人一方应当在收到仲裁院立案通知邮件三日内通过邮件方式进行答辩、提交相关证据、选择仲裁员，逾期不答辩、不提交相关证据、选择仲裁员的，将由仲裁院指定仲裁员依法作出裁决；</p>
<p class="indent2">10.3 双方同意本案裁决书可不写明事实和理由，由仲裁庭根据相关证据直接作出仲裁裁决，并同意由仲裁院通过电子邮件方式送达裁决书；</p>
<p class="indent2">10.4 双方同意仲裁院可以其认为的适当方式公布裁决书。</p>
<p class="titL2">第十一条 其他</p>
<p class="indent2">11.1 乙方在申请贷款成功前已对本协议各条款进行确认，自甲乙双方达成一致时成立，自甲方或甲方委托的第三方将出借款项支付到乙方收款账户时生效；</p>
<p class="indent2">11.2 本协议的任何修改、补充、变更均需以书面(含电子协议)形式签订；</p>
<p class="indent2">11.3 乙方已仔细阅读本合同所有条款，甲方已依法向乙方充分提示了全部条款，特别是限制其责任的条款，并应乙方要求对全部条款的内容和法律含义作出明确说明，乙方已知悉并理解本合同所有内容及法律效力且无异议。</p>
<p class="indent2">11.4 本合同正本一式贰份，签约双方各执一份，均具同等法律效力。  </p><br/>
<p class="indent2 gefang" style = "margin-left:5px">
<img src="${po.writeSignImg}" class="image"/></p>
<div class="fr">合同签订日期：<span class="underline">${po.startYear}</span>年<span class="underline">${po.startMonth}</span>月<span class="underline">${po.startDay}</span>日</div>
</div>
  </body>
  <script type="text/javascript">
  	document.getElementById('WebViewport').setAttribute('content', 'width=750,initial-scale=' + screen.width / 750 + ',target-densitydpi=device-dpi,minimum-scale=0.5,maximum-scale=1,user-scalable=1'); 
  
  </script>
</html>
