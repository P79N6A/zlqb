<template>
  <div class="infoTab">
        <div class="title">
          <span class="titname">提醒记录</span>
        </div>
       <el-table
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column
            align='center'
            prop="createTime"
            width="150"
            label="提醒时间">
          </el-table-column>
          <el-table-column
            prop="remindUserLoginName"
            width="150"
            label="提醒人员"
            align='center'>
          </el-table-column>
           <el-table-column
            prop="remindMsg"
            label="提醒内容"
            align='center'>
          </el-table-column>
        </el-table>
   </div>
</template>
<script type="text/ecmascript-6">
import api from "@/api/index";
import Pagination from "@/components/Pagination";
  export default {
    data() {
      return {
        tableData:[],
      }
    },
    mounted(){
      this.queryRemindMsgList()
    },
    methods: {
      queryRemindMsgList(){
        api.queryRemindMsgList({
          orderNo:this.$route.query.orderNo
        }).then(res => {
            if (res.data.success) {
              this.tableData = res.data.data
            }
        })
      },
    },
  }
</script>
<style lang="less" scoped>
 
</style>