<template>
  <div class="header_wrap clearfix">
    <div class="header_con_left" :class="{wid50: flag}">
      {{title}}
      <i class="sys_icon" v-show="flag"></i>
    </div>
    <div class="header_con_rigth clearfix" :class="{marginLeft: flag}">
      <div class="left">
        <i class="header_arrow" @click="toggleMenu" :class="{header_arrow_hide:flag}"></i>
      </div>
      <div class="right">
        <ul class="clearfix">
          <li class="left">欢迎
            <span class="nameClor">{{userName}}</span>
          </li>
          <li class="left" @click="modifyPas">
            <i class="icon-com"></i>修改密码
          </li>
          <li class="left" @click="logoutFn">
            <i class="icon-com icon-logout"></i>退出系统
          </li>
        </ul>
      </div>
    </div>
    <el-dialog
      :title="titlePas"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      width="600px"
    >
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" :label-width="formLabelWidth">
        <el-form-item label="账号">
          <span class="account">{{userName}}</span>
        </el-form-item>
        <el-form-item label="新密码" prop="pass">
          <el-input
            type="password"
            v-model="ruleForm.pass"
            auto-complete="off"
            placeholder="请输入新密码"
          ></el-input>
        </el-form-item>
    
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" type="primary" @click="submitForm('ruleForm')">修 改</el-button>
        <el-button size="medium" @click="resetForm('ruleForm')">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import api from "@/api/index";
import { baseURL } from "@/api/config.js";
export default {
  data() {
    return {
      flag: false,
      dialogFormVisible: false,
      title: "总控管理系统",
      titlePas: "修改密码",
      formLabelWidth: "70px",
      userName: "",
      ruleForm: {
        pass: ""
        // checkPass: ""
      },
      rules: {
        pass: [
          { required: true, message: "请输入新密码", trigger: "blur" },
          {
            min: 6,
            max: 16,
            message: "只能输入为6~16位数字、字母",
            trigger: "blur"
          },
          {
            pattern: /^[a-zA-Z0-9]{6,16}$/,
            message: "只能输入6~16位数字、字母",
            trigger: "blur"
          }
        ]
        // checkPass: [{ validator: validatePass2, trigger: "blur" }]
      },
      data: null
    };
  },
  created() {
    this.data = JSON.parse(localStorage.getItem("user_data"));
    // this.userName = this.data.loginName;
  },
  mounted() {
    this.getUserInfo();
  },
  computed: {},
  methods: {
    getUserInfo() {
      // if(JSON.parse(localStorage.getItem('user_data'))){
      //    this.userName = JSON.parse(localStorage.getItem('user_data')).loginName;
      //   return
      // }
      api.getUserInfo().then(res => {
        if (res.data.success) {
          this.userName = res.data.data.loginName;
          localStorage.setItem("user_data", JSON.stringify(res.data.data));
        } else {
          this.$notify.error({
            title: "提示",
            message: res.data.msg
          });
        }
      });
    },
    toggleMenu() {
      this.flag = !this.flag;
      this.title = this.flag ? "" : "总控管理系统";
      this.$emit("toggleMenu", this.flag);
    },
    modifyPas() {
      this.dialogFormVisible = true;
    },
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.updatePassWord();
        } else {
          // console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.dialogFormVisible = false;
      this.$refs[formName].resetFields();
    },
    logoutFn() {
      this.$confirm("您将退出系统, 是否继续?", "提示", {
        cancelButtonText: "取消",
        confirmButtonText: "确定",
        type: "warning"
      })
        .then(() => {
          this.logout();
        })
        .catch(() => {});
    },
    updatePassWord() {
      //修改密码
      let paramrs = {
        loginName: this.data.loginName,
        password: this.ruleForm.pass
        // oldPassword: this.ruleForm.checkPass,
      };
      api.updatePassWord(paramrs).then(res => {
        this.ruleForm = {
          pass: ""
          // checkPass: ""
        };
        this.dialogFormVisible = false;
        if (res.data.success) {
          this.$confirm("修改密码成功！", "提示", {
            confirmButtonText: "确定",
            showCancelButton: false,
            type: "success"
          })
            .then(() => {})
            .catch(() => {});
        } else {
          this.$message({
            type: "success",
            message: res.data.msg
          });
        }
      });
    },
    logout() {
      localStorage.removeItem("user_data");
      localStorage.removeItem("menuList");
      // 退出
      window.location.href = baseURL + "/sys/logout";
      // api.logout().then(res => {
      //   if (res.data.success) {
      //     this.$router.push({
      //       path: "/login"
      //     });
      //   } else {
      //     this.$message({
      //       type: "success",
      //       message: res.data.msg
      //     });
      //   }
      // });
    }
  }
};
</script>

<style lang="scss" scoped>
.header_wrap {
  height: 64px;
  line-height: 64px;
  .header_con_left {
    width: 220px;
    float: left;
    color: #fff;
    text-align: center;
    font-weight: 700;
    background-color: #002140;
    transition: width 0.4s;
    font-size: 18px;
  }
  .wid50 {
    width: 54px;
    transition: width 0.4s;
  }
  .header_con_rigth {
    padding: 0 20px 0 20px;
    margin-left: 220px;
    height: 64px;
    line-height: 64px;
    background-color: #fff;
    color: #4d4d4d;
    font-size: 13px;
    transition: margin-left 0.4s;
    ul {
      li {
        padding: 0 10px;
        &:hover {
          background-color: rgba(0, 0, 0, 0.1);
          cursor: pointer;
        }
        span.nameClor {
          display: inline-block;
          margin: 0 5px;
          color: #308bff;
        }
      }
    }
  }
  .marginLeft {
    margin-left: 50px;
    transition: margin-left 0.4s;
  }
  .header_arrow {
    display: inline-block;
    width: 20px;
    height: 20px;
    background: url(../assets/img/icon/icon_arrow.png) no-repeat center;
    background-size: 20px 20px;
    margin-top: -2px;
    vertical-align: middle;
    cursor: pointer;
  }
  .sys_icon {
    display: inline-block;
    width: 48px;
    height: 48px;
    background: url(../assets/img/sys_icon.png) no-repeat center;
    background-size: 48px 48px;
    vertical-align: middle;
  }
  .header_arrow_hide {
    background: url(../assets/img/icon/icon_head_hide.png) no-repeat center;
    background-size: 20px 20px;
  }
  .right {
    .icon-com {
      display: inline-block;
      width: 14px;
      height: 15px;
      background: url(../assets/img/icon/icon_modeify.png) no-repeat center;
      background-size: 14px 15px;
      vertical-align: middle;
      padding-right: 10px;
      margin-top: -2px;
    }
    .icon-logout {
      background: url(../assets/img/icon/icon_logout.png) no-repeat center;
      background-size: 14px 15px;
    }
  }
}
.account {
  padding: 0px 8px;
  background-color: #00a65a;
  color: #fff;
  border-radius: 5px;
}
</style>
