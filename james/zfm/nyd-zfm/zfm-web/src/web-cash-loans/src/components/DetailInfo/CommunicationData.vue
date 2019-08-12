<template>
  <div class="infoTab">
        <div class="title">
          <span class="titname">通讯数据</span>
        </div>
        <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
          <el-tab-pane label="手机通话记录" name="first">
             
          </el-tab-pane>
          <el-tab-pane label="手机通讯录" name="second">

          </el-tab-pane>
          <el-tab-pane label="手机短信记录" name="third">

          </el-tab-pane>
          <el-tab-pane label="运营商通话记录" name="fourth">

          </el-tab-pane>
        </el-tabs>
   </div>
</template>
<script type="text/ecmascript-6">
import Pagination from "@/components/Pagination";
  export default {
    data() {
      return {
        tableData:[],
      }
    }
  }
</script>
<style lang="less" scoped>
 
</style>