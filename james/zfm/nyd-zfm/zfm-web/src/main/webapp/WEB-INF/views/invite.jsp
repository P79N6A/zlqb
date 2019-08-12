<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <title>邀请好友</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/index.css">
</head>

<body>
  <input type="hidden" value="${ pageContext.request.contextPath}" id="basePath"/>
  <input type="hidden" value="${custInfoId}" id="custInfoId"/>
  <input type="hidden" value="${activityStatus}" id="activityStatus"/>
  <c:if test="${empty inviteUrl}">
	 <div id='appDet'>
		<div class="ques-wrap">
			<div>
				<img src="${pageContext.request.contextPath}/common/images/invest/nodata.png" alt="" class="noData">
			</div>
		</div>
     </div>
  </c:if>
  <div class="invest_wrap">
    <div class="invest_top">
      <img src="${pageContext.request.contextPath}/common/images/invest/invest_banner.png" alt="">
    </div>
    <div class="invest_con">
      <div class="invest_tab">
        <ul class="clearfix">
          	<li <c:if test="${activityStatus == 1}"> class="active"  </c:if> data-index="0">赚赏金</li>
            <li <c:if test="${activityStatus == 0}"> class="active" </c:if> data-index="1">提现</li>
        </ul>
      </div>
      <div class="part_wrap">
          
        <div class="part_one part_com_tab" <c:if test="${activityStatus == 0}"> style="display: none;"  </c:if>>
          <ul>
            <li>

              <div class="com_right com_wid">
                <img src="${pageContext.request.contextPath}/common/images/invest/img_two.png" alt="">
              </div>
              <div class="com_left">
                <p>参与方式<span class="circle">1</span>：复制链接</p>
                <div class="word">
                  复制此网址分享给好友
                  <div class="address" id="addressLink">${inviteUrl}</div>
                  <a id="copyBtn" class="copyBtn" ><img src="${pageContext.request.contextPath}/common/images/invest/btn_copy_link.png" alt=""></a>
                </div>
              </div>
            </li>
            <li>
              <div class="com_left">
                <p>参与方式<span class="circle">2</span>：扫描二维码</p>
                <div class="word">
                    邀请好友打开手机浏览器扫一扫
                </div>
              </div>
              <div class="com_right com_wid">
                <img src="${pageContext.request.contextPath}/common/images/invest/img_three.png" alt="">
              </div>
            </li>
            <li>
              <div class="erweima">
                <div id="qrcode"></div>
              </div>
            </li>
          </ul>
          <div class="invest_bot">  
              <h4>活动规则</h4>
              <br/>
                 ${rewardRule}
            </div>
        </div>    
        <div class="part_two part_com_tab" <c:if test="${activityStatus == 0}"> style="display: block;"  </c:if>>
            <div class="part_two_con">
              <div class="part_two_con_wrap">
                  <p>当前可提现赏金（元）</p>
                  <div class="money"><fmt:formatNumber value="${rewardInfo.activeMoney}" pattern="0.00" /></div>
                  <ul class="clearfix">
                    <li>
                      <p><fmt:formatNumber value="${rewardInfo.totalRewardMoney}" pattern="0.00" /></p>
                      <p class="money_word">累计获得金额（元）</p>
                    </li>
                    <li>
                        <p><fmt:formatNumber value="${rewardInfo.withdrawMoney}" pattern="0.00" /></p>
                        <p class="money_word">累计已提现（元）</p>
                    </li>
                  </ul>
                  <button class="withdraw_btn"><img src="${pageContext.request.contextPath}/common/images/invest/btn_withdrawals.png" alt=""></button>
              </div>
            </div>
            <div class="showDetail"><a href="${pageContext.request.contextPath}/web/mobile/activity/invite/rewardSerial?custInfoId=${custInfoId}">查看奖金明细</a></div>
          </div>      
      </div>
      
    </div>
    
  </div>
  <div class="totast_cre"></div>
</body>
<script src="${pageContext.request.contextPath}/common/js/jquery-2.1.1.min.js"></script>
<script src="${pageContext.request.contextPath}/common/js/common.js"></script>
<script src="${pageContext.request.contextPath}/common/js/qrcode.min.js"></script>
<script src="${pageContext.request.contextPath}/common/js/clipboard.min.js"></script>
<script src="${pageContext.request.contextPath}/common/js/invite.js?version=20181206"></script>
</html>