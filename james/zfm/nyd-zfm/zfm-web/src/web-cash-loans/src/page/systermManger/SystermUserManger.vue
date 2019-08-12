<template>
  <div class="systerm_user_manger">
    <div class="com_pad10_20 user_manger_search com_1px">
      <el-form class="form_set_bot">
        <el-form-item label="用户名：" prop="name">
          <el-input class="wid200" placeholder="请输入用户名" v-model="search" clearable></el-input>
          <el-button type="primary" class="marrt20" size="medium" @click="searchFn">查询</el-button>
          <el-button
            type="primary"
            class="marrt20"
            size="medium"
            @click="addInformain('add')"
          >新增</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table_list">
      <el-table :data="tableData" border style="width: 100%" v-loading="loadingTable">
        <el-table-column align="center" type="index" width="50" label = '序号'></el-table-column>
        <!-- <el-table-column type="selection" width="55"></el-table-column> -->
        <el-table-column align="center" prop="loginName" label="用户名" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" prop="roleName" label="角色" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" prop="companyName" label="催收公司" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" prop="status" label="状态">
          <template slot-scope="scope">
            <span>{{scope.row.status == 1 ? '启用': "禁用"}}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="createTime" label="创建时间" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" prop="address" label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="addInformain('modify',scope.row)">修改</el-button>
            <!-- <el-button type="text" size="small" @click="sysRemoveUser(scope.row)">删除</el-button> -->
            <el-button type="text" size="small" @click="sysResetPas(scope.row)">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination_wrap com_cen">
      <Pagination
        :currentPage="currentPage"
        :total="total"
        :myPageSizes="pageSize"
        @handleSizeChange="handleSizeChange"
        @handleCurrentChange="handleCurrentChange"
      ></Pagination>
    </div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      :show-close="false"
      width="600px"
    >
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm">
        <el-form-item label="用户名：" :label-width="formLabelWidth" prop="name">
          <el-input v-model="ruleForm.name" auto-complete="off" placeholder="请输入用户名" maxlength='20' minlength="1"></el-input>
        </el-form-item>
        <el-form-item label="密码：" :label-width="formLabelWidth" prop="password" v-if = 'showPassword'>
          <el-input
            v-model="ruleForm.password"
            type="password"
            auto-complete="off"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item label="角色：" :label-width="formLabelWidth" prop="type">
          <el-select
            style="width: 465px;"
            @change="handleCheckedCitiesChange"
            v-model="ruleForm.type"
            multiple
            value-key = 'key'
            placeholder="请选择"
          >
            <el-option v-for="item in roles" :key="item.key" :label="item.value" :value="item.key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="催收公司：" :label-width="formLabelWidth" prop="company" v-if = "showCompany">
          <el-select
            style="width: 465px;"
            v-model="ruleForm.company"
            placeholder="请选择"
          >
            <el-option v-for="item in arrCompany" :key = "item.userId" :label = "item.userName" :value = "item.userId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态：" :label-width="formLabelWidth" prop="radioState">
          <el-radio-group v-model="ruleForm.radioState">
            <el-radio v-model="ruleForm.radioState" label="1">启用</el-radio>
            <el-radio v-model="ruleForm.radioState" label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click="resetForm('ruleForm')">取 消</el-button>
        <el-button
          size="medium"
          type="primary"
          @click="submitForm('ruleForm')"
          :loading="loadingSut"
        >确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// import {login, getAdminInfo} from '@/api/getData'
import HeadTop from "@/components/HeadTop";
import Pagination from "@/components/Pagination";
import api from "@/api/index";
export default {
  data() {
    return {
      search: "",
      tableData: [],
      alertTitle: "",
      currentPage: 1,
      total: 0,
      pageNo: 1,
      pageSize: 10,
      title: "新增用户",
      dialogFormVisible: false,
      loadingTable: false,
      loadingSut: false,
      //   centerDialogVisible: false,
      //   showCancel: false,
      ruleForm: {
        name: "",
        password: "",
        type: [],
        company: '',
        radioState: "1"
      },
      formLabelWidth: "95px",
      roles: [],
      multipleSelection: [],
      showPassword: true,
      rules: {
        name: [
          { required: true, message: "请输入1~20个字符", trigger: "blur" },
          {
            min: 1,
            max: 20,
            message: "请输入1~20个字符",
            trigger: "blur"
          }
        ],
        password: [
          {
            pattern: /^[a-zA-Z0-9]{6,16}$/,
            message: "只能输入6~16位数字、字母"
          },
          { required: true, message: "请输入6~16个字符", trigger: "blur" },
          {
            min: 6,
            max: 16,
            message: "请输入6~16个字符",
            trigger: "blur"
          }
        ],
        type: [
          {
            type: "array",
            required: true,
            message: "请至少选择一个角色",
            trigger: "change"
          }
        ],
        company: [
          {
            // type: "array",
            required: true,
            message: "请选择催收公司",
            trigger: "change"
          }
        ],
        radioState: [
          { required: true, message: "请选择状态", trigger: "change" }
        ]
      },
      type: '',
      rowId: '',
      showCompany: false,
      arrCompany: [],
      flagNotify: null
    };
  },
  created() {
    this.sysRolelist()
  },
  mounted() {
    this.sysUserlist();
  },
  computed: {},
  methods: {
    sysRolelist() {
      let par = {
        status: 1,
        pageNo0: 0,
        pageSize: 10000
      }
      this.roles = []
      api.sysRolelist(par).then(res => {
        if (res.data.success) {
          for(let i in res.data.data) {
            let obj = {}
            obj.key = res.data.data[i].id
            obj.value = res.data.data[i].name
            this.roles.push(obj)
          }
        } else {
          this.roles = []
        }
      })

    },
    searchFn() {
      this.pageNo = 1
      this.pageSize = 10
      this.currentPage = 1
      this.sysUserlist();
    },
    sysUserlist() {
      this.loadingTable = true;
      let pararms = {
        pageNo: this.pageNo,
        pageSize: this.pageSize,
        loginName: this.search
      };
      api.sysUserlist(pararms).then(res => {
        this.loadingTable = false;
        if (res.data.success && res.data.data != null) {
          this.tableData = res.data.data;
          this.total = res.data.total;
          console.log(this.tableData, this.total);
        } else {
          this.$notify({
            title: "提示",
            message: res.data.msg,
            duration: 1500
          });
        }
      });
    },  
    addInformain(type, row) {
      this.dialogFormVisible = true;
      this.type = type;
     
      if (type == "add") {
        this.title = '新增用户'
        this.showPassword = true
        return;
      }
      if (type == "modify") {
        this.rowId = row.id
        // 修改
        this.title = '修改用户'
        this.showPassword = false
        let flag = false
        let _index = 0
        if (row.roleName == undefined || row.roleName == null ) {
          row.roleName = ''
        }
        _index = row.roleName.indexOf('催收专员')
        flag = _index > -1 ? true : false
        if (_index > -1) {
          this.sysQueryUserByRole(3)
        }
        this.showCompany = flag 
        this.ruleForm = {
          name: row.loginName,
          type: row.roleIdList || [],
          radioState: row.status + '', 
          company: row.companyIdList         
        };
        // if (!flag) {
        //   this.ruleForm.company = null
        // }
      console.log(this.ruleForm , "===");
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          //   this.$refs["ruleForm"].resetFields();
          if (this.type == 'add') {
             // 新增保存
            this.sysUserAdd();
          }
          if (this.type == 'modify') {
            // 修改保存
            this.sysUserUpdateUser();
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    sysUserUpdateUser() {// 修改保存
      // let roleIdList = []
      // for (let i in this.ruleForm.type) {
      //   roleIdList.push(this.ruleForm.type[i].key);
      // }
      let pararms = {
        roleIdList: this.ruleForm.type,
        loginName: this.ruleForm.name,
        id: this.rowId,
        status: this.ruleForm.radioState,
        companyIdList: this.ruleForm.company
      }
      this.loadingSut = true;
      let _this = this
      api.sysUserUpdateUser(pararms).then(res => {
        
         setTimeout(() => {
            this.loadingSut = false
          },1500)
        if (res.data.success) {
          this.sysUserlist();
          this.dialogFormVisible = false
          this.$refs["ruleForm"].resetFields();
          this.ruleForm = {
            name: "",
            password: "",
            type: [],
            radioState: "1",
            company: ''
          };
          this.showCompany = false
          this.flagNotify = this.$notify({
            title: "提示",
            message: res.data.msg,
            duration: 1500,
            type:"success",
          });
        } else {
          this.flagNotify = this.$notify({
            title: "提示",
            message: res.data.msg,
            duration: 1500,
            type:"warning",
          });
        }
        
      })
    },
    sysUserAdd() {
      // 新增保存
      // let roleIdList = [];
      // for (let i in this.ruleForm.type) {
      //   roleIdList.push(this.ruleForm.type[i].key);
      // }
      // let companyIdList = [];
      // for (let i in this.ruleForm.type) {
      //   companyIdList.push(this.ruleForm.company[i].key);
      // }
      let pararms = {
        roleIdList: this.ruleForm.type,
        loginName: this.ruleForm.name,
        password: this.ruleForm.password,
        status: Number(this.ruleForm.radioState),
        companyIdList: this.ruleForm.company
      };
      this.loadingSut = true;
      api.sysUserAdd(pararms).then(res => {
        setTimeout(() => {
           this.loadingSut = false;
          },1500)
        // this.$refs["ruleForm"].resetFields();
        this.sysUserlist();
         
        if (res.data.success) {
          this.$refs["ruleForm"].resetFields();
          this.ruleForm = {
            name: "",
            password: "",
            type: [],
            radioState: "1",
            company: ''
          };
          this.dialogFormVisible = false;
          this.showCompany = false
          this.$notify({
            title: "提示",
            message: res.data.msg,
            duration: 1500,
            type:"success",
          });
        } else {
          this.$notify({
            title: "提示",
            message: res.data.msg,
            duration: 1500,
             type:"error",
          });
        }
        
        
      });
    },
    sysResetPas(row) {// 重置密码
      this.$confirm("密码将重置为“888888”，为保证系统安全，请提醒用户修改密码。", "重置密码", {
        cancelButtonText: "取消",
        confirmButtonText: "确定",
        type: "warning",
        closeOnClickModal: false
      })
        .then(() => {
           api.resetPassWord({userId:row.id}).then(res => {
            this.sysUserlist();
            this.$notify({
                title: "提示",
                message: res.data.msg,
                duration: 1500
            });
          })
        })
        .catch(() => {});
       
    },
    // sysRemoveUser(row) {// 删除
    //   this.$confirm("确定要删除用户吗？", "删除确认", {
    //     cancelButtonText: "取消",
    //     confirmButtonText: "确定",
    //     type: "warning",
    //     closeOnClickModal: false
    //   })
    //     .then(() => {
    //        api.sysRemoveUser({userId:row.id}).then(res => {
    //         this.sysUserlist();
    //         this.$notify({
    //             title: "提示",
    //             message: res.data.msg,
    //             duration: 1500
    //         });
    //       })
    //     })
    //     .catch(() => {});
       
    // },
    resetForm(formName) {
      
      this.dialogFormVisible = false;
      this.showCompany = false
      this.ruleForm = {
        name: "",
        password: "",
        type: [],
        radioState: "1",
        company: ''
      };
      this.$refs[formName].resetFields();
    },
    handleCheckedCitiesChange(value) {
      // let flag = value.find((element, index) => {   
      //   return element.value == '催收专员'
      // })
      let valueObj = null
      this.roles.map((element, index) => {
         value.find((item, ind) => {
          if ((element.key == item) && (element.value == '催收专员')) {
            valueObj = element
          }
        })
      })

      if (valueObj != null && valueObj.value == '催收专员') {
        this.showCompany = true
        this.sysQueryUserByRole(3)
        // this.sysQueryUserByRole(valueObj.key)
      } else {
         this.showCompany = false
         this.arrCompany = []
         this.ruleForm.company = ''
      }
      console.log( valueObj, "======"); 
      // console.log( this.ruleForm.type,value, 7878);     
    },
    sysQueryUserByRole(roleId) {
      let pararms = {
        roleId: roleId
      }
      api.sysQueryUserByRole(pararms).then(res => {
        if (res.data.success && res.data.data != null) {
          this.arrCompany = res.data.data
        }
      })
    },
    handleSizeChange(val) {
      // 一页显示多少条
      this.currentPage = 1;
      this.pageNo = 1;
      this.pageSize = val;
      this.sysUserlist();
      console.log(6666, val);
    },
    handleCurrentChange(val) {
      // 页码改变时
      this.pageNo = val;
      this.currentPage = val;
      this.sysUserlist();
      console.log(1212, val);
    }
  },
  watch: {},
  components: {
    Pagination
  }
};
</script>

<style lang="less" scoped>
.systerm_user_manger {
  .wid200 {
    width: 200px;
  }
  .marrt20 {
    margin-left: 15px;
    margin-right: 0;
  }
  .table_list {
    padding-top: 20px;
  }
}
</style>
