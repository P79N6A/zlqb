<template>
  <div class="systerm_menu_manger">
    <div class="com_pad10_20 menu_manger_search com_1px">
      <el-button
        type="primary"
        class="marrt20"
        size="medium"
        @click="addInformain('add')"
      >新增</el-button>
    </div>
    <div class="table_list">
      <el-table v-loading="loadingTable" :data="tableData" border style="width: 100%">
        <el-table-column align="center" type="index" width="50" label="序号"></el-table-column>
        <el-table-column align="center" prop="name" label="菜单名" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" prop="pid" label="父节点ID" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" prop="url" label="菜单地址" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" prop="sort" label="优先级" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" prop="hasNode" label="具有子菜单" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.hasNode == 1">是</span>
            <span v-if="scope.row.hasNode == 0">否</span>
          </template>
        </el-table-column>
        <el-table-column align="center" prop="iconUrl" label="图标" show-overflow-tooltip></el-table-column>
        <el-table-column align="center" prop="address" label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="addInformain('modify',scope.row)">修改</el-button>
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
        <el-form-item label="菜单名：" :label-width="formLabelWidth" prop="name">
          <el-input v-model="ruleForm.name" auto-complete="off" placeholder="请输入菜单名"></el-input>
        </el-form-item>
        <el-form-item label="拥有子菜单：" :label-width="formLabelWidth" prop="radioState">
          <el-radio-group v-model="ruleForm.radioState" @change = 'changeRadio'>
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="菜单地址："
          :label-width="formLabelWidth"
          prop="menuAdress"
          v-if="ruleForm.radioState == 100  || ruleForm.radioState == '' "
        >
          <el-input v-model="ruleForm.menuAdress" auto-complete="off" placeholder="请输入菜单地址"></el-input>
        </el-form-item>
        <el-form-item label="菜单优先级：" :label-width="formLabelWidth" prop="menuDegree">
          <el-input v-model="ruleForm.menuDegree" auto-complete="off" placeholder="请输入优先级"></el-input>
        </el-form-item>
        <el-form-item label="父节点：" :label-width="formLabelWidth" prop="menuParentNode">
          <!-- @change="handleCheckedCitiesChange" -->
          <el-select clearable v-model = "ruleForm.menuParentNode" placeholder="请选择">
            <el-option v-for="item in menuNode" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="icon图标：" :label-width="formLabelWidth" prop="icon">
          <el-input v-model="ruleForm.icon" auto-complete="off" placeholder="请输入图标"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click="resetForm('ruleForm')">取 消</el-button>
        <el-button size="medium" type="primary" @click="submitForm('ruleForm')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// import {login, getAdminInfo} from '@/api/getData'
import { mapActions } from "vuex"
import HeadTop from "@/components/HeadTop";
import Pagination from "@/components/Pagination";
import api from "@/api/index";
export default {
  mame: "菜单管理",
  data() {
    return {
      tableData: [],
      alertTitle: "",
      currentPage: 1,
      total: 0,
      pageNo: 1,
      pageSize: 10,
      title: "新增菜单",
      dialogFormVisible: false,
      ruleForm: {
        name: "",
        radioState: "",
        menuAdress: "",
        menuDegree: "",
        menuParentNode: '',
        icon: ""
      },
      menuNode: [],
      formLabelWidth: "110px",
      rules: {
        name: [{ required: true, message: "请输入菜单名", trigger: "blur" }],
        radioState: [
          { required: true, message: "请选择拥有子菜单", trigger: "change" }
        ],
        menuAdress: [
          { required: true, message: "请输入菜单地址", trigger: "blur" }
        ],
        menuDegree: [
          { required: true, message: "请输入菜单优先级", trigger: "blur" },
          { pattern: /^\d{0,}$/, message: "只能输入数字" }
        ],
        menuParentNode: [
          {
            required: true,
            message: "请至少选择一个",
            trigger: "change"
          }
        ],
        icon: [{ required: true, message: "请输入图标", trigger: "blur" }]
      },
      type: "",
      loadingTable: false,
      rowId: ''
    };
  },
  mounted() {
    this.sysMenuModuleList();
  },
  created() {
    this.sysMenuGetParentModule();
  },
  methods: {
    sysMenuModuleList() { // 列表
      this.loadingTable = true;
      let pararms = {
        pageNo: this.pageNo,
        pageSize: this.pageSize
      };
      api.sysMenuModuleList(pararms).then(res => {
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
    addInformain(type, row) {
      this.dialogFormVisible = true;
      this.type = type;
      if (type == "modify") {
        // 修改
        this.title = "修改菜单";
        this.rowId = row.id
        this.ruleForm = {
          name: row.name,
          radioState:row.hasNode,
          menuAdress: row.url,
          menuDegree: row.sort,
          menuParentNode: Number(row.pid),
          icon: row.iconUrl
        };
        console.log(this.ruleForm ,"修改")
        return;
      }
      if (type == "add") {
        // 新增
        this.sysMenuGetParentModule();
        this.title = "新增菜单";
        this.ruleForm = {
          name: "",
          radioState: "",
          menuAdress: "",
          menuDegree: "",
          menuParentNode: '',
          icon: ""
        }
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.type == "add") {
            this.sysMenuAddModule()
          }
          if (this.type == "modify") {
            this.sysMenuUpdateModule()
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.dialogFormVisible = false;
      this.$refs[formName].resetFields();
    },
    sysMenuAddModule() {
      // 新增菜单    
      let pararms = {
        name: this.ruleForm.name,
        hasNode: this.ruleForm.radioState,
        url: this.ruleForm.menuAdress,
        sort: this.ruleForm.menuDegree,
        pid: this.ruleForm.menuParentNode,
        iconUrl: this.ruleForm.icon
      }
      api.sysMenuAddModule(pararms).then(res => {
        this.$refs['ruleForm'].resetFields();
        this.dialogFormVisible = false        
        this.sysMenuModuleList();
        this.updateRolerMenuList({"loginUserName": JSON.parse(localStorage.getItem("user_data")).loginName})
        this.$notify({
          title: "提示",
          message: res.data.msg,
          duration: 1500
        });
      })
    },
    sysMenuUpdateModule() {
      // 修改菜单
      let pararms = {
        id: this.rowId,
        name: this.ruleForm.name,
        hasNode: this.ruleForm.radioState,
        url: this.ruleForm.menuAdress,
        sort: this.ruleForm.menuDegree,
        pid: this.ruleForm.menuParentNode,
        iconUrl: this.ruleForm.icon
      }
      api.sysMenuUpdateModule(pararms).then(res => {
        this.dialogFormVisible = false
        this.sysMenuModuleList();
        this.updateRolerMenuList({"loginUserName": JSON.parse(localStorage.getItem("user_data")).loginName})
        this.$notify({
          title: "提示",
          message: res.data.msg,
          duration: 1500
        });
      })
    },
    handleSizeChange(val) {
      // 一页显示多少条
      this.currentPage = 1;
      this.pageNo = 1;
      this.pageSize = val;
      this.sysMenuModuleList();
    },
    handleCurrentChange(val) {
      // 页码改变时
      this.pageNo = val;
      this.currentPage = val;
      this.sysMenuModuleList();
    },
    sysMenuGetParentModule() {
      // 获取父级菜单
      api.sysMenuGetParentModule().then(res => {
        if (res.data.success) {
          this.menuNode = res.data.data;
        } else {
          this.$notify({
            title: "提示",
            message: res.data.msg,
            duration: 1500
          });
        }
      });
    },
    changeRadio() {
      if (this.ruleForm.radioState == 0) {
        this.ruleForm.menuAdress = "";
      }
    },
    ...mapActions({
      updateRolerMenuList:'UPDATE_ROLE_MENU_LIST'
    })
  },
  watch: {
    // "ruleForm.radioState"(val) {
    //   if (val == 0 && this.type != 'add') {
    //     this.ruleForm.menuAdress = "";
    //   }
    //   console.log(val);
    // }
  },
  components: {
    Pagination
  }
};
</script>

<style lang="less" scoped>
.systerm_menu_manger {
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
