<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
<!doctype html>
<html>

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<title>奖金明细</title>
	<script src="${ pageContext.request.contextPath}/common/js/cube.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ pageContext.request.contextPath}/common/js/vue.min.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/common/css/cube.min.css" />
	<script src="${ pageContext.request.contextPath}/common/js/cube.min.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/common/css/index.css" />
</head>

<body>
    <input type="hidden" value="${pageContext.request.contextPath}" id="basePath"/>
    <input type="hidden" value="${custInfoId}" id="custInfoId"/>
	<div id='appDet'>
		<div class="ques-wrap">
			<div v-if = 'items.length == 0'>
				<img src="${pageContext.request.contextPath}/common/images/invest/nodata.png" alt="" class="noData">
			</div>
			<div class="scroll" v-else>
				<cube-scroll ref="scroll" :data="items" :options="options" @pulling-down="onPullingDown" @pulling-up="onPullingUp">
					<template slot-scope="props">
						<div>
							<ul id='activityTab'>
								<li v-for="(item,ind) in items" :key='ind'>
									<p class="title">{{item.serialInfo}}</p>
									<div class="time">
										{{item.createTime}}
										<span class="float_right" v-if = 'item.type == 0'>-{{item.money.toFixed(2)}}元</span>
										<span class="float_right addActive" v-else>+{{item.money.toFixed(2)}}元</span>
									</div>
								</li>
							</ul>
						</div>
					</template>
				</cube-scroll>
			</div>
			
		</div>
	</div>
	<div class="totast_cre"></div>
</body>
<script src="${pageContext.request.contextPath}/common/js/jquery-2.1.1.min.js"></script>
<script src='${pageContext.request.contextPath}/common/js/rewardserial.js?version=20181201'></script>
</html>