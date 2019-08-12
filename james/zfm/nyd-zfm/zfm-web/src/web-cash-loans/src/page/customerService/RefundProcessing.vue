<template>
  <div class="approval-orders common-query">
    <el-card>
      <div class="border-1px padding-10px">
        <div class="common-reset-inptu">
          <span>客户姓名：</span>
            <el-input
              clearable
              placeholder="请输入"
              v-model="formData.name">
            </el-input>
            <span class="common-label-text">手机号码：</span>
            <el-input
              clearable
              placeholder="请输入"
              v-model="formData.phone">
            </el-input>
            <span class="common-label-text">退款时间：</span>
              <el-date-picker
                v-model="refundDate"
                @change="refundDateChange()"
                type="daterange"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
              <br/>
              <span class="common-label-text" style="margin:15px 0 0 0">退款结果：</span>
              <el-select v-model="formData.status" placeholder="请选择" clearable>
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
              <div class="common-flex-btn"><el-button type="primary" @click="search()">查询</el-button></div>
        </div>
        <div class="common-reset-inptu">
          <ul>
            <li>
              <strong>今日共收入：</strong>
              <span class="money">{{sumInfo.totalIncome}}</span>
              <span class="money-text"> 元</span>
            </li>
            <li>
              <strong>已退款：</strong>
              <span class="money" style="color:red">{{sumInfo.refunded}}</span>
              <span class="money-text"> 元</span>
              </li>    
            <li>
              <strong>退款比例：{{sumInfo.refundRatio}}%</strong>
            </li>
          </ul>
        </div>
      </div>
      <div class="common-table">
        <el-table
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column
            align='center'
            label="序号"
            type="index"
            width="50">
          </el-table-column>
          <el-table-column
            align='center'
            prop="refundDate"
            label="期望退款时间"
            >
          </el-table-column>
          <el-table-column
            prop="statusName"
            label="退款结果"
            align='center'>
          </el-table-column>
          <el-table-column
            prop="customerName"
            label="客服"
            align='center'>
          </el-table-column>
          <el-table-column
            align='center'
            prop="refundAmount"
            label="申请退款金额">
          </el-table-column>
          <el-table-column
            align='center'
            prop="name"
            label="客户姓名">
          </el-table-column>
          <el-table-column
            align='center'
            prop="phone"
            label="手机号码">
          </el-table-column>
          <el-table-column
            align='center'
            prop="orderNo"
            width="180"
            label="贷款编号">
          </el-table-column>
          <el-table-column
            align='center'
            fixed='right'
            label="操作">
             <template slot-scope="scope">
              <el-button type='text'  @click="lookDetail(scope.row)">查看详情</el-button>
              
              <el-button type='text' @click="reback(scope.row)" v-if="scope.row.status == '0' || scope.row.status == '3'">退款</el-button>
              <el-button type='text'  v-else disabled>退款</el-button>
            </template>
          </el-table-column>
        </el-table>
         <div class="pagination_wrap com_cen">
          <Pagination
            :currentPage="currentPage"
            :total="total"
            :myPageSizes="formData.pageSize"
            @handleSizeChange="handleSizeChange"
            @handleCurrentChange="handleCurrentChange"
          ></Pagination>
        </div>
      </div>
    </el-card>
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
import Pagination from "@/components/Pagination";
import api from "@/api/index";
  const year = new Date().getFullYear();
  const month = new Date().getMonth() + 1;
  const day =  new Date().getDate();
  const date = year + '-'  + month + '-' + day
 export default {
    data() {
      return {
        options:[
          {
            value:0,
            label:'待处理'
          },
          {
            value:1,
            label:'处理中'
          },
          {
            value:2,
            label:'已退款'
          },
          {
            value:3,
            label:'退款失败'
          },
          {
            value:4,
            label:'拒绝'
          },
        ],
        refundId:'',
        userId:'',
        sumInfo:{},
        dialogvisible:false,
        refundDate:[],
        formData: {
          name: '',
          phone: '',
          status:null,
          pageSize:10,
          pageNo:1
        },
        tableData: [],
        currentPage: 1,
        total: 0,
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
      }
    },
    created(){
      this.getTime()
    },
    mounted(){
      this.refundList()
      this.refundRatio()
    },
    methods: {
      refundDateChange(a){
      },
      getTime () {
        let date = new Date()
        let y = date.getFullYear()
        let m = date.getMonth() + 1
        let d = date.getDate()
        console.log(m,d)
        if(m<10){
          m = '0'+ m
        }
        if(d<10){
          d = '0'+ d
        }
        let time = y + '-' + m + '-' + d
        this.refundDate = [time,time]
      },
      refundMoney(){
        api.refundMoney({
          userId:this.userId,
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
              this.refundList()
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
      refundRatio(){ //表格上面数据汇总
        api.refundRatio().then(res => {
          if (res.data.success) {
            this.sumInfo = res.data.data
          }
        })
      },
      lookDetail(row){
        var routeData = this.$router.resolve({
          path: '/refundOrderDetail',
          query: {
            orderNo: row.orderNo,
            status:row.orderStatus,
            custName:row.name,
            userId:row.userId,
            operate:true,
            custIdRepore:row.custId
          }
        });
        window.open(routeData.href)
      },
      reback(row){
        this.dialogvisible=true
        // this.$refs.backform.resetFields()
        this.refundId = row.id
        this.userId = row.userId
        this.backform.cashnum = row.refundAmount
      },
      search(){
        this.formData.pageNo = 1
        this.formData.pageSize = 10
        this.currentPage = 1
        this.refundList();
      },
      refundList(){
        if(this.refundDate  && this.refundDate.length>0 ){
          this.formData.beginTime =this.refundDate[0]
          this.formData.endTime =this.refundDate[1]
        }else{
          this.formData.beginTime =''
          this.formData.endTime =''
        }
        api.refundList(this.formData).then(res => {
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
          this.total = res.data.total
      })
      },
      handleSizeChange(val) {
        // 一页显示多少条
        this.currentPage = 1;
        this.formData.pageNo = 1;
        this.formData.pageSize = val;
        this.refundList();
      },
      handleCurrentChange(val) {
        // 页码改变时
        this.formData.pageNo = val;
        this.formData.currentPage = val;
        this.refundList();
      },
    },
    components: {
      Pagination
    }
  }
</script>

<style lang="less" scoped>
.border-1px {
    min-width: 1140px;
}
  .common-reset-inptu {
    overflow: hidden;
    ul  {
      float: right;
      li {
        float: left;
        margin-right: 50px;
        strong {
          font-size: 18px;
        }
        .money {
          color: green;
          font-family: 'Arial Negreta', 'Arial';
          font-weight: 700;
          font-size: 18px;
        }
      }
    }
  }
</style>