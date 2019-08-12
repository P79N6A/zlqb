<template>
  <div class="query-orders common-query">
      
      <div class="common-table">
        <el-table
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column
            align='center'
            label="姓名"
            >
            <template  slot-scope="scope">
               {{scope.row.name}}({{scope.row.relation}})
            </template>
          </el-table-column>
          <el-table-column
            prop="tel"
            label="号码"
            align='center'>
          </el-table-column>
          <el-table-column
            align='center'
            prop="counts"
            label="通话次数">
          </el-table-column>
          <el-table-column
            align='center'
            prop="totalTime"
            label="通话总时长(秒)">
          </el-table-column>
          <el-table-column
            align='center'
            prop="createTime"
            label="最近一次通话时间">
          </el-table-column>
        </el-table>
        <div class="pagination_wrap com_cen">
          <!-- <Pagination
            :currentPage="currentPage"
            :total="total"
            :myPageSizes="pageSize"
            @handleSizeChange="handleSizeChange"
            @handleCurrentChange="handleCurrentChange"
          ></Pagination> -->
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[10, 30, 50, 999999]"
            :page-size="10"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </div>
      </div>
  </div>
</template>

<script type="text/ecmascript-6">
import api from "@/api/index";
  import Pagination from "@/components/Pagination";
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
      List(){ //callList
         api.callList({
           page:this.pageNo,
           size:this.pageSize,
           userId:this.$route.query.userId
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