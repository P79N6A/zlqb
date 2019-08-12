<template>
  <div class="infoTab">
        <div class="title" >
          <span class="titname">扣款信息</span>
          <label class="highlight">合计已扣金额：{{totalAmount}}元</label>
        </div>
        <el-table
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column
            align='center'
            prop="requestTime"
            label="扣款时间"
            >
          </el-table-column>
          <el-table-column
            prop="money"
            label="扣款金额"
            align='center'>
          </el-table-column>
          <el-table-column
            align='center'
            label="扣款结果">
            <template slot-scope="scope">
              成功
            </template>
          </el-table-column>
        </el-table>
   </div>
</template>
<script type="text/ecmascript-6">
import api from "@/api/index";
export default {
  props:[],
  data() {
    return {
      tableData:[],
      totalAmount:''
    }
  },
  mounted(){
    this.getDeductInfo()
  },
  methods: {
    getDeductInfo(){
      api.riskSuccessFlow({
        orderNo:this.$route.query.orderNo
      }).then(res => {
          if (res.data.success) {
            this.tableData = res.data.data.list
            //  for(let i in this.tableData){
            //   switch (this.tableData[i].deductResults)
            //   {
            //   case 0:
            //       this.tableData[i].statusName = '初始化';
            //       break;
            //   case 1:
            //       this.tableData[i].statusName = '代扣中';
            //       break;
            //   case 2:
            //       this.tableData[i].statusName = '代扣完成';
            //       break;
            //   case 3:
            //       this.tableData[i].statusName = '代扣失败';
            //       break;
            //   default:
            //       this.tableData[i].statusName = '--';
            //   }
            // }
             
            this.totalAmount = res.data.data.money
            debugger
          }
      })
    },
  },
}
</script>
<style lang="less" scoped>
 
</style>
