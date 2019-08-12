<template>
  <div class="overflowScroll">
    <div style="margin-top:20px;">
      <template>
        <el-tabs v-model="activeName" type="card">
          <el-tab-pane label="基本信息" name="first">
            <div class="inform_wid">
              <div class="titleTop">身份信息</div>
              <div class="info_row">
                <el-row :gutter="24">
                  <el-col :span="8"><span class="info_row_wid">真实姓名：</span>{{baseInfo.name}}</el-col>
                  <el-col :span="8"><span class="info_row_wid">民族：</span>{{baseInfo.nation}}</el-col>
                  <el-col :span="8"><span class="info_row_wid">性别：</span>{{baseInfo.sexDesc}}</el-col>
                </el-row>
              </div>
              <div class="info_row">
                <el-row :gutter="24">
                  <el-col :span="8">
                    <span class="info_row_wid">身份证号码：</span>{{baseInfo.ic}}
                  </el-col>
                  <el-col :span="8"><span class="info_row_wid">出生日期：</span>{{baseInfo.birthday}}</el-col>
                  <el-col :span="8"><span class="info_row_wid">年龄：</span>{{baseInfo.age}}</el-col>
                </el-row>
              </div>
              <el-table v-loading="loadingTable1" :data="tableData1" border style="width: 100%">
                <el-table-column align="center" prop="orderPlatName" width=110 label="平台信息" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="orderUpdateTime" label="更新时间" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="registerTime" label="注册时间" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="orderMobile" label="联系电话" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="orderAddress" label="现居住地址" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="orderQq" label="qq号码" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="orderWechar" label="微信号码" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="repaymentPurpose" label="借款用途证明" show-overflow-tooltip></el-table-column>
              </el-table>
              <div class="titleTop">联系人信息</div>
              <el-table v-loading="loadingTable2" :data="tableData2" border style="width: 100%">
                <el-table-column align="center" prop="linkPlatName" width=110 label="平台信息" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="linkRelation" label="联系人关系" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="linkName" label="联系人姓名" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="linkMobile" label="联系人电话号码" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="linkUpdateTime" label="更新时间" show-overflow-tooltip></el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="借款记录" name="secnd">
            <div class="inform_wid">
              <el-table v-loading="loadingTable3" :data="tableData3" border style="width: 100%">
                <el-table-column align="center" prop="platName" width=110 label="平台信息" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="orderNumber" label="订单号" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="applyNum" label="借款期次" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="loanNum" label="放款期次" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="extensionNum" label="展期期次" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="loanMoney" label="借款金额" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="period" label="借款期限" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="createTime" label="借款时间" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="actualMoney" width=110 label="实际到账金额" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="repaymentMoney" width=110 label="实际还款金额" show-overflow-tooltip></el-table-column>
                <el-table-column align="center" prop="orderStatusDesc" label="状态" show-overflow-tooltip></el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
        </el-tabs>
      </template>
    </div>
  </div>
</template>
<script>
import api from "@/api/index.js";
export default {
  data() {
    return {
      activeName: "first",
      loadingTable1:false,
      loadingTable2:false,
      loadingTable3:false,
      baseInfo:'',
      tableData1:[],
      tableData2:[],
      tableData3:[],
      custInfoId: sessionStorage.getItem("custInfoId"),
      mobile: this.$route.query.mobile,
      ic: this.$route.query.ic,
      platCode: this.$route.query.platCode || ''
    };
  },
  mounted() {
    this.customerBaseInfo()
    this.customerOrderList()
  },
  methods: {
    customerBaseInfo(){
      this.loadingTable1 = true
      this.loadingTable2 = true
      let params={
        custInfoId:this.custInfoId,
        platCode:this.platCode
      }
      api.customerBaseInfo(params).then(res => {
        if (res.data.success) {
          this.loadingTable1 = false
          this.loadingTable2 = false
          this.baseInfo = res.data.data
          this.tableData2 = this.baseInfo.linkList
          this.tableData1 = this.baseInfo.orderList
        } else {
          this.$message.error(res.data.msg)
        }
        
      })
    },
    customerOrderList(){
      this.loadingTable3 = true
      let params={
        mobile:this.mobile,
        ic:this.ic,
        platCode:this.platCode
      }
      api.customerOrderList(params).then(res => {
        if (res.data.success) {
          this.loadingTable3 = false
          this.tableData3 = res.data.data
        } else {
          this.$message.error(res.data.msg)
        }
        
      })
    },
  },
  components: {
    
  }
};
</script>
<style lang="less" scoped>
.overflowScroll {
  width: 100%;
  height: 100%;
  overflow-y: scroll
}
// .inform_mar{
//   margin-top: 20px;
// }
.inform_wid{
  width:99%;
  margin-left: 0.5%;
}
.info_row{
  margin-bottom: 10px;
  margin-left: 10px;
  .info_row_wid{
    display:inline-block;
    width:85px;
    
  }
}
.titleTop{
  font-size: 14px;
  padding-left: 5px;
  border-left: 2px solid #098FFF;
  margin-top: 20px;
  margin-bottom:20px;
  color:#098FFF;
}
</style>