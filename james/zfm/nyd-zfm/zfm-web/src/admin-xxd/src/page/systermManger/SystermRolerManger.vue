<template>
  <div class="systerm_user_manger">
    <div class="com_pad10_20 user_manger_search com_1px">
      <el-form class="form_set_bot">
        <el-form-item label="角色名：" prop="name">
          <el-input class="wid200" placeholder="请输入角色名" v-model="search" clearable></el-input>
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
      <el-table v-loading="loadingTable" :data="tableData" border style="width: 100%">
        <el-table-column align="center" type="index" width="50" label="序号" ></el-table-column>
        <!-- <el-table-column type="selection" width="55"></el-table-column> -->
        <el-table-column align="center" prop="name" label="角色名" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" prop="createTime" label="创建时间" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" prop="updateTime" label="更新时间" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" prop="address" label="操作">
          <template slot-scope="scope">
            <el-button type="text" :disabled ='scope.row.defaultFlag == 1' size="small" @click="addInformain('modify',scope.row)">修改</el-button>
            <el-button type="text" size="small" @click="menuSet(scope.row)">菜单配置</el-button>
            <el-button :disabled ='scope.row.defaultFlag == 1' type="text" size="small" @click="sysRemoveUser(scope.row)">删除</el-button>
            <!-- <el-button v-else type="text" size="small" @click="sysRemoveUser(scope.row)">删除</el-button> -->
            <!--  -->
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
        <el-form-item label="角色名：" :label-width="formLabelWidth" prop="name">
          <el-input v-model="ruleForm.name" auto-complete="off" placeholder="请输入角色名" minlength="1" maxlength="20"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button  size="medium" @click="resetForm('ruleForm')">取 消</el-button>
        <el-button :loading='loadingbtn'  size="medium" type="primary" @click="submitForm('ruleForm')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="角色菜单配置"
      :visible.sync="centerDialogVisible"
      :close-on-click-modal="false"
      width="600px"
    >
      <div>
        <Tree :treeData="treeData" :checkedArr="checkedArr" ref="tree" :defaultProps="defaultProps"></Tree>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="medium" @click="celFn">取 消</el-button>
        <el-button type="primary" size="medium" :loading = 'loadingTree' @click="saveTree">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
// import {login, getAdminInfo} from '@/api/getData'
// import { mapActions, mapState } from "vuex"
import HeadTop from "@/components/HeadTop";
import Pagination from "@/components/Pagination";
import Tree from "@/components/Tree";
import {mapActions} from "vuex"
import api from "@/api/index";
export default {
  data() {
    return {
      treeData: [],
      checkedArr: [],
      defaultProps: {
        children: "childrenModule",
        label: "name"
      },
      search: "",
      tableData: [],
      alertTitle: "",
      currentPage: 1,
      total: 0,
      pageNo: 1,
      pageSize: 10,
      title: "新增角色",
      dialogFormVisible: false,
      centerDialogVisible: false,
      showCancel: false,
      ruleForm: {
        name: ""
      },
      formLabelWidth: "80px",
      rules: {
        name: [{ required: true, message: "请输入角色名", trigger: "blur" },
        {
            min: 1,
            max: 20,
            message: "请输入1~20个字符",
            trigger: "blur"
          }]
      },
      type: "",
      loadingTable: false,
      rowId: '',
      loadingbtn: false,
      loadingTree: false
    };
  },
  mounted() {
    this.sysRolelist();
  },
  computed: {},
  methods: {
    saveTree() { // 菜单配置保存
      // this.centerDialogVisible = false
      let arrId = []
      arrId = this.$refs.tree.$refs.tree.getCheckedKeys()
      let pararms = {
        pageNo: 1,
        pageSize: 10000,
        sysModuleIds: arrId.join(",") || '',
        id: this.rowId
      };
      this.loadingTree = true
      api.sysUpdateRole(pararms).then(res => {
        setTimeout(() => {
          this.loadingTree = false
        },1500)
        this.centerDialogVisible = false;
        this.sysRolelist();
        this.updateRolerMenuList({"loginUserName": JSON.parse(localStorage.getItem("user_data")).loginName})
        if (res.data.success) {
           this.$notify({
            title: "提示",
            type: 'success',
            message: res.data.msg,
            duration: 1500
          });
        } else {
          this.$notify({
            title: "提示",
            type: 'error',
            message: res.data.msg,
            duration: 1500
          });
        }
      });
    },
    menuSet(row) {
      //菜单配置
      this.sysGetModuleListByRoleId(row.id);
    },
    sysGetModuleListByRoleId(id) {
      this.rowId = id
      api.sysGetModuleListByRoleId({ roleId: id }).then(res => {
        this.centerDialogVisible = true;
        
        if (res.data.success) {
          this.treeData = res.data.data.moduleList;
          this.checkedArr = res.data.data.moduleIds;
        } else {
          this.$notify({
            title: "提示",
            message: res.data.msg,
            duration: 1500
          });
        }
      });
    },
    searchFn() {
       this.pageNo = 1
       this.pageSize = 10
      this.currentPage = 1
      this.sysRolelist();
    },
    sysRolelist() {
      this.loadingTable = true;
      let pararms = {
        pageNo: this.pageNo,
        pageSize: this.pageSize,
        name: this.search
      };
      api.sysRolelist(pararms).then(res => {
        this.loadingTable = false;
        if (res.data.success && res.data.data != null) {
          this.tableData = res.data.data;
          this.total = res.data.total;
        } else {
          this.$notify({
            title: "提示",
            message: res.data.msg,
            duration: 1500
          });
        }
      });
    },
    sysAddRole() {
      // 新增角色保存
      let pararms = {
        pageNo: 1,
        pageSize: 10000,
        name: this.ruleForm.name
      };
      this.loadingbtn = true
      api.sysAddRole(pararms).then(res => {
        setTimeout(() => {
            this.loadingbtn = false
          },400)
        if (res.data.success) {
          this.dialogFormVisible = false;
          this.$refs["ruleForm"].resetFields();
          this.sysRolelist();
        } else {
          this.$notify({
            title: "提示",
            message: res.data.msg,
            duration: 400
          });
        }
        
      });
    },
    sysUpdateRole() {
      // 修改角色保存
      let pararms = {
        pageNo: 1,
        pageSize: 10000,
        name: this.ruleForm.name,
        id: this.ruleForm.id
      };
       this.loadingbtn = true
      //  debugger
      api.sysUpdateRole(pararms).then(res => {
        
        this.sysRolelist();
        setTimeout(() => {
          this.loadingbtn = false
        },400)
        if (res.data.success) {
          this.dialogFormVisible = false;
        } else {
          this.$notify({
            title: "提示",
            message: res.data.msg,
            duration: 1500
          });
        }
      });
    },
    sysRemoveRole(id) {
      api.sysRemoveRole({ roleId: id }).then(res => {
        this.sysRolelist();
        this.$notify({
          title: "提示",
          message: res.data.msg,
          duration: 1500
        });
      });
    },
    celFn() {
      // 取消
      this.centerDialogVisible = false;
      this.$refs.tree.$refs.tree.setCheckedKeys([]);
    },
    sysRemoveUser(row) {
      // 删除
      this.$confirm("确定要删除角色吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        closeOnClickModal: false
      })
        .then(() => {
          this.sysRemoveRole(row.id);
        })
        .catch(() => {});
    },
    addInformain(type, row) {
      this.dialogFormVisible = true;
      this.type = type;
      if (type == "modify") {
        // 修改
        this.title = "修改角色";
        this.ruleForm = {
          name: row.name,
          id: row.id
        };
        return;
      }
      if (type == "add") {
        // 新增
        this.title = "新增角色";
        this.ruleForm = {
          name: "",
          id: ""
        };
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.type == "add") {
            this.sysAddRole();
          }
          if (this.type == "modify") {
            this.sysUpdateRole();
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.dialogFormVisible = false;
      this.ruleForm = {
        name: ""
      };
      this.$refs[formName].resetFields();
    },
    handleCheckedCitiesChange(value) {
      console.log(value, 7878);
    },
    handleSizeChange(val) {
      // 一页显示多少条
      this.currentPage = 1;
      this.pageNo = 1;
      this.pageSize = val;
      this.sysRolelist();
    },
    handleCurrentChange(val) {
      // 页码改变时
      this.pageNo = val;
      this.currentPage = val;
      this.sysRolelist();
    },
    ...mapActions({
      updateRolerMenuList:'UPDATE_ROLE_MENU_LIST'
    })
  },
  watch: {},
  components: {
    Pagination,
    Tree
  }
};
</script>

<style lang="scss" scoped>
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
