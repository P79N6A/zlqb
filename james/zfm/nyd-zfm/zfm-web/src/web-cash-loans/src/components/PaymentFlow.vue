<template>
  <div class="query-orders common-query"> 
      <div class="common-table">
        <el-table
          :data="tableData"
          border
          style="width: 100%">
           <el-table-column
            show-overflow-tooltip
             align="center" 
             label="序号" 
             type="index" 
             width="50">
            </el-table-column>
          <el-table-column
            align='center'
            prop="createTime"
            label="交易时间"
            >
          </el-table-column>
          <el-table-column
            prop="repayAmount"
            label="实还金额"
            align='center'>
          </el-table-column>
          <el-table-column
            align='center'
            prop="payType"
            label="交易方式">
            <template slot-scope="scope">
                    {{ scope.row.payType == '1' ? '主动还款' : '代扣' }}
            </template>
          </el-table-column>
          <el-table-column
            align='center'
            prop="remark"
            label="交易发起人">
          </el-table-column>
          <el-table-column
            align='center'
            prop="resultCode"
            label="交易结果">
            <template slot-scope="scope">
                    {{ scope.row.resultCode == '1' ? '成功' : scope.row.resultCode == '2' ? '失败' : '处理中'}}
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination_wrap com_cen">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[10, 30, 50]"
            :page-size="10"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </div>
      </div>


  </div>
</template>

<script type="text/ecmascript-6">
  import Pagination from "@/components/Pagination";
  import api from "@/api/index";
//   还款方式 1 主动还款 2 代扣
//   交易状态 1:成功,2:失败,3:处理中
  export default {
    data() {
      return {
        tableData: [],
        currentPage: 1,
        total: 0,
        pageNo: 1,
        pageSize: 10,
      }
    },
    mounted(){
      this.List()
    },
    methods: {
      List(){ //getBillRepayList\
          api.getBillRepayList({
            orderNo:this.$route.query.orderNo || '101564210518468001',
            pageNo:this.pageNo,
            pageSize:this.pageSize
          }).then(res => {
              if (res.data.success) {
                this.tableData = res.data.data
                 this.total = res.data.total
              }
          })
      },
      handleSizeChange(val) {
        // 一页显示多少条
        this.currentPage = 1;
        this.pageNo = 1;
        this.pageSize = val;
        this.List();
      },
      handleCurrentChange(val) {
        // 页码改变时
        this.pageNo = val;
        this.currentPage = val;
        this.List();
      },
    },
    components: {
      Pagination
    }
  }
</script>

<style lang="less" scoped>
 
</style>