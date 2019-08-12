<template>
  <div class="wrapper" v-wechat-title="title">
     <!-- //基本信息 -->
    <BaseInfo></BaseInfo>
    <!-- //认证信息图片 -->
    <CertificationPic></CertificationPic>
    <!-- //联系人信息 -->
    <ContactInfo></ContactInfo>
    <!-- //历史申请记录 -->
    <HistoryRecord></HistoryRecord>
    <div class="infoTab">
        <div class="title">
          <span class="titname">通讯数据</span>
        </div>
        <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
          <el-tab-pane label="手机通话记录" name="first">
              <CallRecord></CallRecord>
          </el-tab-pane>
          <el-tab-pane label="手机通讯录" name="second">
              <AdressList></AdressList>
          </el-tab-pane>
          <el-tab-pane label="手机短信记录" name="third">
              <MsgRecord></MsgRecord>
          </el-tab-pane>
          <el-tab-pane label="运营商通话记录" name="fourth">
              <CarrierRecord></CarrierRecord>
          </el-tab-pane>
        </el-tabs>
   </div>
  </div>
</template>
<script>
import HeadTop from "@/components/HeadTop";
import CertificationPic from "@/components/DetailInfo/CertificationPic";
import BaseInfo from "@/components/DetailInfo/BaseInfo";
import ContactInfo from "@/components/DetailInfo/ContactInfo";
import HistoryRecord from "@/components/DetailInfo/HistoryRecord";
import CallRecord from "@/components/communicationData/CallRecord";
import AdressList from "@/components/communicationData/AdressList";
import MsgRecord from "@/components/communicationData/MsgRecord";
import CarrierRecord from "@/components/communicationData/CarrierRecord";
import Drag from '@/components/DralogComponent/Drag'
import api from "@/api/index";
export default {
  data() {
    return {
      title:'',
      styleObj:{
        width:'300px',
      },
      activeName:'first',
      channelName:'',
      linkName:'',
      platCode:sessionStorage.getItem('platCode'),
      runDate:sessionStorage.getItem('runDate'),
      currentPage: 1,
      total: 0,
      pageNo: 1,
      pageSize: 10,
      loadingTable: false,
    };
  },
  computed: {},
  mounted() {
     this.title = this.$route.query.userName +'订单详情'
  },
  methods: {
     handleClick(tab, event) {
        console.log(tab, event);
      }
  },
  components: {
     CertificationPic,
     BaseInfo,
     ContactInfo,
     HistoryRecord,
     CallRecord,
     AdressList,
     MsgRecord,
     CarrierRecord,
      Drag,
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
      th{
        font-weight: 400;
      }
      td{
        padding:20px 10px;
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
  .photo{
    display: inline-block;
    margin-bottom: 20px;
    text-align: center;
    p{
      margin-top: 10px;
    }
    img{
   height: 200px;
   width: 300px;
   margin: 0 10px;
    }
  
  }
 }
</style>