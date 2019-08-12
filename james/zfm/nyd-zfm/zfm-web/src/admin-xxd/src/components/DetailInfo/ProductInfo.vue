<template>
  <div class="infoTab">
        <div class="title">
          <span class="titname">产品信息</span>
          
          <el-tag type="danger" v-if="orderStatus == '审核拒绝'">审核拒绝</el-tag>
          <el-tag type="info" v-else-if="orderStatus == '关闭'">关闭</el-tag>
          <el-tag type="success" v-else>{{orderStatus}}</el-tag>
        </div>
        <table class="laytab">
            <tr>
              <th width="100">贷款编号：</th>
              <td width="300">{{productInfo.orderNo | globalFilter}}</td>
              <th width="120">放款时间：</th>
              <td width="200">{{productInfo.loanTime | globalFilter}}</td>
              <th width="100">结清日期：</th>
              <td>{{productInfo.settleTime | globalFilter}}</td>
            </tr>
            <tr>
              <th>申请时间：</th>
              <td>{{productInfo.applayTime | globalFilter}}</td>
              <th >放款金额：</th>
              <td>{{productInfo.loanAmount | globalFilter}}</td>
              <th >应还本金：</th>
              <td>{{productInfo.shouldPrincipal | globalFilter }}</td>
            </tr>
            <tr>
              <th >申请金额：</th>
              <td>{{productInfo.applayAmount | globalFilter}}</td>
              <th>应还日期：</th>
              <td>{{productInfo.repaymentTime | globalFilter}}</td>
              <th>应还利息：</th>
              <td>{{productInfo.shouldInterest | globalFilter}}</td>
            </tr>
            <tr>
              <th >申请期限：</th>
              <td>{{productInfo.applayPeriods | globalFilter }}天</td>
              <th >应还本金：</th>
              <td>{{productInfo.shouldPrincipal | globalFilter}}</td>
              <th >推荐服务费：</th>
              <td>{{productInfo.managerFee | globalFilter}}</td>
            </tr>
            <tr>
              <th >产品利率：</th>
              <td>{{productInfo.productRate | globalFilter }}</td>
              <th >应还利息：</th>
              <td>{{productInfo.shouldInterest | globalFilter }}</td>
              <th >逾期费用：</th>
              <td>{{productInfo.overdueInterest | globalFilter}}</td>
            </tr>
            <tr>
              <th >放款产品：</th>
              <td>{{productInfo.prodcutName | globalFilter}}</td>
              <th >应还服务费：</th>
              <td>{{productInfo.shouldServiceFee | globalFilter}}</td>
              <th >已还金额：</th>
              <td>{{productInfo.alreadyAmount | globalFilter}}</td>
            </tr>
            <tr>
              <th >借款用途：</th>
              <td>{{productInfo.purpose | globalFilter}}</td>
              <th >实收评估费用：</th>
              <td>{{productInfo.assessmentAmount | globalFilter}}</td>
              <th >剩余应还：</th>
              <td>{{productInfo.remainsAmount | globalFilter}}</td>
            </tr>
        </table>
   </div>
</template>
<script>
import api from "@/api/index";
export default {
  props:[],
  data() {
    return {
      productInfo:{},
      orderStatus:'',
    }
  },
  mounted(){
    this.getOrderProduct()
    this.orderStatus = this.$route.query.status
  },
  methods: {
    getOrderProduct(){
      api.getOrderProduct({
        orderNo:this.$route.query.orderNo
      }).then(res => {
          if (res.data.success) {
            this.productInfo = res.data.data
          }
      })
    },
  },
}
</script>

<style lang="less" scoped>
 
</style>
