let cacheData = {}

//通用方法
var UTIL = {
  go (page) {
    window.location.hash = `#/${page}`
  },
  setSession (key, value, expire) {
    if (window.sessionStorage) {
      window.sessionStorage.setItem(key, value)
    } else {
      cacheData[key] = value
    }
  },
  getSession (key) {
    if (window.sessionStorage) {
      return window.sessionStorage.getItem(key)
    } else {
      return cacheData[key]
    }
  },
  removeSession (key) {
    if (window.sessionStorage) {
      window.sessionStorage.removeItem(key)
    } else {
      delete cacheData[key]
    }
  },
  cleanSession () {
    if (window.sessionStorage) {
      window.sessionStorage.clear()
    } else {
      cacheData = {}
    }
  },
  formatDate (d, format) {
    if (!(d instanceof Date)) {
      return '请传入日期类型'
    }
    let year = d.getFullYear()
    let month = d.getMonth() + 1
    let date = d.getDate()
    let hours = d.getHours()
    let minutes = d.getMinutes()
    let seconds = d.getSeconds()
    month = month > 9 ? month : '0' + month
    date = date > 9 ? date : '0' + date
    hours = hours > 9 ? hours : '0' + hours
    minutes = minutes > 9 ? minutes : '0' + minutes
    seconds = seconds > 9 ? seconds : '0' + seconds
    format = format.replace('YYYY', year)
    format = format.replace('MM', month)
    format = format.replace('DD', date)
    format = format.replace('hh', hours)
    format = format.replace('mm', minutes)
    format = format.replace('ss', seconds)
    return format
  },
  merge (target, obj) {
    if (typeof target !== 'object' || typeof obj !== 'object') {
      return target
    }
    for (let attr in obj) {
      if (obj.hasOwnProperty(attr)) {
        target[attr] = obj[attr]
      }
    }
    return target
  },

  /********************************************/
  //判断是否登录
  isLogin :function(){
    if(sessionStorage.getItem('token')){
      vm.$children[0].isLogin = true;
    }else{
      window.location.href="#/login"
    }
  },
  //ajax请求
  ajax : function(url,param,requestType, fn ,fn2){
    var _ajax = new Ajax();
    _ajax.url = url;
    // 增加空值过滤
    var _param = {};
    $.each(param,function(key,val){
      if(val !== '' && val !== "" && val != null){
        _param[key] = val;
      }
    });
    _ajax.param = _param;
    _ajax.requestType = requestType;
    if(Object.prototype.toString.call( requestType ) === "[object Function]"){
      _ajax.requestType = 'application/json';
      fn2 = fn;
      fn = requestType;
    }
    _ajax.exec(function( success ){
      fn && fn( success )
    },function( error ){
      fn2 && fn2( error );
      Dialog.alert(error);
    })
  },
  date_ : {
    //秒转换成日期
    dateTo : function(r) {
      var q,p,o,n,m,l,k,j;
      return r = 1000 * r,
        q = new Date,
        q.setTime(r),
        p = "" + q.getFullYear(),
        o = q.getMonth() + 1,
        n = q.getDate(),
        m = q.getHours(),
        l = q.getMinutes(),
        k = q.getSeconds(),
      p + "-" + (o > 9 ? o : "0" + o) + "-" + (n > 9 ? n : "0" + n) +" "+ (m > 9 ? m : "0" + m) +":"+ (l > 9 ? l : "0" + l) +":"+ (k > 9 ? k : "0" + k)
    },
    //日期转换成秒
    toDate : function (b) {
      var t = new Date(b.replace(/-/g, "/")).getTime();
      return t
    }
  },
  //复制一个新对象
  extendObj : function( p, c) {
    var c = c || {};
    for (var i in p) {
      if(typeof p[i] === 'object') {
        c[i] = Object.prototype.toString.call(p[i]) === "[object Array]" ? [] : {};
        this.extendObj(p[i], c[i]);
      } else {
        c[i] = p[i];
      }
    }
    return c;
  }
}
export default UTIL;
