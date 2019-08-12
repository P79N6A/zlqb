<template>
  <div class="infoTab">
        <div class="title" >
          <span class="titname">退款处理记录</span>
          <label class="highlight">期望退款总额：{{sumInfo.refundAmount}}元， 已退款总额：{{sumInfo.realRefundAmount}}元</label>
        </div>
        <el-table
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column
            align='center'
            prop="refundDate"
            label="期望退款时间"
            >
          </el-table-column>
          <el-table-column
            align='center'
            prop="refundAmount"
            label="期望退款金额"
            >
          </el-table-column>
          <el-table-column
            prop="statusName"
            label="退款结果"
            align='center'>
          </el-table-column>
          <el-table-column
            align='center'
            prop="realRefundAmount"
            label="退款金额">
             <template slot-scope="scope">
               {{scope.row.realRefundAmount | globalFilter}}
            </template>
          </el-table-column>
          <el-table-column
            align='center'
            prop="remarks"
            width="150"
            label="备注">
            <template slot-scope="scope">
               {{scope.row.remarks | globalFilter}}
            </template>
          </el-table-column>
          <el-table-column
            align='center'
            prop="realRefundDate"
            label="实际退款时间">
            <template slot-scope="scope">
               {{scope.row.realRefundDate | globalFilter}}
            </template>
          </el-table-column>
        <el-table-column
            align='center'
            label="操作">
            <template slot-scope="scope">
              <el-button type='text' @click="reback(scope.row)" v-if="scope.row.status == '0' || scope.row.status == '3'">退款</el-button>
              <el-button type='text'  v-else disabled >退款</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-dialog
          title="退款"
          :visible.sync="dialogvisible"
          :close-on-click-modal='false'
          width="30%"
          >
          <el-form  :model="backform" label-width="100px" :rules="backformRules" ref="backform" >
            <el-form-item label="结果：" required>
              <el-radio v-model="backform.result" :label="1">同意退款</el-radio>
              <el-radio v-model="backform.result" :label="0">拒绝退款</el-radio>
            </el-form-item>
            <el-form-item label="金额："  prop="cashnum" v-if="backform.result == '1'">
              <el-input v-model="backform.cashnum"></el-input>
            </el-form-item>
            <el-form-item label="备注：" prop="remark">
              <el-input
                type="textarea"
                :rows="3"
                placeholder="请输入内容"
                v-model="backform.remark">
              </el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogvisible = false">取 消</el-button>
            <el-button type="primary" @click="submitBtn()">确 定</el-button>
          </span>
    </el-dialog>
   </div>
</template>
<script type="text/ecmascript-6">
import api from "@/api/index";
export default {
  props:[],
  data() {
    return {
      tableData:[],
      dialogvisible:false,
      refundId:'',
      custId:'',
      backform:{
        result:1,
        cashnum:'',
        remark:''
      },
      backformRules: {
        cashnum: [
          { required: true,  message: "请填写金额", trigger: 'blur' },
          { pattern:/^\d\.([1-9]{1,2}|[0-9][1-9])$|^[1-9]\d{0,1}(\.\d{1,2}){0,1}$|^99(\.0{1,2}){0,1}$/, message: "输入大于0小于99的金额", trigger: 'blur'}
        ]
      },
      sumInfo:{
        refundAmount:0,
        realRefundAmount:0
      },
    }
  },
  mounted(){
    this.refundListRecord()
    this.sumAmount()
  },
  methods: {
    sumAmount(){
      api.sumAmount({
         orderNo:this.$route.query.orderNo
      }).then(res => {
          if (res.data.success && res.data.data) {
            this.sumInfo = res.data.data
          }
      })
    },
    reback(row){
      this.dialogvisible=true
      // this.$refs.backform.resetFields()
      this.refundId = row.id
      this.custId = row.userId
    },
    refundMoney(){
        api.refundMoney({
          custId:this.custId,
          refundId:this.refundId,	
          isResult:this.backform.result,	//结果:1 同意 0 拒绝
          refundAmount:this.backform.cashnum,
          remark:this.backform.remark,
        }).then(res => {
            if (res.data.success) {
              this.$message({
                  message: res.data.msg,
                  type: 'success'
              });
              this.dialogvisible=false
              this.$refs.backform.resetFields()

            }else{
              this.$message({
                  message: res.data.msg,
                  type: 'error'
              });
            }
        })
      },
    submitBtn(){
      this.$refs['backform'].validate((valid) => {
          if (valid) {
            this.refundMoney()
          }
      })
      
    },
    refundListRecord(){
      api.refundListRecord({
        orderNo:this.$route.query.orderNo
      }).then(res => {
          if (res.data.success) {
            this.tableData = res.data.data
             for(let i in this.tableData){
              switch (this.tableData[i].status)
              {
              case '0':
                  this.tableData[i].statusName = '待处理';
                  break;
              case '1':
                  this.tableData[i].statusName = '处理中';
                  break;
              case '2':
                  this.tableData[i].statusName = '已退款';
                  break;
              case '3':
                  this.tableData[i].statusName = '退款失败';
                  break;
              case '4':
                  this.tableData[i].statusName = '拒绝';
                  break;
              default:
                  this.tableData[i].statusName = '--';
              }
            }
          }
      })
    },
  },
}
</script>
<style lang="less" scoped>
 
</style>