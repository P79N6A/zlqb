<template>
  <div class="query-orders common-query">
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
            <span class="common-label-text">订单状态：</span>
            <el-select v-model="formData.orderStatus" placeholder="请选择" clearable>
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            <el-button type="primary" @click="search()">查询</el-button>
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
            prop="orderStatus"
            label="订单状态"
            >
          </el-table-column>
          <el-table-column
            prop="productPeriods"
            label="借款期次"
            align='center'>
          </el-table-column>
          <el-table-column
            align='center'
            prop="custName"
            label="客户姓名">
          </el-table-column>
          <el-table-column
            align='center'
            prop="mobile"
            label="手机号码" width="120">
          </el-table-column>
          <el-table-column
            align='center'
            prop="source"
            label="注册渠道">
          </el-table-column>
          <el-table-column
            align='center'
            prop="createTime"
            label="申请时间" width="180">
          </el-table-column>
          <el-table-column
            align='center'
            prop="procuctName"
            label="放款产品">
             <template slot-scope="scope">助乐钱包</template>
          </el-table-column>
          <el-table-column
            align='center'
            prop="orderNo"
            label="贷款编号" width="180">
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
        <!-- <div class="pagination_wrap com_cen">
          <Pagination
            :currentPage="currentPage"
            :total="total"
            :myPageSizes="pageSize"
            @handleSizeChange="handleSizeChange"
            @handleCurrentChange="handleCurrentChange"
          ></Pagination>
        </div> -->
        <!-- <Drag :style="styleObj">我是可拖拽的</Drag> -->
      </div>
    </el-card>
  </div>
</template>

<script type="text/ecmascript-6">
  import api from "@/api/index.js";
  import Pagination from "@/components/Pagination";
  import Drag from '@/components/DralogComponent/Drag'
  import {orderState} from '@/utils/constOptions'
  console.log(orderState,99999)
  export default {
    data() {
      return {
        styleObj: {
          width: '550px',
        },
        formData: {
          custName: '',
          mobile: '',
          source: '',
          orderStatus:''
        },
         options: orderState,
        tableData: [],
      }
    },
    mounted(){
      // this.orderList();
    },
    methods: {
      // channelList(){
      //   api.channelList({
      //     pageNo:1,
      //     pageNo:9999
      //   }).then(res => {
      //     if (res.data.success) {

      //     }
      //   })
      // },
      lookDetail(row){
        var routeData = this.$router.resolve({
          path: '/custOrderDetail',
          query: {
            orderNo: row.orderNo,
            status:row.orderStatus,
            custName:row.custName,
            userId:row.userId,
            operate:false,
          }
        });
        window.open(routeData.href)
      },
      search(){
        this.orderList();
      },
      orderList(){
        api.getOrderList(this.formData).then(res => {
          this.loadingTable = false;
          if (res.data.success) {
            this.tableData = res.data.data;
            this.total = res.data.total;
          } else {
            this.$message.error(res.data.msg);
          }
        })
      },
    },
    components: {
      Drag,
      Pagination
    }
  }
</script>

<style lang="less" scoped>
 
</style>