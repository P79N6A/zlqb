let menu = {};

/**
 * 首页
 * @type {{name: string, path: string, icon: string}}
 */
menu.home = {
    name: '首页',
    path: '/',
    icon: 'fa fa-tachometer',
};

// menu.permission_manage = {
//   name: '权限管理',
//   icon: 'fa fa-qrcode',
//   children: {}
// };
// let PermissionManage = menu.permission_manage.children;

// PermissionManage.role = {
//   name: '角色管理',
//   path: '/role_manage',
// };

// PermissionManage.permission = {
//   name: '权限列表',
//   path: '/permission_list',
// };

menu.customer = {
    name: '客服管理',
    icon: 'fa fa-qrcode',
    children: {}
}

let CustomerMoudle = menu.customer.children

CustomerMoudle.orders = {
    name: '订单查询',
    path: '/queryOrders',
}


CustomerMoudle.refundProcessing = {
    name: '退款处理',
    path: '/refundProcessing',
}

export default menu;