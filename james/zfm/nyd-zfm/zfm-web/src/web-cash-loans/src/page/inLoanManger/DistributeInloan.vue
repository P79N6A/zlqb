<template>
  <div class="approval-orders common-query">
    <el-card>
      <div class="border-1px padding-10px">
        <div class="common-reset-inptu">
          <span>客户姓名：</span>
            <el-input
              clearable
              placeholder="请输入"
              v-model="formData.custName">
            </el-input>
            <span class="common-label-text">手机号码：</span>
            <el-input
              clearable
              placeholder="请输入"
              v-model="formData.mobile">
            </el-input>
        </div>
        <div class="common-reset-inptu">
          <span>注册渠道：</span>
            <el-input
              clearable
              placeholder="请输入"
              v-model="formData.source">
            </el-input>
             <span class="common-label-text"> 贷款编号：</span>
             <el-input
              clearable
              placeholder="请输入"
              v-model="formData.loanNo">
            </el-input>
             <span class="common-label-text">距还款日：</span>
              <el-checkbox-group v-model="formData.paymentDaysArry">
                <el-checkbox label="0"></el-checkbox>
                <el-checkbox label="1"></el-checkbox>
                <el-checkbox label="2"></el-checkbox>
              </el-checkbox-group>
            <div class="common-flex-btn">
              <el-button type="primary" @click="search()">查询</el-button>
              <el-button type="primary" @click="distribute()">人工分配</el-button>
            </div>
        </div>
      </div>
      <div class="common-table">
        <el-table
          :data="tableData"
          border
          style="width: 100%"  @selection-change="handleSelectionChange">
          <el-table-column
           label="全选"
            align='center'
            type="selection"
            width="55">
          </el-table-column>
           <el-table-column
            align='center'
            label="序号"
            type="index"
            width="50">
          </el-table-column>
          <el-table-column
            align='center'
            prop="userName"
            label="客户姓名">
          </el-table-column>
          <el-table-column
            align='center'
            prop="userMobile"
             width="120"
            label="手机号码">
          </el-table-column>
          <el-table-column
            align='center'
            prop="applyTime"
             width="180"
            label="申请日期">
          </el-table-column>
          <el-table-column
            align='center'
            prop="loanTime"
             width="180"
            label="放款日期">
          </el-table-column>
           <!-- <el-table-column
            align='center'
            prop="productName"
            label="放款产品">
             <template slot-scope="scope">助乐钱包</template>
          </el-table-column> -->
          <el-table-column
            align='center'
            prop="source"
            label="注册渠道">
          </el-table-column>
           <el-table-column
            align='center'
            prop="loanNum"
            label="借款期次">
          </el-table-column>
          <el-table-column
            align='center'
            prop="repayPrinciple"
             width="180"
            label="放款金额">
          </el-table-column>
          <el-table-column
            align='center'
            prop="promiseRepaymentDate"
            width="180"
            label="应还日期">
          </el-table-column>
          <el-table-column
            align='center'
            prop="lessDays"
            width="180"
            label="距还款日">
          </el-table-column>
          <el-table-column
            align='center'
            prop="curRepayAmount"
            width="180"
            label="应还本息">
          </el-table-column>
          <el-table-column
            align='center'
            prop="creditTrialUserName"
            width="180"
            label="信审人员">
          </el-table-column>
          <el-table-column
            align='center'
            prop="orderNo"
            width="180"
            label="贷款编号">
          </el-table-column>
          <el-table-column
            align='center'
            prop="address"
            fixed='right'
            label="操作">
             <template slot-scope="scope">
              <el-button type='text' @click="lookDetail(scope.row)">查看详情</el-button>
            </template>
          </el-table-column>

        </el-table>
      </div>
      <div class="pagination_wrap com_cen">
        <Pagination
          :currentPage="currentPage"
          :total="total"
          :myPageSizes="formData.pageSize"
          @handleSizeChange="handleSizeChange"
          @handleCurrentChange="handleCurrentChange"
        ></Pagination>
      </div>
    </el-card>
    <el-dialog
      title="分配提醒人员"
      :visible.sync="dialogvisible"
      :close-on-click-modal='false'
      width="30%"
      >
      <el-form  :model="backform" label-width="100px" :rules="backformRules" ref="backform" >
        <el-form-item label="提醒人员："  prop="nameInfo">
              <el-select v-model="backform.nameInfo" placeholder="请选择提醒人员" value-key ='id' clearable>
                <el-option
                  v-for="item in RemindPeopleAry"
                  :key="item.id"
                  :label="item.loginName"
                  :value="item">
                </el-option>
              </el-select>
         </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogvisible = false">取 消</el-button>
        <el-button type="primary" @click="orderAllocationToUser()" >确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script type="text/ecmascript-6">
import Pagination from "@/components/Pagination";
import api from "@/api/index";
const invalidNameInfo = (rule, value, callback) => {
      var arr = Object.keys(value);
      if(arr.length < 1){
          return callback(new Error('请选择提醒人员'));
      }else{
        callback();
      }
  };
 export default {
    data() {
      return {
        orderNos:[],
        RemindPeopleAry:[],
        dialogvisible:false,
        distributeDate:[],
        formData: {
          custName: '',
          mobile: '', //手机号
          source: '', //注册渠道
          loanNo:'',//贷款编号
          pageNo:1,
          pageSize:10,
          paymentDaysArry :['0','1','2'],//距还款日
          paymentDays:'',
        },
        currentPage: 1,
        total:0,
        tableData: [],
        backform:{
          nameInfo:{}
        },
        backformRules:{
          nameInfo:[
            // {required: true,  message: "请选择信审人员", trigger: 'change' },
            {validator: invalidNameInfo, trigger: "change" }
          ]
        },
      }
    },
    mounted(){
      this.getReadyDistributionList()
    },
    methods: {
      orderAllocationToUser(){
        this.$refs.backform.validate(valid => {
        if (valid) {
          api.doDistribution({
            "orderNos":this.orderNos,
            "receiveUserId": this.backform.nameInfo.id,
            "receiveUserName": this.backform.nameInfo.loginName,
          }).then(res => {
            if (res.data.success) {
              this.$message({
                message: res.data.msg,
                type: 'success'
              });
              this.getReadyDistributionList()
              debugger
              this.dialogvisible = false
              this.orderNos =[]
            }
          })
        }
       })
      },
      distribute(){
        if(this.orderNos.length>0){
          this.getCreditTrialUsersList()
          this.dialogvisible = true
        }else{ //请至少选中一条数据
          this.$message({
            message: '请至少选中一条数据',
            type: 'warning'
          });
        }
      },
      getCreditTrialUsersList(){
        api.getCreditTrialUsersList({}).then(res => {
          if (res.data.success) {
            this.RemindPeopleAry = res.data.data
           
          }
        })
      },
       handleSelectionChange(ary){
        this.orderNos = []
          for(let i in ary){
            this.orderNos.push(ary[i].orderNo)
          }
        console.log(this.orderNos)
      },
      search(){
        this.formData.pageNo = 1
        this.formData.pageSize = 10
        this.currentPage = 1
        this.getReadyDistributionList();
      },
      handleSizeChange(val) {
        // 一页显示多少条
        this.currentPage = 1;
        this.formData.pageNo = 1;
        this.formData.pageSize = val;
        this.getReadyDistributionList();
      },
      handleCurrentChange(val) {
        // 页码改变时
        this.formData.pageNo = val;
        this.currentPage = val;
        this.getReadyDistributionList();
      },
      getReadyDistributionList(){
        if(this.formData.paymentDaysArry.length > 0){
          this.formData.paymentDays = this.formData.paymentDaysArry.join(',')
        }else{
          this.formData.paymentDays = ''
        }
        api.getReadyDistributionList(this.formData).then(res => {
          if (res.data.success) {
            this.tableData = res.data.data
            this.total = res.data.total
          }
        })
      },
      lookDetail(row){
        debugger
        // this.$router.push({ path: './CreditAduitOrderDetail' })
        var routeData = this.$router.resolve({
          path: '/distributeDetailInloan',
          query: {
            orderNo: row.orderNo,
            userName: row.userName,
            userId:row.userId,
            mobile:row.userMobile
          }
        });
        window.open(routeData.href)
      },
    },
    components: {
      Pagination
    }
  }
</script>

<style lang="less" scoped>
.el-checkbox-group{
  display: inline-block;
}
  .customer-orders {
    position: relative;
  }
  .pice-one {
    position: relative;
    padding: 20px;
  }
  .show-detail {
    position: relative;
  }
  .pp-item {
    margin-bottom: 20px;
    border: 1px #999999 solid;
    padding: 15px;
  }
  .dk-detail {
    height: 30px;
  }
</style>