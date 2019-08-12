<template>
  <div class="common-query">
    <el-card>
      <CommonLoanSearch
        :limitFormData="limitFormData"
        @searchHand="searchHand"
        @disCustHander="disCustHander"
      />
      <div class="common-table">
        <el-table v-loading = 'ladingTable' :data="tableData" :highlight-current-row="true" border style="width: 100%">
          <el-table-column show-overflow-tooltip align="center" label="序号" type="index" width="50"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="userName" label="客户姓名"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="userMobile" label="手机号码"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="repayPrinciple" label="借款金额"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="applyTime" label="借款日期"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="promiseRepaymentDate" label="应还日期"></el-table-column>
          <el-table-column
            show-overflow-tooltip
            align="center"
            prop="waitRepayAmount"
            width="120px"
            label="剩余应还金额"
          ></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="overdueDays" label="逾期天数"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="receiveUserName" label="贷后专员"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="urgeStatusName" label="订单状态"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="alreadyRepayAmount" label="已还金额"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="orderNo" width="200px" label="贷款编号"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="createTime" label="分配时间"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="mobile" label="操作" fixed='right'>
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="openDetail(scope.row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="pagination_wrap com_cen">
        <Pagination
          :currentPage= "currentPage"
          :total="total"
          :myPageSizes= "formData.pageSize"
          @handleSizeChange= "handleSizeChangeHand"
          @handleCurrentChange= "handleCurrentChangeHand"
        ></Pagination>
      </div>
    </el-card>
  </div>
</template>
<script>
import CommonLoanSearch from "@/page/afterLoanManger/CommonLoanSearch"
import Pagination from "@/components/Pagination"
import api from "@/api/index"
export default {
  data() {
    return {
      formData: {
        pageNo:1,
        pageSize:10,
      },
      currentPage: 1,
      total: 0,
      ladingTable: false,
      limitFormData: {
        // 人工分配 按钮
        disCustHanderBtn: false,
        // 分配时间
        refundDate: true,
        // 订单状态
        LoanOrderState: true,
        receiveUserName: true
      },
      tableData: []
    };
  },
  created() {
    this.LoanOverdueAssignList()
  },
  methods: {
    disCustHander(val) {
      console.log(val);
    },
    searchHand(val) {
      this.formData = Object.assign(this.formData,val)
      this.LoanOverdueAssignList()
      console.log(this.formData, 9999123);
    },
    handleSizeChangeHand(val) {
      // 一页显示多少条
      this.currentPage = 1;
      this.formData.pageNo = 1;
      this.formData.pageSize = val;
      this.LoanOverdueAssignList();
    },
    handleCurrentChangeHand(val) {
      // 页码改变时
      this.formData.pageNo = val;
      this.currentPage = val;
      this.LoanOverdueAssignList();
    },
    LoanOverdueAssignList() {
      let pararms = {
        ...this.formData
      }
      if (this.formData.promiseRepayment) {
        // 应还日期
        pararms.promiseRepaymentStart = this.formData.promiseRepayment[0]
        pararms.promiseRepaymentEnd = this.formData.promiseRepayment[1]
      }
      if (this.formData.createTime) {
        // 分配时间
        pararms.createTimeStart = this.formData.createTime[0]
        pararms.createTimeEnd = this.formData.createTime[1]
      }
      delete pararms.promiseRepayment
      delete pararms.createTime
      this.ladingTable = true
      api.LoanOverdueAssignList(pararms).then(res => {
        if (res.data.success) {
          this.tableData = res.data.data
          this.total = res.data.total
        } else {
          this.$notify.error({
            title: '提示',
            message: res.data.msg
          });
        }
        this.ladingTable = false
      })
    },
    openDetail(row) {
      // 打开详情
      var routeData = this.$router.resolve({
        path: '/postloanDetails',
        query: {
          orderNo: row.orderNo,
          userId: row.userId,
          userName: row.userName,
          mobile: row.userMobile
        }
      });
      window.open(routeData.href)
    },
  },
  components: {
    CommonLoanSearch,
    Pagination
  }
};
</script>
<style lang="less" scoped>
.el-range-editor {
  width: 230px;
}
</style>
