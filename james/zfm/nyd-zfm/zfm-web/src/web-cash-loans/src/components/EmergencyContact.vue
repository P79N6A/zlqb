<template>
  <div class="query-orders common-query">
      
      <div class="common-table">
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
            prop="name"
            label="姓名"
            >
          </el-table-column>
          <el-table-column
            prop="phone"
            label="联系电话"
            align='center'>
          </el-table-column>
          <el-table-column
            align='center'
            prop="updateTime"
            label="更新时间">
          </el-table-column>
          <el-table-column
            align='center'
            prop="remark"
            label="最近一次催收记录">
          </el-table-column>
          <el-table-column
            align='center'
            prop="callTime"
            label="操作">
            <template slot-scope="scope">
                <el-button type="text" @click="addmsg(scope.row)">添加催记</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 添加催记录弹窗 -->
      <el-dialog
        title="添加催收记录"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose">
        <el-form ref="form" :model="form" :rules="rules" label-width="125px">
            <el-form-item label="是否承诺还款：" prop="isPromiseRepay">
                <el-radio-group v-model="form.isPromiseRepay">
                    <el-radio :label="0">是</el-radio>
                    <el-radio :label="1">否</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="备注：" prop="remark">
                    <el-input
                        type="textarea"
                        :rows="2"
                        placeholder="请输入内容"
                        v-model="form.remark">
                        </el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="handleClose">取 消</el-button>
            <el-button type="primary" @click="submitFrom('form')">确 定</el-button>
        </span>
        </el-dialog>
  </div>
</template>

<script type="text/ecmascript-6">
  import Pagination from "@/components/Pagination";
  import api from "@/api/index";
  export default {
    data() {
      return {
        tableData: [],
        dialogVisible: false,
        form:{
            isPromiseRepay:0,//0-是，1-否 ,
            remark:'',
            name:'',
            orderNo:'',
            phone:'',
            userId:'',
            relationMsg:''
        },
        rowdata:[],
        rules:{
            isPromiseRepay:[{required: true, message: '请确定是否承诺还款', trigger: 'blur'}],
            remark:[
                {required: true, message: '请输入催记', trigger: 'blur'},
                { max: 200, message: '最长只能200字符', trigger: 'blur' }
                ]
        }
      }
    },
    mounted(){
      this.List()
    },
    methods: {
      List(){ //emergencyContact\
          api.emergencyContact({
            userId:this.$route.query.userId
          }).then(res => {
              if (res.data.success) {
                this.tableData = res.data.data
              }
          })
      },
    addmsg(e){
        // saveCollection
        console.log(e)
        this.dialogVisible = true
        this.form.name = e.name
        this.form.phone = e.phone
        this.form.relationMsg = e.relationMsg
        this.form.userId =e.userId || '192071100004' // 
        this.form.orderNo = this.$route.query.orderNo || '101564454959714001'
    },
    submitFrom(from){
        this.$refs[from].validate((valid) => {
            if (valid) {
                api.saveCollection(this.form).then(res => {
                        console.log(res)
                        if(res.data.success){
                            console.log(res)
                             this.$refs['form'].resetFields();
                             this.dialogVisible = false
                        }
                })
            } else {
                console.log('error submit!!');
                return false;
            }
            });
    },  
      handleClose() {
          this.$refs['form'].resetFields();
          this.dialogVisible = false
      }
    },
    components: {
      Pagination
    }
  }
</script>

<style lang="less" scoped>
 
</style>