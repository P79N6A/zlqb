<template>
  <div class="wrapper" v-wechat-title="title">
     <!-- //产品信息 -->
    <ProductInfo></ProductInfo>
    <!-- //个人基本信息 -->
    <BaseInfo></BaseInfo>
    <!-- //扣款信息 -->
    <DeductionInfo></DeductionInfo>
    <!-- //退款处理记录 -->
    <RefundRecord ></RefundRecord>
    <!-- //协议信息 -->
    <protocolInfo></protocolInfo>
  </div>
</template>
<script>
import HeadTop from "@/components/HeadTop";
import ProductInfo from "@/components/DetailInfo/ProductInfo";
import BaseInfo from "@/components/DetailInfo/BaseInfo";
import DeductionInfo from "@/components/DetailInfo/DeductionInfo";
import ProtocolInfo from "@/components/DetailInfo/ProtocolInfo";
import RefundRecord from "@/components/DetailInfo/RefundRecord";

import api from "@/api/index";
export default {
  data() {
    return {
      title:'',
      channelName:'',
      linkName:'',
      platCode:sessionStorage.getItem('platCode'),
      runDate:sessionStorage.getItem('runDate'),
      currentPage: 1,
      total: 0,
      pageNo: 1,
      pageSize: 10,
      tableData: [],
      loadingTable: false,
    };
  },
  computed: {},
  mounted() {
    this.title = this.$route.query.custName +'订单详情'
  },
  methods: {},
  components: {
     ProductInfo,
     BaseInfo,
     DeductionInfo,
     RefundRecord,
     ProtocolInfo,
  }
}
</script>
<style lang="less" >            
 .wrapper{
   width: 1000px;
   padding: 20px;
   background: #fff;
   .infoTab{
    width: 1000px;
    background: #fff;
    margin-bottom: 20px;
    border-bottom: 1px solid #ddd;
    .title{
      margin-bottom:20px;
      span.titname{
        display: inline-block;
        border-left: solid  rgb(9, 143, 255) 3px;
        color:rgb(9, 143, 255);
        padding-left: 5px;
      }
    }
    .laytab{
      width: 100%;
      margin: 20px 0;
      text-align: left;
      th{
        font-weight: 400;
      }
      td{
        padding:5px;
        word-wrap:break-word;
        word-break:break-all;
        &:nth-child(1){
          width:120px;
        }
        &:nth-child(2){
          width:300px;
        }
         &:nth-child(3){
          width:120px;
        }
      }
    }
  }
 }
</style>