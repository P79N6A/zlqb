<template>
  <div class="infoTab">
        <div class="title">
          <span class="titname">联系人信息</span>
        </div>
       <el-table
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column
            align='center'
            prop="contactRelation"
            label="联系人关系"
            >
          </el-table-column>
          <el-table-column
            prop="contactName"
            label="联系人姓名"
            align='center'>
          </el-table-column>
          <el-table-column
            prop="contactPhone"
            label="联系人电话号码"
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
      this.contactsInfo()
    },
    methods: {
      contactsInfo(){
        api.contactsInfo({
          userId:this.$route.query.userId,
          // orderNo:this.$route.query.orderNo
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