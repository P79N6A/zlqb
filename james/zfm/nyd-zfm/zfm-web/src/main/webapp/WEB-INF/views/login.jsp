<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>登录</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/common/css/index.css">
   <!-- 引入样式 -->
  <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
</head>                           
<body>
  <div id="app">
    <div class="login_page fillcontain">
      <!-- <div class="bg_img">
          <img src="../assets/img/icon/login_back.jpg" alt="">
      </div> -->
      <!-- <transition name="form-fade" mode="in-out"> -->
        <section class="form_contianer">
          <div class="manage_tip">
            <p>助乐管理系统</p>
          </div>
          <el-form :model="loginForm" :rules="rules" ref="loginForm">
            <div class="margin_bot22">管理员登录</div>
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" placeholder="用户名"   minlength="1" maxlength="20">
                <i slot="suffix" class="icon-conmon"></i>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input type="password" placeholder="密码" v-model="loginForm.password" auto-complete="off" @keyup.native.enter="submitForm('loginForm')">
                <i slot="suffix" class="icon-conmon icon-password"></i>
              </el-input>
            </el-form-item>
            <el-form-item>
              <div class="submit_btn">
                <el-button
                  size="medium"
                  type="primary"
                  @click.enter="submitForm('loginForm')"
                  :loading="loading"
                >登 录</el-button>
              </div>
            </el-form-item>
          </el-form>
        </section>
      <!-- </transition> -->
    </div>
  </div>
</body>
<script src="https://cdn.jsdelivr.net/npm/vue@2.5.17/dist/vue.js"></script>
<script src="http://code.jquery.com/jquery-2.1.1.min.js"></script>
<!-- 引入组件库 -->
<script src="https://unpkg.com/element-ui/lib/index.js"></script>  
<script src="${pageContext.request.contextPath}/common/js/common.js"></script>
<script src="${pageContext.request.contextPath}/common/js/login.js"></script>

</html>