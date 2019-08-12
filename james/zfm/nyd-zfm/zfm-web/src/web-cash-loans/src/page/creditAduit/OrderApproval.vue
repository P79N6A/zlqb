<template>
  <div class="approval-orders common-query">
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
              v-model="formData.accountNumber">
            </el-input>
            <span class="common-label-text">信审人员：</span>
            <el-input
              clearable
              placeholder="请输入"
              v-model="formData.checkPersonnel">
            </el-input>
        </div>
        <div class="common-reset-inptu">
          <span>注册渠道：</span>
            <el-input
              clearable
              placeholder="请输入"
              v-model="formData.appName">
            </el-input>
             <span class="common-label-text"> 分配时间：</span>
             <el-date-picker
              v-model="distributeDate"
              type="daterange"
              format="yyyy 年 MM 月 dd 日"
              value-format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
            <div class="common-flex-btn"><el-button type="primary" @click="search()">查询</el-button></div>
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
            prop="assignName"
            label="信审人员"
            >
          </el-table-column>
          <el-table-column
            prop="loanNumber"
            label="借款期次"
            align='center'>
          </el-table-column>
          <el-table-column
            align='center'
            prop="userName"
            label="客户姓名">
          </el-table-column>
          <el-table-column
            align='center'
            prop="mobile"
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
            prop="loanTime"
             width="180"
            label="申请时间">
          </el-table-column>
          <!-- <el-table-column
            align='center'
            prop="appName"
            label="放款产品">
          </el-table-column> -->
          <el-table-column
            align='center'
            prop="appName"
            label="放款产品">
             <template slot-scope="scope">助乐钱包</template>
          </el-table-column>
          <el-table-column
            align='center'
            prop="assignTime"
             width="180"
            label="分配时间">
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
  </div>
</template>

<script type="text/ecmascript-6">
import Pagination from "@/components/Pagination";
import api from "@/api/index";
 export default {
    data() {
      return {
        distributeDate:[],
        formData: {
          userName: '',
          accountNumber: '', //手机号
          appName: '', //注册渠道
          checkPersonnel: '', //信审人员
          pageNo:1,
          pageSize:10,
          startTime:'',
          endTime:''
        },
        currentPage: 1,
        total:0,
        tableData: []
      }
    },
    mounted(){
      this.orderCheckList()
    },
    methods: {
      search(){
        this.formData.pageNo = 1
        this.formData.pageSize = 10
        this.currentPage = 1
        this.orderCheckList();
      },
      handleSizeChange(val) {
        // 一页显示多少条
        this.currentPage = 1;
        this.formData.pageNo = 1;
        this.formData.pageSize = val;
        this.orderCheckList();
      },
      handleCurrentChange(val) {
        // 页码改变时
        this.formData.pageNo = val;
        this.currentPage = val;
        this.orderCheckList();
      },
      orderCheckList(){
        if(this.distributeDate.length>0){
          this.formData.beginTime =this.distributeDate[0]
          this.formData.endTime =this.distributeDate[1]
        }else{
          this.formData.startTime =''
          this.formData.endTime =''
        }
        api.orderCheckList(this.formData).then(res => {
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
          path: '/creOrderDetail',
          query: {
            orderNo: row.orderNo,
            userName: row.userName,
            userId:row.userId,
            mobile:row.mobile
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