<template>
  <div class="common-query">
    <el-card>
      <CommonLoanSearch
        :limitFormData="limitFormData"
        @searchHand="searchHand"
        @disCustHander="disCustHander"
      />
      <div class="common-table">
        <el-table 
          :data="tableData" 
          v-loading = 'ladingTable' 
          :highlight-current-row="true" 
          border style="width: 100%" 
          @select-all = "selectAllHand"
          @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" label="序号" type="index" width="50"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="userName"  label="客户姓名"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="userMobile"  label="手机号码"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="repayPrinciple"  label="借款金额"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="applyTime"  label="借款日期"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="promiseRepaymentDate"  label="应还日期"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="waitRepayAmount" width='120px' label="剩余应还金额"></el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="overdueDays"  label="逾期天数"></el-table-column>          
          <el-table-column show-overflow-tooltip align="center" prop="urgeStatusName"  label="订单状态">
            <template slot-scope="scope">
              <span>{{scope.row.urgeStatusName}}</span>
            </template>
          </el-table-column>
          <el-table-column show-overflow-tooltip align="center" prop="alreadyRepayAmount"  label="已还金额"></el-table-column>
          <!-- <el-table-column show-overflow-tooltip align="center" prop="mobile"  label="分配状态"></el-table-column> -->
          <el-table-column show-overflow-tooltip align="center" prop="orderNo" width='200px' label="贷款编号"></el-table-column>
          <!-- <el-table-column show-overflow-tooltip align="center" prop="createTime"  label="分配时间"></el-table-column> -->
          <el-table-column show-overflow-tooltip align="center" prop=""  label="操作" fixed='right'>
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
    <el-dialog title="人工分配" width='300px' :visible.sync="dialogFormVisible" :close-on-click-modal='false' :show-close = 'false'>
      <el-form>
        <el-form-item label="贷后专员" :label-width="formLabelWidth">
          <el-select v-model="receiveUserId" placeholder="请选择" clearable>
            <el-option :label="item.loginName" :value="item.id" v-for="item in optionsId" :key="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelDialog">取 消</el-button>
        <el-button type="primary" @click="saveDialog">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import CommonLoanSearch from "@/page/afterLoanManger/CommonLoanSearch";
import Pagination from "@/components/Pagination";
import api from "@/api/index";
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
        disCustHanderBtn: true,
        // 分配时间
        refundDate: false,
        // 订单状态
        LoanOrderState: false,
        receiveUserName: false
      },
      tableData: [],
      multipleSelection: [],
      orderNos: [],
      dialogFormVisible: false,
      formLabelWidth: '70px',
      receiveUserId: '',
      optionsId: []
    };
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      if (this.limitFormData.disCustHanderBtn) {
        // 若有”人工分配“按钮 ，调用获取信审专员列表
        this.LoangetCreditTrialUsersList()
      }
      this.LoanOverdueList()
    },
    disCustHander(val) {
      // 人工分配
      if (this.multipleSelection.length === 0) {
        this.$notify({
          title: '提示',
          message: '请至少选中一条数据',
          type: 'warning',
          duration: 1000
        });
        return
      }
      this.dialogFormVisible = true
      console.log(val);
    },
    searchHand(val) {
      // 查询
      this.formData = Object.assign(this.formData,val)
      this.LoanOverdueList()
      console.log(this.formData, 123);
    },
    selectAllHand(val) {
      // 全选
      this.multipleSelection = val
      console.log(val,'全选')
    },
    handleSelectionChange(val) {
      // 分配
      this.multipleSelection = val;
      console.log(val,'分配')
    },
    cancelDialog() {
      this.receiveUserId = ''
      this.dialogFormVisible = false
    },
    saveDialog() {
      if (!this.receiveUserId) {
        this.$notify({
          title: '提示',
          message: '请选择一个贷后专员',
          type: 'warning',
          duration: 1000
        });
        return
      }      
      let arrId = []
      this.multipleSelection.map((item,index) => {
        arrId.push(item.orderNo)
      })
      this.orderNos = arrId
      // 分配保存
      this.saveLoanDoDistribution()
      console.log(this.orderNos,this.receiveUserId,8888888)
    },
    saveLoanDoDistribution() {
      let pararms = {
        orderNos: this.orderNos,
        receiveUserId: this.receiveUserId
      }
      api.LoanDoDistribution(pararms).then(res => {
        if (res.data.success) {
          this.dialogFormVisible = false
          this.receiveUserId = ''
          this.$notify({
            title: '提示',
            message: res.data.msg,
            type: 'success'
          });
          // 刷新列表
          this.LoanOverdueList()
        } else {
          this.$notify.error({
            title: '提示',
            message: res.data.msg
          });
        }
      })
    },
    LoanOverdueList() {
      let pararms = {
        ...this.formData
      }
      if (this.formData.promiseRepayment) {
        // 应还日期
        pararms.promiseRepaymentStart = this.formData.promiseRepayment[0]
        pararms.promiseRepaymentEnd = this.formData.promiseRepayment[1]
      }
      delete pararms.promiseRepayment
      this.ladingTable = true
      api.LoanOverdueList(pararms).then(res => {
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
    handleSizeChangeHand(val) {
      // 一页显示多少条
      this.currentPage = 1;
      this.formData.pageNo = 1;
      this.formData.pageSize = val;
      this.LoanOverdueList();
    },
    handleCurrentChangeHand(val) {
      // 页码改变时
      this.formData.pageNo = val;
      this.currentPage = val;
      this.LoanOverdueList();
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
      console.log(row)
    },
    LoangetCreditTrialUsersList() {
      // 获取信审专员列表
      api.LoangetCreditTrialUsersList().then(res => {
        if (res.data.success) {
          this.optionsId =  res.data.data
        }        
      })
    }
  },
  filters: {
    getName(val) {
      console.log(val,'filter')
      return ';;;'
    }
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
