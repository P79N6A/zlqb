<template>
  <div class="form-container">
    <div class="page-bread">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/dictionary/brand' }">工厂管理</el-breadcrumb-item>
        <el-breadcrumb-item>{{this.$route.params.id ? '编辑工厂' : '新增工厂'}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="form-content">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px" class="form-wraper">
        <el-form-item label="仓库编号" v-if="this.$route.params.id">
          <span>{{this.$route.params.id}}</span>
        </el-form-item>

        <el-form-item label="所属公司" prop="rid">
          <el-input v-model="form.rid"></el-input>
        </el-form-item>

        <el-form-item label="品牌名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>

        <el-form-item label="品牌logo" prop="logo">
          <el-input v-model="form.logo"></el-input>
        </el-form-item>

        <el-form-item label="品牌链接" prop="url">
          <el-input v-model="form.url"></el-input>
        </el-form-item>

        <el-form-item label="品牌权重" prop="weight">
          <el-input v-model="form.weight"></el-input>
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
          rid: '',
          name: '',
          logo: '',
          url: '',
          weight: '',
          descr: '',
        },
        rules: {

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
            Utils.ajax(Api.dictionary.brand.create, params, (success) => {
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
