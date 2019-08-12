<template>
  <div class="wrapper" v-wechat-title="title">
     <!-- //产品信息 -->
    <ProductInfo ></ProductInfo>
    <!-- //个人基本信息 -->
    <BaseInfo></BaseInfo>
    <!-- //扣款信息 -->
    <DeductionInfo></DeductionInfo>
    <!-- //退款处理记录 -->
    <RefundRecord ref="refundRecords"></RefundRecord>
    <!-- //协议信息 -->
    <protocolInfo></protocolInfo>
    <Drag :style="styleObj">
      <div class="refundbox">
          <p class="tit">新增退款申请</p>
          <el-form  :model="refundForm" label-width="100px" :rules="refundFormRule" ref="refundForm">
            <el-form-item label="退款日期：" prop="backdate">
                <el-date-picker
                  v-model="refundForm.backdate"
                   :picker-options="pickerOptions"
                  type="date"
                  placeholder="选择日期"
                  format="yyyy 年 MM 月 dd 日"
                   value-format="yyyy-MM-dd">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="退款金额："  prop="cashnum">
              <el-input v-model="refundForm.cashnum"></el-input>
            </el-form-item>
            <el-form-item label="备注：" prop="remark">
              <el-input
                type="textarea"
                :rows="2"
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
import ProductInfo from "@/components/DetailInfo/ProductInfo";
import BaseInfo from "@/components/DetailInfo/BaseInfo";
import DeductionInfo from "@/components/DetailInfo/DeductionInfo";
import ProtocolInfo from "@/components/DetailInfo/ProtocolInfo";
import RefundRecord from "@/components/DetailInfo/RefundRecord";
import Drag from '@/components/DralogComponent/Drag'
import api from "@/api/index";
export default {
  data() {
    return {
      title:'',
      pickerOptions:{
        disabledDate(time) {
            let curDate = (new Date()).getTime();
            let three = 4 * 24 * 3600 * 1000;
            let threeMonths = curDate + three;
            return time.getTime() < Date.now()- 24 * 3600 * 1000 || time.getTime() > threeMonths;;
          }
      },
      orderNo:'',
      styleObj:{
        width:'350px'
      },
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
      refundForm:{
         backdate:'',
        cashnum:'',
        remark:''
      },
      refundFormRule:{
        backdate:[{
           required: true,  message: "请选择日期", trigger: 'change' 
        }],
        cashnum: [
            { required: true,  message: "请填写金额", trigger: 'blur' },
            { pattern:/^\d\.([1-9]{1,2}|[0-9][1-9])$|^[1-9]\d{0,1}(\.\d{1,2}){0,1}$|^99(\.0{1,2}){0,1}$/, message: "输入大于0小于99的金额", trigger: 'blur'}
        ],
      },
    }
  },
  computed: {},
  mounted() {
    this.title = this.$route.query.custName +'订单详情'
  },
  methods: {
    saveRefundRecord(){
      api.saveRefundRecord({
        orderNo:this.$route.query.orderNo,
        refundTime:this.refundForm.backdate,
        refundAmount:this.refundForm.cashnum,
        remark:this.refundForm.remark,
      }).then(res => {
          if (res.data.success) {
             this.$message({
                message: res.data.msg,
                type: 'success'
             });
             this.$refs.refundForm.resetFields()
             this.$refs.refundRecords.refundListRecord();
          }else{
             this.$message({
                message: res.data.msg,
                type: 'error'
             });
          }
      })
    },
    submitBtn(){
      this.$refs['refundForm'].validate((valid) => {
          if (valid) {
            this.saveRefundRecord()
          }
      })
      
    }
  },
  components: {
     ProductInfo,
     BaseInfo,
     DeductionInfo,
     RefundRecord,
     ProtocolInfo,
     Drag
  }
}
</script>
<style lang="less" >            
 .wrapper{
   width: 1000px;
   padding: 20px;
   background: #fff;
   .highlight{
     color: #FF9900;
     padding-left: 30px;
   }
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
      text-align: left;
      th{
        padding: 5px;
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
  .refundbox{
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