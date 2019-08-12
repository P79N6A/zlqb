<template>
  <div class="form-container">
    <div class="page-bread">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/dictionary/series' }">工厂管理</el-breadcrumb-item>
        <el-breadcrumb-item>{{this.$route.params.id ? '编辑工厂' : '新增工厂'}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="form-content">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px" class="form-wraper">
        <el-form-item label="工厂编号" v-if="this.$route.params.id">
          <span>{{this.$route.params.id}}</span>
        </el-form-item>

        <el-form-item label="所属品牌" prop="typ">
          <el-input v-model="form.rid"></el-input>
        </el-form-item>

        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>

        <el-form-item prop="descr" label="备注">
          <el-input type="textarea" v-model="form.descr"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('form')">立即创建</el-button>
          <el-button @click="resetForm('form')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  import Dialog from "../../../resources/js/dialog";
  import ElFormItem from "../../../../node_modules/element-ui/packages/form/src/form-item.vue";
  /*import {regionData} from 'element-china-area-data'*/

  export default {
    components: {ElFormItem},
    data() {
      var validateTel = function (rule, value, callback) {
        if (value !== '') {
          if (/^((\+86)|(86))?(13)\d{9}$/.test(value) || /^0\d{2,3}-?\d{7,8}$/.test(value)) {
            callback();
          }
          callback(new Error('请输入正确的联系电话'));
        }
      };
      return {
        /*options: regionData,*/
        selectedOptions: [
          "210000",
          "210100",
          "210102"
        ],
        form: {
          id: '',
          // 关联用户id
          rid: '',
          // 公司名称
          name: '',
          // 手机
          cellphone: '',
          // 电话
          telephone: '',
          // 邮箱
          email: '',
          // 法人
          legalName: '',
          // 营业执照号
          licence: '',
          // 公司性质或类型（个人0/股份1/外企3）
          typ: '',
          province: '',
          city: '',
          district: '',
          address: '',
          descr: '',
        },
        rules: {
          typ: [
            {required: false, message: '请选择客户类型', trigger: 'change'}
          ],
          name: [
            {required: false, message: '请输入名称', trigger: 'blur'},
            {min: 6, max: 30, message: '长度在 6 到 30 个字符', trigger: 'blur'}
          ],
          contacts: [
            {required: true, message: '请输入联系人', trigger: 'blur'},
            {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
          ],
          tel: [
            {required: true, message: '请输入联系电话', trigger: 'blur'},
            {validator: validateTel, trigger: 'change'}
          ],
          email: [
            {type: 'email', message: '请输入正确的邮箱', trigger: 'blur,change'}
          ],
          province: [
            {required: true, message: '请选择省份', trigger: 'change'}
          ],
          address: [
            {required: true, message: '请输入详细地址', trigger: 'blur'},
            {min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur,change'}
          ],
          desc: [
            {min: 0, max: 200, message: '长度在 0 到 200 个字符', trigger: 'change'}
          ]
        }
      };


    },
    activated: function () {
      console.log(this.$route.params)
    },
    deactivated: function () {
      this.$refs['form'].resetFields();
    },
    mounted() {
      var factory = sessionStorage.getItem("_factory");
      if (factory) {
        var form = this.$data.form = JSON.parse(factory);
        /*this.selectedOptions[0] = form.province;
        this.selectedOptions[1] = form.city;
        this.selectedOptions[2] = form.district;*/
        console.log(this.selectedOptions);
        /*this.$data.selectedOptions = [
          "210000",
          "210100",
          "210102"
        ]*/
      }
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            var params = this.$data.form;
            params.province = this.selectedOptions[0];
            params.city = this.selectedOptions[1];
            params.district = this.selectedOptions[2];

            Utils.ajax(Api.dictionary.factory.create, params, (success) => {
              //Dialog.alert();
            });
          } else {
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      handleChange(value) {
        this.$data.form.province = value[0];
        this.$data.form.city = value[1];
        this.$data.form.district = value[2];
      }
    }
  }
</script>

<style lang="less">
  .form-container {
    height: auto;
    overflow: hidden;

    .form-content {
      height: auto;
      overflow: hidden;
      padding: 15px;
      background: #fff;
      .form-wraper {
        width: 500px;
        .line {
          text-align: center;
        }
      }
    }
  }
</style>
