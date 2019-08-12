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
    <Drag :style="styleObj">
        <div class="refundbox">
          <p class="tit">审核信息</p>
          <el-form  :model="refundForm" label-width="100px" :rules="refundFormRule" ref="refundForm">
            <el-form-item label="审核产品："  prop="product">
              <el-select v-model="refundForm.product" placeholder="请选择审核产品">
                <el-option
                  v-for="item in prOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="审核意见：" prop="remark">
              <el-input
                type="textarea"
                :rows="5"
                placeholder="请输入内容"
                v-model="refundForm.remark">
              </el-input>
            </el-form-item>
          </el-form>
          <el-button type="primary" class="right" @click="submitBtn()">提 交</el-button>
        </div>
    </Drag>
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
        width:'350px'
      },
      prOptions:[
        {
          value: '1000',
          label: '1000'
        },
        // {
        //   value: '1500',
        //   label: '1500'
        // }, 
        {
          value: '0',
          label: '拒绝'
        }
      ],
      refundForm:{
        product:'',
        remark:''
      },
      refundFormRule:{
        product:[{
           required: true,  message: "请选择产品", trigger: 'change' 
        }],
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
    submitBtn(){
      this.$refs.refundForm.validate(valid => {
        if (valid) {
          this.examineInfo()
        }
      })
    },
    examineInfo(){
      api.examineInfo({
        orderNo:this.$route.query.orderNo,
        checkProduct:this.refundForm.product,
        remark:this.refundForm.remark
      }).then(res => {
          if (res.data.success) {
            this.$message.success(res.data.msg);
          }else{
            this.$message.error(res.data.msg);
          }
      })
    },
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
    .refundbox{
     .tit{
       border-left: solid  rgb(9, 143, 255) 3px;
       padding-left: 5px;
       color: rgb(9, 143, 255);
       margin-bottom: 20px;
     }
   }
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
  .refundbox{
    .right{
      float: right;
      margin-right: 55px;
    }
    .el-input{
      width:180px;
    }
    .el-textarea{
      width: 180px;
    }
    .right{
      float: right;
      margin-right: 55px;
    }
    .el-picker-panel{
      z-index: 123456790
    }
  }
 }
</style>