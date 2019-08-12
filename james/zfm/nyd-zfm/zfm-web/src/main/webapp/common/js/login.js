var vm = new Vue({
    el: '#app',
    data: {
        basePath: '',
        loginForm: {
            username: '',
            password: ''
        },
        loading: false,
        rules: {
            username: [{
                    required: true,
                    message: "请输入用户名",
                    trigger: "blur"
                },
                {
                    min: 1,
                    max: 20,
                    message: '长度在 1 到 20 个字符',
                    trigger: 'blur'
                }
            ],
            password: [{
                    required: true,
                    message: "请输入密码",
                    trigger: "blur"
                },
                {
                    min: 6,
                    max: 20,
                    message: '长度在 6 到 20 个字符',
                    trigger: 'blur'
                }
            ]
        },
    },
    mounted() {
        this.basePath = common.basePath()
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate(valid => {
                if (valid) {
                    this.loading = true;
                    this.loginFn();
                } else {
                    return false;
                }
            });
        },
        loginFn() {
            var _this = this
            var pararms = {
                loginName: this.loginForm.username,
                password: this.loginForm.password
            };
            $.ajax({
                type: 'post',
                url: _this.basePath + "/api/sys/user/login",
                data: pararms,
                success: function (data) {
                   
					setTimeout(() => {
						 _this.loading = false;
					  },1500)
                    if (data.success) {
                    	window.location.href=_this.basePath+"/#/index"; 
                    } else {
                    	_this.$notify.error({
                            title: "提示",
                            message :data.msg,
							duration: 1500,
                        });
                    }
                }
            });
        }
    }
})