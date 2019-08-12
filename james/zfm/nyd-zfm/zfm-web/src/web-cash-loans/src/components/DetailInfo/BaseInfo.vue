<template>
  <div class="infoTab">
        <div class="title">
          <span class="titname">个人基本信息</span>
        </div>
        <table class="laytab">
            <tr>
              <th width="120">客户姓名：</th>
              <td >{{custInfo.custName}}</td>
              <th width="120">月收入：</th>
              <td >{{custInfo.monthlyIncome}}</td>
            </tr>
            <tr>
              <th >手机号码：</th>
              <td>{{custInfo.mobile}}</td>
              <th >学历：</th>
              <td>{{custInfo.education}}</td>
            </tr>
            <tr>
              <th >身份证号码：</th>
              <td>{{custInfo.custIc}}</td>
              <th >婚姻：</th>
              <td>{{custInfo.marriage}}</td>
            </tr>
            <tr>
              <th>单位名称：</th>
              <td>{{custInfo.companyName}}</td>
              <th >职业类型：</th>
              <td>{{custInfo.jobType}}</td>
            </tr>
            <tr>
              <th >单位地址：</th>
              <td>{{custInfo.companyAddress}}</td>
              <th >职业：</th>
              <td>{{custInfo.job}}</td>
            </tr>
            <tr>
              <th>常住地址：</th>
              <td>{{custInfo.homeAddress}}</td>
              <td colspan ="2"><a @click="lookReport()" class="pointer">查看批核报告</a></td>
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
      custInfo:{},
      flag:false,
    }
  },
  mounted(){
    this.getCustInfo()
    this.checkReport()
  },
  methods: {
    checkReport(){
      api.checkReport({
        orderNo:this.$route.query.orderNo
      }).then(res => {
          if (res.data.success) {
           this.flag = res.data.data //返回true没有报告
          }
      })
    },
    lookReport(){
        if(this.$route.query.orderNo && !this.flag){
          window.open('http://dist.ugiygk.cn/#/creditReport?orderNo=' + this.$route.query.orderNo + '&userId=' + this.$route.query.userId)
        }
        
    },
    getCustInfo(){
      api.getCustInfo({
        orderNo:this.$route.query.orderNo
      }).then(res => {
          if (res.data.success) {
            this.custInfo = res.data.data
          }
      })
    },
  },
}
</script>
<style lang="less" scoped>
 .pointer{
   cursor:pointer;
   color: #FF9900;
 }
</style>
