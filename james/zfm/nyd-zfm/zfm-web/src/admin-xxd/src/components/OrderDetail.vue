<template>
  <div class="wrapper">
    <ProductInfo></ProductInfo>
    <BaseInfo></BaseInfo>
    <DeductionInfo></DeductionInfo>
    <RefundRecord></RefundRecord>
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
      th{
        font-weight: 400;
      }
      td{
        padding:20px 10px;
      }
    }
  }
 }
</style>