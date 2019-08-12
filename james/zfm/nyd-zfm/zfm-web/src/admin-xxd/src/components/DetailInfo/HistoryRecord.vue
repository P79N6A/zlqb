<template>
  <div class="infoTab">
        <div class="title">
          <span class="titname">历史申请记录</span>
        </div>
       <el-table
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column align="center" label="序号" type="index" width="50"></el-table-column>
          <el-table-column
            align='center'
            prop="orderNo"
            label="订单号"
            >
          </el-table-column>
          <el-table-column
            prop="applyDate"
            label="申请日期"
            align='center'>
          </el-table-column>
           <el-table-column
            prop="applyResult"
            label="申请结果"
            align='center'>
          </el-table-column>
           <el-table-column
            prop="approvingOfficer"
            label="审批人"
            align='center'>
          </el-table-column>
           <el-table-column
            prop="applicationOpinion"
            label="审批意见"
            align='center'>
          </el-table-column>
           <el-table-column
            prop="overdueDays"
            label="逾期天数"
            align='center'>
          </el-table-column>
           <el-table-column
            prop="endTime"
            label="到期时间"
            align='center'>
          </el-table-column>
          <el-table-column
            prop="actualRepayTime"
            label="实际还款时间"
            align='center'>
          </el-table-column>
        </el-table>
   </div>
</template>
<script type="text/ecmascript-6">
import api from "@/api/index";
import Pagination from "@/components/Pagination";
  export default {
    data() {
      return {
        tableData:[],
      }
    },
    mounted(){
      this.orderRecordHis()
    },
    methods: {
      orderRecordHis(){
        api.orderRecordHis({
          userId:this.$route.query.userId
        }).then(res => {
            if (res.data.success) {
              this.tableData = res.data.data
            }
        })
      },
    },
  }
</script>
<style lang="less" scoped>
 
</style>