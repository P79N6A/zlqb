import Axios from './config-interceptors.js'
const queryString = require('query-string');
import { baseURL } from './config.js';
export default {

    // 登录
    getUserInfo(data, params) { // 获取客户信息
        return Axios.post("/api/sys/user/getUserInfo", queryString.stringify(data), { params: params });
    },
    // 退出
    logout(data, params) {
        return Axios.post("/api/sys/user/logout", queryString.stringify(data), { params: params });
    },
    // 修改密码
    updatePassWord(data, params) {
        return Axios.post("/api/sys/user/updatePassWord", queryString.stringify(data), { params: params });
    },
    // 系统用户管理
    sysUserlist(data, params) {
        return Axios.post("/api/sys/user/userlist", queryString.stringify(data), { params: params });
    },
    //系统用户管理:新增 
    sysUserAdd(data, params) {
        return Axios.post("/api/sys/user/userAdd", queryString.stringify(data), { params: params });
    },
    //系统用户管理:修改
    sysUserUpdateUser(data, params) {
        return Axios.post("/api/sys/user/updateUser", queryString.stringify(data), { params: params });
    },
    //系统用户管理:删除 
    sysRemoveUser(data, params) {
        return Axios.post("/api/sys/user/removeUser", queryString.stringify(data), { params: params });
    },
    //系统用户管理: 重置密码
    resetPassWord(data, params) {
        return Axios.post("/api/sys/user/resetPassWord", queryString.stringify(data), { params: params });
    },
    // 系统管理： 角色管理
    sysRolelist(data, params) {
        return Axios.post("/api/sys/role/rolelist", queryString.stringify(data), { params: params });
    },
    // 系统管理： 角色管理 ==》菜单树
    sysGetModuleListByRoleId(data, params) {
        return Axios.post("/api/sys/module/getModuleListByRoleId", queryString.stringify(data), { params: params });
    },
    // 系统管理： 新增角色
    sysAddRole(data, params) {
        return Axios.post("/api/sys/role/addRole", queryString.stringify(data), { params: params });
    },
    // 系统管理： 删除角色
    sysRemoveRole(data, params) {
        return Axios.post("/api/sys/role/removeRole", queryString.stringify(data), { params: params });
    },
    // 系统管理： 修改角色
    sysUpdateRole(data, params) {
        return Axios.post("/api/sys/role/updateRole", queryString.stringify(data), { params: params });
    },
    // 系统管理：菜单管理 获取父级菜单
    sysMenuGetParentModule(data, params) {
        return Axios.post("/api/sys/module/getParentModule", queryString.stringify(data), { params: params });
    },
    // 系统管理：菜单管理列表
    sysMenuModuleList(data, params) {
        return Axios.post("/api/sys/module/moduleList", queryString.stringify(data), { params: params });
    },
    // 系统管理：菜单管理 获取父级菜单
    sysMenuGetParentModule(data, params) {
        return Axios.post("/api/sys/module/getParentModule", queryString.stringify(data), { params: params });
    },
    // 系统管理：菜单管理 新增菜单
    sysMenuAddModule(data, params) {
        return Axios.post("/api/sys/module/addModule", queryString.stringify(data), { params: params });
    },
    // 系统管理：菜单管理 修改菜单
    sysMenuUpdateModule(data, params) {
        return Axios.post("/api/sys/module/updateModule", queryString.stringify(data), { params: params });
    },
    // 根据角色查询用户:主要用户查询催收公司
    sysQueryUserByRole(data, params) {
        return Axios.post("/api/sys/user/queryUserByRole", queryString.stringify(data), { params: params });
    },
    // 系统管理：获取用户菜单列表
    getUserModuleList(data, params) {
        return Axios.post("/api/sys/user/getUserModuleList", queryString.stringify(data), { params: params });
    },
    // 系统管理 : 参数设置 默认值
    queryNewData(data, params) {
        return Axios.get("/api/paramter/queryParamter", data, { params: params });
    },
    //系统管理 : 参数设置 更新
    queryNewUpdateData(data, params) {
        return Axios.post("/api/paramter/updateParamter", queryString.stringify(data), { params: params });
    },
    //数据字典
    queryPageDictionary(data, params) {
        return Axios.post("/api/sys/dictionary/queryPageDictionary", queryString.stringify(data), {
            params: params
        });
    },
    updateDictionary(data, params) {
        return Axios.post("/api/sys/dictionary/updateDictionary", queryString.stringify(data), {
            params: params
        });
    },
    queryAllDetailOrder(data, params) {
        return Axios.post("/api/sys/dictionaryDetail/queryDetailOrder", queryString.stringify(data), {
            params: params
        });
    },
    queryPageDictionaryDetail(data, params) {
        return Axios.post("/api/sys/dictionaryDetail/queryPageDictionaryDetail", queryString.stringify(data), {
            params: params
        });
    },
    updateDictionaryDetail(data, params) {
        return Axios.post("/api/sys/dictionaryDetail/updateDictionaryDetail", queryString.stringify(data), {
            params: params
        });
    },


    //by weiqian<2019-07-19>
    
    getOrderList(data, params) { //客服订单列表
        return Axios.post("/api/order/getOrderList", queryString.stringify(data), {
            params: params
        });
    },
    getCustInfo(data, params) { //根据订单编号获取客户信息
        return Axios.post("/api/order/getCustInfo", queryString.stringify(data), {
            params: params
        });
    },
    getOrderProduct(data, params) { //根据订单编号获取产品信息
        return Axios.post("/api/order/getOrderProduct", queryString.stringify(data), {
            params: params
        });
    },
    getDeductInfo(data, params) { //获取扣款信息
        return Axios.post("/api/order/getDeductInfo", queryString.stringify(data), {
            params: params
        });
    },
    refundList(data, params) { //退款列表
        return Axios.post("/refund/refundList", data, {
            params: params
        });
    },
    refundRatio(data, params) { //退款比例
        return Axios.post("/refund/refundRatio", data, {
            params: params
        });
    },
    saveRefundRecord(data, params) { //新增退款记录
        return Axios.post("/api/order/saveRefundRecord", queryString.stringify(data), {
            params: params
        });
    },
    refundListRecord(data, params) { //退款处理记录
        return Axios.post("/refund/refundListRecord", queryString.stringify(data), {
            params: params
        });
    },
    refundMoney(data, params) { //退款
        return Axios.post("/refund/refundMoney", queryString.stringify(data), {
            params: params
        });
    },
    sumAmount(data, params) { //退款处理记录金额合计查询
        return Axios.post("/refund/sumAmount", queryString.stringify(data), {
            params: params
        });
    },
    orderApportionAndUndistributed(data, params) { //订单分配列表
        return Axios.post("/api/sys/order/orderApportionAndUndistributed", data, {
            params: params
        });
    },
    orderjudgePeople(data, params) { //询查信审人员
        return Axios.post("/api/sys/order/orderjudgePeople", data, {
            params: params
        });
    },
    orderAllocationToUser(data, params) { //分配信贷订单
        return Axios.post("/api/sys/order/orderAllocationToUser", data, {
            params: params
        });
    },
    orderCheckList(data, params) { //订单审批列表
        return Axios.post("/order/orderCheckList", data, {
            params: params
        });
    },
    queryUserPicture(data, params) { //身份证正反面及活体照片查询
        return Axios.post("/report/idcardUrl", data, {
            params: params
        });
    },
    contactsInfo(data, params) { //联系人信息
        return Axios.post("/order/contactsInfo", data, {
            params: params
        });
    },
    orderRecordHis(data, params) { //历史申请记录
        return Axios.post("/order/orderRecordHis", data, {
            params: params
        });
    },
    callRecord(data, params) { //手机通话记录
        return Axios.post("/call/callRecord", data, {
            params: params
        });
    },
    operatorCallRecord(data, params) { ///report/运营商通话记录
        return Axios.post("/report/operatorCallRecord", data, {
            params: params
        });
    },
    smsRecording(data, params) { //手机短信记录
        return Axios.post("/report/smsRecording", data, {
            params: params
        });
    },
    callList(data, params) { //手机通讯录
        return Axios.post("/report/callList", data, {
            params: params
        });
    },
    examineInfo(data, params) { //审核信息
        return Axios.post("/order/examineInfo", data, {
            params: params
        });
    },
    
    contrat(data, params) { //协议列表
        return Axios.post("/report/contrat", data, {
            params: params
        });
    },
    riskSuccessFlow(data, params) { //风控扣款流水,退款处理记录
        return Axios.post("/api/order/riskSuccessFlow",  queryString.stringify(data), {
            params: params
        });
    },
    checkReport(data, params) { //判断订单是否有批核报告
        return Axios.post("/api/order/report/check",  queryString.stringify(data), {
            params: params
        });
    },
    
    
}