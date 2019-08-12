import axios from 'axios'
/*
* @type : get/post ， 默认post,
* @success : 请求成功callback
* @error : 请求失败callback
var param = {
	username : 'wer',
}
//Form Data格式
util.ajax(apiConfig.test.save,param, 'Form Data',(success)=>{
	Dialog.alert( success );
})
//JSON格式
util.ajax(apiConfig.test.save,param,(success)=>{
	Dialog.alert( success );
})
*/
function Ajax() {};
//Ajax.prototype.param = {};
Ajax.prototype.exec = function (success, error) {
  //vm.$store.state.stateObject.fullscreenLoading = true;
  var wait = this.wait || false; //是否是多个请求
  var type = (this.type || 'post').toLowerCase(); //get/post ， 默认post,
  var requestType = this.requestType || 'application/json'; //浏览器的原生 form 表单(默认提交方式)
  var url = this.url || '';
  var params = this.param || {};
  params.token = sessionStorage.getItem('token');

  //浏览器的原生 form 表单(默认提交方式:Form Data:'application/x-www-form-urlencoded')
  if( requestType === 'Form Data'){//请求数据方式(Form Data)
    var qs = require('qs');
    params = qs.stringify(params);
    axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
  }else if( requestType === 'application/json'){//请求数据方式(application/json)
    axios.defaults.headers.post['Content-Type'] = 'application/json';
  }

  axios[ type ]( url , params)
    .then(function ( ret ) {
      if( ret.statusText === 'OK'){
        success && success( ret.data );
        // if( ret.data.status === 'success' || ret.data.status === '1'){
        // 	success && success( ret.data );
        // }else{
        // 	error && error( ret.data.msg || ret.data.error);
        // }
      }
      //vm.$store.state.stateObject.fullscreenLoading = false;
    }).catch(function ( ret ) {
    error && error( ret.message );
    //vm.$store.state.stateObject.fullscreenLoading = false;
  });
};
var Inherit = {};
Inherit.Class = function( k ){
  k.prototype = Object.assign( k.prototype , Ajax.prototype );
  return k;
}

export default Ajax;
