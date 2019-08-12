var common = {
    basePath: function() {
        var localObj = window.location;
        var contextPath = localObj.pathname.split("/")[1];
        var basePath = localObj.protocol + "//" + localObj.host + "/"+ contextPath;
        return basePath
    }
}
var vm = new Vue({
    el: '#app',
    data(){
        return{
           basePath:'',
           bankArr:[],
           bankData:[],
           bankName:'',
           bankCode:[],
           custName:'',
           custIc :'',
           cardNumber:'',
           custMobile:'', 
           param:'',  
           verifyCode:'',
           readyCust:false,
           readyIc:false,
           goSend:false,
           imgSrc:'',
           custInfoId:'',
           orderId:'',
           code:'700001',
        }
    },
    mounted() {
        this.basePath = common.basePath();
        this.bankName = $('#bankName').val();
    	this.custName = $('#custName').val()
        this.custIc =$('#custIc').val()
        this.custMobile =$('#custMobile').val()
        this.custInfoId= $('#custInfoId').val()
        this.orderId= $('#orderId').val()
        this.type= $('#type').val()
        this.cardNumber= $('#cardNumber').val()
        if(this.custName){
          this.readyCust = true
        }
        if(this.custIc){
          this.readyIc = true
        }
        this.getBankData();
        this.imgSrc=this.basePath + '/web/mobile/validcode/createcaptcha?'+Math.random();
    },
    methods: {
        // 获取银行信息信息
        getBankData(){
          let _this = this
           $.ajax({
                type: 'post',
                url: _this.basePath + "/web/account/queryDictionaryDetail",
                data: {
                  code:_this.code
                },
                success: function (data) {
                    if (data.code == '1') {
                       _this.bankData = data.data;
                        _this.handelData()
                    } else {
                        _this.$createToast({
                          type: 'error',
                          txt: data.msg,
                          time: 1500
                        }).show()
                    }
                }
            });
        },
        //银行数据处理
        handelData(){
           for (var i = 0; i < this.bankData.length; i++) {
             this.bankArr.push({text:this.bankData[i].name,value:this.bankData[i].price})
           }
        },
        selectBank(){
            this.$createPicker({
              title: '请选择银行',
              data: [this.bankArr],
              onSelect: this.selectHandle,
              onCancel: this.cancelHandle
            }).show()
        },
        selectHandle(selectedVal,selectedIndex,selectedText){
        
            this.bankName = selectedText;
            this.bankCode = selectedVal; 
        },
        cancelHandle(){

        },
        //图形验证码
        changeImg(){
          this.imgSrc=this.basePath + '/web/mobile/validcode/createcaptcha?'+Math.random();
        },

        //发送验证码
        sendCode(){
            //开户银行验证
            if(!this.bankName){
               this.$createToast({
                  type: 'error',
                  txt: '请选择开户银行',
                  time: 1500
                }).show()
               return false
            }
           //客户姓名验证
            if(!this.custName ){
                this.$createToast({
                   type: 'error',
                   txt: '请填写用户姓名',
                   time: 1500
                 }).show()
                return false
             }
           if(!(/^.{1,20}$/.test(this.custName))){
               this.$createToast({
                  type: 'error',
                  txt: '姓名错误',
                  time: 1500
                }).show()
               return false
            }
           //身份证验证
           if(!this.custIc){
               this.$createToast({
                  type: 'error',
                  txt: '提示请输入身份证号码',
                  time: 1500
                }).show()
               return false
            }
           if(!(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(this.custIc))){
               this.$createToast({
                  type: 'error',
                  txt: '身份证号码不正确',
                  time: 1500
                }).show()
               return false
            }
           //银行卡验证
           if(!this.cardNumber){
               this.$createToast({
                  type: 'error',
                  txt: '请输入银行卡号',
                  time: 1500
                }).show()
               return false
            }
           if( !(/^[0-9]{16,19}$/.test(this.cardNumber))){
               this.$createToast({
                  type: 'error',
                  txt: '银行卡号不正确',
                  time: 1500
                }).show()
               return false
            }
           //手机号验证
           if(!this.custMobile){
               this.$createToast({
                  type: 'error',
                  txt: '请输入手机号码',
                  time: 1500
                }).show()
               return false
            }
           if(!this.custMobile || !(/^1[345678]\d{9}$/.test(this.custMobile))){
               this.$createToast({
                  type: 'error',
                  txt: '手机号码不正确',
                  time: 1500
                }).show()
               return false
            }
            //图形验证码校验
           if(!this.param){
               this.$createToast({
                  type: 'error',
                  txt: '请输入图形验证码',
                  time: 1500
                }).show()
               return false
            }
            //校验验证码
           $('.megCode').attr('disabled',"disabled");
            let _this=this
            $.ajax({
                type: 'post',
                url: _this.basePath +"/web/mobile/validcode/chkCaptcha",
                data: {
                  param:_this.param
                },
                success: function (data) {
                    if (data.code == '1') {
                        _this.appPreCard()
                    } else {
                    	$('.megCode').removeAttr('disabled');
                        _this.$createToast({
                          type: 'error',
                          txt: data.msg,
                          time: 1500
                        }).show()
                    }
                }
            });
        },
        //正式发送短信验证码
        appPreCard(){
          let params={
             bankCode:this.bankCode[0],
             custName:this.custName,
             custIc :this.custIc,
             cardNumber:this.cardNumber,
             custMobile:this.custMobile,
             verifyCode:this.verifyCode,
             custInfoId: this.custInfoId,
             orderId: this.orderId
          }
          let _this=this
          $.ajax({
              type: 'post',
              url: this.basePath +"/web/account/appPreCard",
              data: params,
              success: function (data) {
                  if (data.success) {
                    _this.countDown(89,".megCode");
                  } else if(data.code == 'B0057') {
                      _this.$createDialog({
                        type: 'confirm',
                        content: data.msg,
                        confirmBtn: {
                          text: '确定',
                          active: true,
                          disabled: false,
                          href: 'javascript:;'
                        },
                        cancelBtn: {
                          text: '取消',
                          active: false,
                          disabled: false,
                          href: 'javascript:;'
                        },
                        onConfirm: () => {
                           _this.updateCardStatus()
                        },
                        onCancel: () => {
                          $('.megCode').removeAttr('disabled');
                        }
                      }).show()
                  }else if(data.code == 'B0058') {
                	  $('.megCode').removeAttr('disabled');
                      _this.$createToast({
                        type: 'error',
                        txt: data.msg,
                        time: 1500
                      }).show()
                  }else{
                	  $('.megCode').removeAttr('disabled');
                	  _this.$createToast({
                          type: 'error',
                          txt: data.msg,
                          time: 1500
                        }).show()
                  }
              }
          });
        },
        //验证码倒计时
        countDown(time,selector) {  
          time = time || 89;
          var countNum = setInterval(function () {
            $(selector).text(time+'s');
            time--;
            if ( time <= 0 ){
              time = 89;
              clearInterval(countNum);
              $(selector).text("重新获取").removeAttr('disabled');
            }
          }, 1000);
        },

        //已开户处理 修改卡为有效
        updateCardStatus(){
          let params={
             bankCode:this.bankCode[0],
             custName:this.custName,
             custIc :this.custIc,
             cardNumber:this.cardNumber,
             custMobile:this.custMobile,
             verifyCode:this.verifyCode,
             custInfoId: this.custInfoId,
             orderId: this.orderId
          }
          let _this=this
          $.ajax({
              type: 'post',
              url: this.basePath +"/web/account/updateCardStatus",
              data: params,
              success: function (data) {
                  if (data.success) {
//                	  _this.$createDialog({
//                          type: 'alert',
//                          content: data.msg,
//                          confirmBtn: {
//                            text: '确定',
//                            active: true,
//                            disabled: false,
//                            href: 'javascript:;'
//                          },
//                          onConfirm: () => {
                        	  window.location.href= data.data;
//                          },
//                	  }).show() 	
                  } else {
                    _this.$createToast({
                      type: 'error',
                      txt: data.msg,
                      time: 2000
                    }).show()
                  }
              }
          });

        },
        //保存确定签约开户
        submit(){
            //开户银行验证
            if(!this.bankName){
               this.$createToast({
                  type: 'error',
                  txt: '请选择开户银行',
                  time: 1500
                }).show()
               return false
            }
           //客户姓名验证
            if(!this.custName){
                this.$createToast({
                   type: 'error',
                   txt: '请输入用户姓名',
                   time: 1500
                 }).show()
                return false
             }
           if(!this.custName || !(/^.{1,20}$/.test(this.custName))){
               this.$createToast({
                  type: 'error',
                  txt: '姓名错误',
                  time: 1500
                }).show()
               return false
            }
           //身份证验证
           if(!this.custIc ){
               this.$createToast({
                  type: 'error',
                  txt: '请输入身份证号码',
                  time: 1500
                }).show()
               return false
            }
           if(!this.custIc || !(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(this.custIc))){
               this.$createToast({
                  type: 'error',
                  txt: '身份证号码不正确',
                  time: 1500
                }).show()
               return false
            }
           //银行卡验证
           if(!this.cardNumber ){
               this.$createToast({
                  type: 'error',
                  txt: '请输入银行卡号',
                  time: 1500
                }).show()
               return false
            }
           if(!this.cardNumber || !(/^[0-9]{16,19}$/.test(this.cardNumber))){
               this.$createToast({
                  type: 'error',
                  txt: '银行卡号不正确',
                  time: 1500
                }).show()
               return false
            }
           //手机号验证
           if(!this.custMobile){
               this.$createToast({
                  type: 'error',
                  txt: '请输入手机号',
                  time: 1500
                }).show()
               return false
            }
           if(!this.custMobile || !(/^1[345678]\d{9}$/.test(this.custMobile))){
               this.$createToast({
                  type: 'error',
                  txt: '手机号码不正确',
                  time: 1500
                }).show()
               return false
            }
            //图形验证码校验
           if(!this.param){
               this.$createToast({
                  type: 'error',
                  txt: '请输入图形验证码',
                  time: 1500
                }).show()
               return false
            }
            //短信验证码校验
            if(!this.verifyCode){
               this.$createToast({
                  type: 'error',
                  txt: '请输入短信验证码',
                  time: 1500
                }).show()
               return false
            }
            let loading = this.$createToast({
                time: 0,
                txt: 'Loading...',
                mask: true
              });
            loading.show()
            let params={
               bankCode:this.bankCode[0],
               custName:this.custName,
               custIc :this.custIc,
               cardNumber:this.cardNumber,
               custMobile:this.custMobile,
               verifyCode:this.verifyCode,
               custInfoId: this.custInfoId,
               orderId: this.orderId,
               bankName: this.bankName[0],
            }
            let _this=this;
            $.ajax({
                type: 'post',
                url: this.basePath +"/web/account/confirmSign",
                data:params,
                success: function (data) {
                    if (data.success) {
                    	 loading.hide()
                    	 _this.$createDialog({
                             type: 'alert',
                             content: '授权成功',
                             confirmBtn: {
                               text: '确定',
                               active: true,
                               disabled: false,
                               href: 'javascript:;'
                             },
                             
                             onConfirm: () => {
                           	  window.location.href= data.data;
                             },
                   	  }).show()
                    } else {
                    	 loading.hide()
                    	_this.$createToast({
                          type: 'error',
                          txt: data.msg,
                          time: 2000
                        }).show()
                    }
                }
            });
        }
    }
})