<template>
  <div class="query-orders common-query">
      
      <div class="common-table">
        <div class="searchbox">
              <p>关键词：</p>
              <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
              <div style="margin: 15px 0;"></div>
              <el-checkbox-group v-model="searchArry" @change="handleCheckedCitiesChange">
                <el-checkbox v-for="item in keysArry" :label="item" :key="item">{{item}}</el-checkbox>
              </el-checkbox-group>
        </div>
        <el-table
          :data="tableData"
          border
          style="width: 100%">
          <el-table-column
            align='center'
            prop="name"
            label="联系人"
            >
            <template slot-scope="scope">{{scope.row.name | globalFilter}}</template>
          </el-table-column>
          <el-table-column
            prop="callNo"
            label="联系电话"
            align='center'>
          </el-table-column>
          <el-table-column
            align='center'
            prop="content"
            label="内容">
               <template  slot-scope="scope">
                  <div v-html="scope.row.content" >{{scope.row.content}}</div>
              </template>
          </el-table-column>
          <el-table-column
            align='center'
            prop="createtime"
            label="创建时间">
          </el-table-column>
        </el-table>
      </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import Pagination from "@/components/Pagination";
  import api from "@/api/index";
  export default {
    data() {
      return {
        checkAll: false,
        isIndeterminate: true,
        searchArry:[],
        keysArry:['欠款','欠债','拖欠','吸毒','贩毒','抽大烟','麻古','麻果','K粉','赌徒','赌输','输完','输光','已逾期','法务部','法催部','戒毒所','撸口子','褥羊毛','过不下去','活不下去','已经逾期','严重逾期','恶意透支','逃避欠款','逃避还款','催收录音',],
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
       handleCheckAllChange(val) {
        this.searchArry = val ? this.keysArry : [];
        this.isIndeterminate = false;
        this.List()
      },
      handleCheckedCitiesChange(value) {
        let checkedCount = value.length;
        this.checkAll = checkedCount === this.keysArry.length;
        this.isIndeterminate = checkedCount > 0 && checkedCount < this.keysArry.length;
        this.List()
      },
      search(a){
        debugger
        if(a.status == false){ //点击之前是灰色按钮，点了之后变高亮
          if(this.searchArry.indexOf(a.value) < 0){
            this.searchArry.push(a.value)
          }
          this.keysArry[a.sort].status = true
          console.log(this.keysArry,this.searchArry)
        }else{
          if(this.searchArry.indexOf(a.value) > -1){
            var Pos = this.searchArry.indexOf(a.value)
            this.searchArry.splice(Pos,1)
            // this.searchArry.remove(a.value)
          }
          this.keysArry[a.sort].status = false
          console.log(this.keysArry,this.searchArry)
        }
      },
      List(){
         api.smsRecording({
           keywordList:this.searchArry,
            userId:this.$route.query.userId
          }).then(res => {
              if (res.data.success) {
                this.tableData = res.data.data
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
 .searchbox{
   border:#ddd solid 1px;
   padding: 20px;
   p{
     margin-bottom: 10px;
   }
   a{
     display: inline-block;
     padding: 8px 20px;
     text-align: center;
     border-radius: 5px;
     cursor: pointer;
     border:#ddd solid 1px;
     margin-left: 20px;
     &.btn{
       background: #409EFF;
       color: #fff;
     }
   }
   .el-checkbox{
     width: 100px;
   }
 }
</style>