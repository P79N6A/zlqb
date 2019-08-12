import Vue from 'vue'

import router from './router/'
import store from './store'
import App from './App.vue'
import './assets/css/style.scss'
import './assets/css/reset.scss'
import * as types from '@/store/types.js'
import Config from './config/app'
// import { isLogin } from './utils/dataStorage'
import VueWechatTitle from 'vue-wechat-title'
Vue.use(VueWechatTitle)

import '@/style/common.less'
import '@/style/resetElement.less'

Vue.prototype.$Config = Config;
// 按需加载
import {
    Pagination,
    Dialog,
    Autocomplete,
    Dropdown,
    DropdownItem,
    DropdownMenu,
    Menu,
    MenuItem,
    Submenu,
    Input,
    Radio,
    RadioGroup,
    RadioButton,
    Checkbox,
    CheckboxButton,
    CheckboxGroup,
    Switch,
    Select,
    Option,
    Button,
    ButtonGroup,
    Table,
    TableColumn,
    Form,
    FormItem,
    Alert,
    Icon,
    Row,
    Col,
    Card,
    Loading,
    MessageBox,
    Message,
    Notification,
    Tree,
    Breadcrumb,
    BreadcrumbItem,
    DatePicker,
    Tag,
    Tabs,
    TabPane,
} from 'element-ui';

Vue.use(TabPane);
Vue.use(Tabs);
Vue.use(Tag);
Vue.use(Pagination);
Vue.use(Dialog);
Vue.use(Autocomplete);
Vue.use(Dropdown);
Vue.use(DropdownItem);
Vue.use(DropdownMenu);
Vue.use(Menu);
Vue.use(MenuItem);
Vue.use(Submenu);
Vue.use(Input);
Vue.use(Radio);
Vue.use(RadioGroup);
Vue.use(RadioButton);
Vue.use(Checkbox);
Vue.use(CheckboxButton);
Vue.use(CheckboxGroup);
Vue.use(Switch);
Vue.use(Select);
Vue.use(Option);
Vue.use(Button);
Vue.use(ButtonGroup);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Alert);
Vue.use(Icon);
Vue.use(Row);
Vue.use(Col);
Vue.use(Card);
Vue.use(Tree);
Vue.use(Breadcrumb);
Vue.use(BreadcrumbItem);
Vue.use(DatePicker);
Vue.use(Loading.directive);

Vue.prototype.$loading = Loading.service;
Vue.prototype.$msgbox = MessageBox;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$prompt = MessageBox.prompt;
Vue.prototype.$notify = Notification;
Vue.prototype.$message = Message;

Vue.prototype.$ELEMENT = { size: 'mini', zIndex: 3000 };
// Vue.use(ElementUI, { size: 'mini' })

// router.beforeEach((to, from, next) => {
//     window.document.title = to.meta.title ? to.meta.title + '-' + Config.siteName : Config.siteName;
//     if (!isLogin() && to.path != '/login') {
//         next({ path: '/login' });
//     } else {
//         next();
//     }
// });
// router.afterEach(transition => {

// });
// 获取菜单
store.dispatch(types.UPDATE_ROLE_MENU_LIST, {})
Vue.filter('globalFilter', function (value) {
    if(value == null || value == ''){
        return '--'
    } else{
        return value
    }
})
// 根据路由设置标题
router.beforeEach((to, from, next) => {
    /*路由发生改变修改页面的title */
    if(to.meta.title) {
      document.title = to.meta.title
    }
    next();
})

new Vue({
    el: '#app',
    router,
    store,
    render: h => h(App)
})