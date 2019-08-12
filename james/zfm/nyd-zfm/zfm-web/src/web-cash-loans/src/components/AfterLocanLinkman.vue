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
          <el-table-column
            prop="contactPhone"
            label="操作"
            align='center'>
              <template slot-scope="scope">
                <el-button type="text" @click="addmsg(scope.row)">添加催记</el-button>
            </template>
          </el-table-column>
        </el-table>
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
import api from "@/api/index";
import Pagination from "@/components/Pagination";
  export default {
    data() {
      return {
        tableData:[],
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
  }
</script>
<style lang="less" scoped>
 
</style>