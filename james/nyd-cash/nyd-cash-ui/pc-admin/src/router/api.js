var domain = "http://127.0.0.1:8080";

let api = {
  //ajax:domain+ Ajax,
  test: {
    save: domain + "/userstest/save",
    saveJson: domain + "/userstest/saveJson",
  },
  production: {
    config: domain + '/production/config',
    list: domain + '/production/list',
    save: domain + '/production/save',
    delete: domain + '/production/delete',
  },
  product: {
    config: domain + '/product/config',
    list: domain + '/product/list',
    save: domain + '/product/save',
    delete: domain + '/product/delete',
  },
  user: {
    add: domain + '/user/add',
    del: domain + '/user/del',
    upd: domain + '/user/upd',
    list: domain + '/user/list',
    query: domain + '/user/query',

    login: domain + "/user/login",
    logout: domain + "/user/logout",
    menu: domain + "/user/menu",
    userLogin: domain + "/note/user/userLogin",
    // 登录
    login: domain + '/auth/login',
    // 验证session是否有效
    validSession: domain + '/auth/session',
  },
  bill: {
    // 查询对账单列表
    list: domain + '/bill/list',
    // 确认账单
    confirmOrder: domain + '/bill/confirmBills',
    // 账单导入
    importOrder: domain + '/bill/importBill',
    // 处理账单
    handleOrder: domain + '/bill/handleBill',
  },
  order: {
    // 查询账单列表
    orderList: domain + '/order/list',
    // 查询处理状态
    queryStatus: domain + '/order/queryTaskStatus',
    // 查询已处理的条数
    completeNum: domain + '/order/queryCompleteNumber',
    // 改变订单审核状态
    updateOrderStatus: domain + '/order/updateCheckStatus',
    // 获取拒绝
    getRefusalCase: domain + '/order/refusalCause',
    // 全部确认被初步审核的单子
    confirmPlotOrder: domain + '/order/confirmAllPrePlotOrders',
    // 查询对账单列表
    list: domain + '/order/orders'
  },
  role: {
    add: domain + '/role/add',
    del: domain + '/role/del',
    upd: domain + '/role/upd',
    list: domain + '/role/list',
    query: domain + '/role/query',
    selectRole: domain + '/role/selectRole',
    listResourcesByRoleId: domain + '/role/listResourcesByRoleId'
  },
  resources: {
    add: domain + '/resources/add',
    del: domain + '/resources/del',
    upd: domain + '/resources/upd',
    list: domain + '/resources/list',
    query: domain + '/resources/query',
    listTree: domain + '/resources/listTree'
  },
  auth: {
    add: domain + '/auth/add',
    del: domain + '/auth/del',
    upd: domain + '/auth/upd',
    list: domain + '/auth/list',
    query: domain + '/auth/query'
  },
  account: {
    add: domain + '/account/add',
    del: domain + '/account/del',
    upd: domain + '/account/upd',
    list: domain + '/account/list',
    paging: domain + '/account/paging',
    query: domain + '/account/query'
  },
  dictionary: {
    factory: {
      save: domain + '/company/save',
      delete: domain + '/company/delete',
      list: domain + '/company/list',
      options: domain + '/company/options',
    },
    brand: {
      save: domain + '/brand/save',
      delete: domain + '/brand/delete',
      list: domain + '/brand/list',
      options: domain + '/brand/options',
    },
    series: {
      save: domain + '/series/save',
      delete: domain + '/series/delete',
      list: domain + '/series/list',
      options: domain + '/series/options',
    },
    model: {
      save: domain + '/model/save',
      delete: domain + '/model/delete',
      list: domain + '/model/list',
      options: domain + '/model/options',
    },
    brand: {
      save: domain + '/brand/save',
      delete: domain + '/brand/delete',
      list: domain + '/brand/list',
      options: domain + '/brand/options',
    }
  }
}


/*
var ss = {
  test:["save","saveJson"],
  user:["add","del","upd","list","query"],
  bill:["add","del","upd","list","query"],
  order:{
    sub:["add","del","upd","list","query"]

  },
}
var _api= {}
$.each(ss,function (key,val) {
  _api[key] = {};
  val.forEach(function (index) {
    _api[key][index] = domain+"/" + key + "/" + index;
  })
})
console.log(_api);
*/

// 每个人心里都有一个忘不掉的人
// 听着一首歌，会想起一个人
//var API = {};
/*$.each(api,function (key,val) {
  //API.key = key;
  console.log(key);
  console.log(val);
  $.each(val,function (k,v) {
    val[k] = domain+v;
    console.log(val[k]);
  })
});*/

/*var API = {};
$.each(api,function (key,val) {
  API[key] ={};
  $.each(val,function (k,v) {
    API[key][k] = domain+v;
  })
});
console.log(API);*/
export default api;
