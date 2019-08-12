<template>
    <div class="header_container">
		<el-breadcrumb separator-class="el-icon-arrow-right">
			<el-breadcrumb-item v-for="(item, index) in $route.meta.title" :key="index">{{item}}</el-breadcrumb-item>
		</el-breadcrumb>
    </div>
</template>
<script>
    export default {
    	data(){
    		return {
    		}
    	},
    	created(){
    	},
    	computed: {
    	},
		methods: {
		}
    }
</script>

<style lang="less">
	@import '../style/mixin';
	.header_container{
		background-color: #EFF2F7;
		height: 45px;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-left: 20px;
	}
	.avator{
		.wh(36px, 36px);
		border-radius: 50%;
		margin-right: 37px;
	}
	.el-dropdown-menu__item{
        text-align: center;
    }
</style>
