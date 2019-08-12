<template>
  <div class="query-orders common-query">
      
      <div class="common-table">
        <p>近一个月通话记录</p>
        <el-table
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column
            align='center'
            prop="mobile"
            label="号码"
            >
          </el-table-column>
          <el-table-column
            prop="name"
            label="姓名"
            align='center'>
          </el-table-column>
          <el-table-column
            align='center'
            prop="callStaus"
            label="通话类型">
          </el-table-column>
          <el-table-column
            align='center'
            prop="callTime"
            label="通话时长(秒)">
          </el-table-column>
          <!-- <el-table-column
            align='center'
            prop="address"
            label="最近一次通话时间">
          </el-table-column> -->
        </el-table>
        <div class="pagination_wrap com_cen">
          <Pagination
            :currentPage="currentPage"
            :total="total"
            :myPageSizes="pageSize"
            @handleSizeChange="handleSizeChange"
            @handleCurrentChange="handleCurrentChange"
          ></Pagination>
        </div>
      </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import Pagination from "@/components/Pagination";
  import api from "@/api/index";
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
      List(){ //contactsList\
      debugger
          api.callRecord({
            
            mobile:this.$route.query.mobile
          }).then(res => {
              if (res.data.success) {
                this.tableData = res.data.data
                this.total = res.data.data.total
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

<style lang="scss" scoped>
 
</style>