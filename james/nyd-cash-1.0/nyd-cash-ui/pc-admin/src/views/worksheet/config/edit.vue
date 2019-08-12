<template>
  <div class="form-container">
    <div class="page-bread">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/production/config' }">生产配置</el-breadcrumb-item>
        <el-breadcrumb-item>{{this.$route.params.id ? '编辑配置' : '新增配置'}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="form-content">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px" class="form-wraper">
        <el-form-item label="工厂编号" v-if="this.$route.params.id">
          <span>{{this.$route.params.id}}</span>
        </el-form-item>
        <el-form-item label="省份" prop="province">
          <el-input v-model="form.province"></el-input>
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-input v-model="form.city"></el-input>
        </el-form-item>
        <el-form-item label="区域代码" prop="areaCode">
          <el-input v-model="form.areaCode"></el-input>
        </el-form-item>



        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand"></el-input>
        </el-form-item>
        <el-form-item label="系列" prop="serial">
          <el-input v-model="form.serial"></el-input>
        </el-form-item>
        <el-form-item label="型号" prop="model">
          <el-input v-model="form.model"></el-input>
        </el-form-item>
        <el-form-item label="生产日期" prop="ptime">
          <el-date-picker v-model="form.ptime" type="date" placeholder="选择日期"/>
        </el-form-item>
        <el-form-item label="保修日期" prop="gtime">
          <el-date-picker v-model="form.gtime" type="date" placeholder="选择日期"/>
        </el-form-item>
        <el-form-item label="生产数量" prop="count">
          <el-input v-model="form.count"></el-input>
        </el-form-item>
        <el-form-item label="生产批次" prop="batch">
          <el-input v-model="form.batch"></el-input>
        </el-form-item>
        <el-form-item label="存放仓库" prop="warehouse">
          <el-input v-model="form.warehouse"></el-input>
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
          province: '',
          city: '',
          areaCode: '',
          brand: '',
          series: '',
          model: '',
          ptime: '',
          gtime: '',
          count: '',
          batch: '',
          warehouse: '',
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
        console.log(this.selectedOptions);
      }
    },
    methods: {
      submitForm(formName) {
        var me = this.$data;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            var params = me.form;
            Utils.ajax(Api.production.save, params, (success) => {

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
