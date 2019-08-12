<template>
	<div class="pagition-common-wrap">
	    <el-pagination
	      @size-change="handleSizeChange"
	      @current-change="handleCurrentChange"
	      :current-page="currentPage"
	      :page-sizes="pageNum"
	      :page-size="myPageSizes"
	      :layout="layout"
	      :total="total">
	    </el-pagination>
	</div>
</template>

<script>
	export default {
		props: {
			myPageSizes:{
				type: Number,
				default: 10
			},
			pageNum :{
				type: Array,
				default: function() {
					return [10, 30, 50]
				}
			},
			currentPage: {
				type: Number,
				default: 1
			},
			total: {
				type: Number,
				default: 0
			},
			layout: {
				type: String,
				default: 'total, sizes, prev, pager, next, jumper'
			}
		},
	    data() {
	      return {
	      }
		},
		created() {

		},
		methods: {
		    handleSizeChange(val) {//每页要显示的条数
		      	this.$emit("handleSizeChange",val)
		    },
	        handleCurrentChange(val) {//当前页
	        	this.$emit("handleCurrentChange",val)
		    }
    	},
   }
</script>

<style scoped lang="scss">
.pagition-common-wrap{text-align: center}
	.title-common-wrap {
		border-bottom: 1px solid #ddd;
	    /* box-shadow: 0 1px 0 #888888; */
	    line-height: 2;
	    div {
	    	span {
	    		display: inline-block;
	    		border-bottom: 3px solid #ff9900;
    			margin-bottom: -2px;
	    	}
	    }
	}
</style>
