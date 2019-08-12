<template>
  <div class="infoTab">
        <div class="title">
          <span class="titname">协议信息</span>
        </div>
       <el-table
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column
            align='center'
            prop="contratName"
            label="协议名称"
            >
          </el-table-column>
          <el-table-column
            align='center'
            label="操作">
            <template slot-scope="scope">
              <el-button type='text' @click="look(scope.row)">预览</el-button>
            </template>
          </el-table-column>
        </el-table>
   </div>
</template>
<script>
import api from "@/api/index";
export default {
  props:[],
  data() {
    return {
      tableData:[],
    }
  },
  mounted(){
    this.contrat()
  },
  methods: {
    contrat(){
      api.contrat({
        orderNo:this.$route.query.orderNo,
        userId:this.$route.query.userId
      }).then(res => {
          if (res.data.success) {
            this.tableData = res.data.data
          }
      })
    },
    look(a){
      if(a.url){
        window.open(a.url)
      }
    }
  },
}
</script>
<style lang="less" scoped>
 
</style>