<template>
	<div class="form-container">
		<div class="page-bread">
			<el-breadcrumb separator="/">
			  	<el-breadcrumb-item :to="{ path: '/baseinfo/customer' }">用户管理</el-breadcrumb-item>
			  	<el-breadcrumb-item>{{this.$route.params.id?'编辑用户':'新增用户'}}</el-breadcrumb-item>
			</el-breadcrumb>
		</div>

		<div class="form-content">
			<el-form :model="form" :rules="rules" ref="form" label-width="150px" class="form-wraper">
			  	<el-form-item label="用户编号" v-if="this.$route.params.id">
			    	<span>{{this.$route.params.id}}</span>
			  	</el-form-item>

			  	<el-form-item label="姓名" prop="name">
			      	<el-input v-model="form.name"></el-input>
			  	</el-form-item>
			  	<el-form-item label="性别" prop="sex">
            <el-select v-model="form.sex" placeholder="请选择">
              <el-option v-for="item in options.sex" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
			  	</el-form-item>
			  	<el-form-item label="年龄" prop="age">
			      	<el-input v-model="form.age"></el-input>
			  	</el-form-item>
			  	<el-form-item label="第二昵称" prop="nick">
			      	<el-input v-model="form.nick"></el-input>
			  	</el-form-item>
          <el-form-item label="身份证号" prop="idcard">
            <el-input v-model="form.idcard"></el-input>
          </el-form-item>
			  	<el-form-item label="身份证户籍所在地" prop="birthplace">
			      	<el-input v-model="form.birthplace"></el-input>
			  	</el-form-item>


			  	<el-form-item label="身份证户籍所在地" prop="province">
			      	<el-col :span="7">
				      	<el-select placeholder="选择省份" v-model="form.province">
					      	<el-option label="区域一" value="shanghai"></el-option>
					      	<el-option label="区域二" value="beijing"></el-option>
					    </el-select>
				    </el-col>
				    <el-col class="line" :span="1">-</el-col>
				    <el-col :span="7">
				      	<el-select placeholder="选择城市" v-model="form.city">
					      	<el-option label="区域一" value="shanghai"></el-option>
					      	<el-option label="区域二" value="beijing"></el-option>
					    </el-select>
				    </el-col>
				    <el-col class="line" :span="1">-</el-col>
				    <el-col :span="8">
				      	<el-select placeholder="选择区/县" v-model="form.district">
					      	<el-option label="区域一" value="shanghai"></el-option>
					      	<el-option label="区域二" value="beijing"></el-option>
					    </el-select>
				    </el-col>
			  	</el-form-item>
			  	<el-form-item prop="address">
			      	<el-input v-model="form.address"></el-input>
			  	</el-form-item>
			  	<el-form-item label="备注" prop="desc">
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
  export default {

    data() {
    	var validateTel = function(rule, value, callback){
    		if (value!=='') {
	    		if(/^((\+86)|(86))?(13)\d{9}$/.test(value)||/^0\d{2,3}-?\d{7,8}$/.test(value)){
	    			callback();
	    		}
	    		callback(new Error('请输入正确的联系电话'));
          	}
	     };
      	return {
          options: {
            sex: [{
              value: '0',
              label: '男'
            }, {
              value: '1',
              label: '女'
            }],
            age:[{
              value: '18',
              label: '18'
            },{
              value: '19',
              label: '19'
            },{
              value: '20',
              label: '20'
            },{
              value: '21',
              label: '21'
            }]
          },

          form: {
            id:'',
        		name:"",
            sex:'0',
            age:"",
            nick:"",
            idcard:"",
            birthplace:"",
        		province:"",
        		city:"",
        		district:"",
        		address:"",
        		descr:""
        	},
        	rules: {
        		/*type:[
	          		{ required: true, message: '请选择客户类型', trigger: 'change' }
	          	],
	          	name: [
		            { required: true, message: '请输入名称', trigger: 'blur' },
		            { min: 6, max: 30, message: '长度在 6 到 30 个字符', trigger: 'blur' }
	          	],
	          	contacts: [
		            { required: true, message: '请输入联系人', trigger: 'blur' },
		            { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
	          	],
	          	tel: [
		            { required: true, message: '请输入联系电话', trigger: 'blur' },
		            { validator: validateTel, trigger: 'change' }
	          	],
	          	email:[
      				{ type: 'email', message: '请输入正确的邮箱', trigger: 'blur,change' }
	          	],
	          	province:[
	          		{ required: true, message: '请选择省份', trigger: 'change' }
	          	],
	          	address:[
	          		{ required: true, message: '请输入详细地址', trigger: 'blur' },
	          		{ min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur,change' }
	          	],
	          	desc:[
	          		{ min: 0, max: 200, message: '长度在 0 到 200 个字符', trigger: 'change' }
	          	]*/
        	}
      };
    },
    activated:function(){
    	console.log(this.$route.params)
    },
    deactivated:function(){
    	this.$refs['form'].resetFields();
    },
    methods: {
      	submitForm(formName) {
	        this.$refs[formName].validate((valid) => {
	          	if (valid) {
	            	alert('submit!');
	          	} else {
		            // console.log('error submit!!');
		            // this.$router.back(-1)
		            return false;
	          	}
	        });
      	},
      	resetForm(formName) {
        	this.$refs[formName].resetFields();
      	}
    }
  }
</script>

<style lang="less">
	.form-container{
		height: auto;
		overflow: hidden;

		.form-content{
			height: auto;
			overflow: hidden;
			padding: 15px;
			background: #fff;
			.form-wraper{
				width: 500px;
				.line{
					text-align: center;
				}
			}
		}
	}
</style>
