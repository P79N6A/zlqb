<template>
  <div class="query-orders common-query">
      
      <div class="common-table">
        <p>近六个月通话记录</p>
        <el-table
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column
            align='center'
            prop="peer_number"
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
            prop="dialName"
            label="通话类型">
          </el-table-column>
          <el-table-column
            align='center'
            prop="duration"
            label="通话时长(秒)">
          </el-table-column>
          <el-table-column
            align='center'
            prop="time"
            label="通话时间">
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
      List(){ //
         api.operatorCallRecord({
            userId:this.$route.query.userId,
            page:this.pageNo,
            size:this.pageSize,
          }).then(res => {
              if (res.data.success) {
                this.tableData = res.data.data
                for(let i in this.tableData){
                  if(this.tableData[i].dial_type == 'DIAL'){
                    this.tableData[i].dialName ='主叫'
                  }else if(this.tableData[i].dial_type == 'DIALED'){
                    this.tableData[i].dialName ='被叫'
                  }
                }
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