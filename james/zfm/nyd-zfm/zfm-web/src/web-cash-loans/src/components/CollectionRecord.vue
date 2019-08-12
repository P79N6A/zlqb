<template>
  <div class="query-orders common-query"> 
      <div class="common-table">
       <div class="common-reset-inptu">
        <div class="common-display">
          <span>催收时间：</span>
           <el-date-picker
            size='small'
            v-model="time"
            type="daterange"
            range-separator="至"
            value-format="yyyy-MM-dd"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
            </el-date-picker>
        </div>
        <div class="common-display">
          <span class="common-label-text">联系电话：</span>
          <el-input size='small' clearable placeholder="请输入" v-model="phone"></el-input>
        </div>  
        <el-button type="primary" @click="toSearch">查询</el-button>     
      </div>
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
            prop="orderNo"
            label="订单号"
            >
            </el-table-column>
          <el-table-column
            align='center'
            prop="createTime"
            label="催收时间"
            width="180"
            >
          </el-table-column>
          <el-table-column
            prop="relationMsg"
            label="催收对象"
            align='center'>
          </el-table-column>
          <el-table-column
            align='center'
            prop="name"
            label="姓名"> 
          </el-table-column>
          <el-table-column
            align='center'
            prop="phone"
            label="联系电话">
          </el-table-column>
          <el-table-column
            align='center'
            prop="sysUserName"
            label="催收人员">
          </el-table-column>
          <el-table-column
            align='center'
            prop="isPromiseRepay"
            label="是否承诺还款">
             <template slot-scope="scope">
                    {{ scope.row.isPromiseRepay == '1' ? '否' : '是'}}
            </template>
          </el-table-column>
          <el-table-column
            align='center'
            prop="remark"
            label="催收备注">
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
  export default {
    data() {
      return {
        tableData: [],
        phone:'',
        time:'',
        currentPage: 1,
        total: 0,
        pageNo: 1,
        pageSize: 10,
        beginTime:'',
        endTime:'', 
      }
    },
    mounted(){
      this.List()
    },
    methods: {
      List(){
            //   把时间做分配
            if(this.time){
                this.beginTime = this.time[0]
                this.endTime= this.time[1]
            }else{
                this.beginTime = ''
                this.endTime= ''
            }
          api.collectionList({
            orderNo:this.$route.query.orderNo || '101564210518468001',
            pageNo:this.pageNo,
            pageSize:this.pageSize,
            beginTime:this.beginTime,
            endTime:this.endTime, 
            phone:this.phone,
          }).then(res => {
              if (res.data.success) {
                this.tableData = res.data.data
                 this.total = res.data.total
              }
          })
      },
      toSearch(){
          console.log(this.time)
          this.List()
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