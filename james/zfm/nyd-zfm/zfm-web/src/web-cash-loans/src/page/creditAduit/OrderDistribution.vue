<template>
  <div class="order-distribution-wrap common-query">
    <el-card>
      <div class="border-1px padding-10px">
        <div class="common-reset-inptu">
          <span>客户姓名：</span>
            <el-input
              clearable
              placeholder="请输入"
              v-model="formData.userName">
            </el-input>
            <span class="common-label-text">手机号码：</span>
            <el-input
              clearable
              placeholder="请输入"
              v-model="formData.phone">
            </el-input>
            <div class="common-flex-btn query-btn"><el-button type="primary" @click="search">查询</el-button></div>
        </div>
        <div class="common-reset-inptu">
          <span>注册渠道：</span>
          <el-input
            clearable
            placeholder="请输入"
            v-model="formData.channel">
          </el-input>
            <span class="common-label-text">进入时间：</span>
            <el-date-picker
              v-model="formData.times"
              type="daterange"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
            <div class="common-flex-btn"><el-button type="primary" @click="distribute()">人工分配</el-button></div>
        </div>
      </div>
      <div class="common-table">
        <el-table
          :data="tableData"
          border
          style="width: 100%" @selection-change="handleSelectionChange">
           <el-table-column
           label="全选"
            align='center'
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            align='center'
            prop="borrowPeriods"
            label="借款期次"
            >
          </el-table-column>
          <el-table-column
            prop="userName"
            label="客户姓名"
            align='center'>
          </el-table-column>
          <el-table-column
            align='center'
            prop="phone"
             width="120"
            label="手机号码">
          </el-table-column>
           <el-table-column
            align='center'
            prop="channel"
            label="注册渠道">
          </el-table-column>
          <el-table-column
            align='center'
            prop="regTime"
            width="180"
            label="申请时间">
          </el-table-column>
          <el-table-column
            align='center'
            prop="loanProduct"
            label="放款产品">
             <template slot-scope="scope">助乐钱包</template>
          </el-table-column>
          <el-table-column
            align='center'
            prop="inTime"
             width="180"
            label="进入时间">
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
              <el-button type='text' @click="lookDetail(scope.row)">查看详情</el-button>
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
      title="分配信审人员"
      :visible.sync="dialogvisible"
      :close-on-click-modal='false'
      width="30%"
      >
      <el-form  :model="backform" label-width="100px" :rules="backformRules" ref="backform" >
        <el-form-item label="信审人员："  prop="nameInfo">
              <el-select v-model="backform.nameInfo" placeholder="请选择信审人员" value-key ='userId'>
                <el-option
                  v-for="item in judgePeopleAry"
                  :key="item.userId"
                  :label="item.userName"
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
  const year = new Date().getFullYear();
  const month = new Date().getMonth() + 1;
  const day =  new Date().getDate();
  const date = year + '-'  + month + '-' + day
  const invalidNameInfo = (rule, value, callback) => {
    debugger
      var arr = Object.keys(value);
      if(arr.length < 1){
          return callback(new Error('请选择信审人员'));
      }else{
        callback();
      }
  };
  export default {
    data() {
      return {
        dialogvisible:false,
        judgePeopleAry:[],
        formData: {
          userName: '',
          phone: '',
          times: [],
          channel:'',
          pageNo:1,
          pageSize:10,
        },
        currentPage: 1,
        total: 0,
        tableData: [],
        backform:{
          nameInfo:''
        },
        backformRules:{
          nameInfo:[
            // {required: true,  message: "请选择信审人员", trigger: 'change' },
            {validator: invalidNameInfo, trigger: "change" }
          ]
        },
        orderNos:[], //选择的要分配的订单的订单号集合
        judgePeopleAry:[], 
      }
    },
    mounted(){
      this.orderApportionAndUndistributed()
    },
    methods: {
      orderAllocationToUser(){ //确定分配
       this.$refs.backform.validate(valid => {
        if (valid) {
          api.orderAllocationToUser({
            "orderNo":this.orderNos,
            "userId": this.backform.nameInfo.userId,
            "userName": this.backform.nameInfo.userName,
          }).then(res => {
            if (res.data.success) {
              this.$message({
                message: res.data.msg,
                type: 'success'
              });
              this.orderApportionAndUndistributed()
              this.dialogvisible = false
              this.orderNos =[]
            }
          })
        }
       })
        
      },
      orderjudgePeople(){
        api.orderjudgePeople({}).then(res => {
          debugger
          if (res.data.success) {
            this.judgePeopleAry = res.data.data
            debugger
          }
        })
      },
      handleSelectionChange(ary){
        this.orderNos = []
        // if(ary.length == 0){
        //   this.orderNos = []
        // }else{
          for(let i in ary){
            this.orderNos.push(ary[i].orderNo)
          }
        // }
        
        console.log(this.orderNos)
      },
      search(){
        this.formData.pageNo = 1
        this.formData.pageSize = 10
        this.currentPage = 1
        this.orderApportionAndUndistributed();
      },
      handleSizeChange(val) {
        // 一页显示多少条
        this.currentPage = 1;
        this.formData.pageNo = 1;
        this.formData.pageSize = val;
        this.orderApportionAndUndistributed();
      },
      handleCurrentChange(val) {
        // 页码改变时
        this.formData.pageNo = val;
        this.currentPage = val;
        this.orderApportionAndUndistributed();
      },
      orderApportionAndUndistributed(){
         if(this.formData.times.length>0){
           this.formData.inTimeStart = this.formData.times[0]
           this.formData.inTimeEnd = this.formData.times[1]
         } else {
           this.formData.inTimeStart = ''
           this.formData.inTimeEnd = ''
         }
        api.orderApportionAndUndistributed(this.formData).then(res => {
             if (res.data.success) {
              this.tableData = res.data.data
              this.total = res.data.total
             }
        })
      },
      distribute(){
        if(this.orderNos.length>0){
          this.orderjudgePeople()
          this.dialogvisible = true
        }else{ //请至少选中一条数据
          this.$message({
            message: '请至少选中一条数据',
            type: 'warning'
          });
        }
      },
      lookDetail(row){
        var routeData = this.$router.resolve({
          path: '/distibuteOrderDetail',
          query: {
             orderNo: row.orderNo,
             userId:row.userId,
             userName:row.userName,
             mobile:row.phone
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
<style lang="less">
  .order-distribution-wrap {
    .common-reset-inptu {
      .el-range-editor {
        width: 230px;
      }
      .el-input {
         width: 230px;
      }
      .common-flex-btn .el-button {
        width: 88px;
      }
    }
}
</style>
<style lang="less" scoped>

.query-btn {

}
</style>