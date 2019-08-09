<template>
	<div class="customer-detail">
		<div class="detail-item">
			<span class="item-title">编号：</span>
			<div class="item-content">{{detail.id}}</div>
		</div>
		<div class="detail-item">
			<span class="item-title">批次号：</span>
			<div class="item-content">{{detail.batch}}</div>
		</div>
		<div class="detail-item">
			<span class="item-title">型号：</span>
			<div class="item-content">{{detail.model}}</div>
		</div>
		<div class="detail-item">
			<span class="item-title">生产时间：</span>
			<div class="item-content">{{detail.ptime}}</div>
		</div>
		<div class="detail-item">
			<span class="item-title">保修时间：</span>
			<div class="item-content">{{detail.gtime}}</div>
		</div>

		<div class="detail-item">
			<span class="item-title">状态：</span>
			<div class="item-content">
				<span v-if="detail.state==2">已启用</span>
	      		<span v-else-if="detail.state==1">已停用</span>
	      		<span v-else>草稿</span>
			</div>
		</div>
		<div class="detail-item">
			<span class="item-title">备注：</span>
			<div class="item-content">{{detail.desc}}</div>
		</div>
	</div>
</template>

<script type="text/javascript">
	export default{
		name:"factory",
		props:["detail"]
	}
</script>

<style lang="less" scoped>
	.customer-detail{
		height: auto;
		overflow: hidden;
		.detail-item{
			height: auto;
			overflow: hidden;
			line-height: 25px;
			padding-bottom: 15px;
			.item-title{
				display: inline-block;
				width: 100px;
				font-size: 14px;
				float: left;
				color: #333;
				text-align: right;
			}
			.item-content{
				margin-left: 110px;
				color:#999;
			}
		}
	}
</style>
