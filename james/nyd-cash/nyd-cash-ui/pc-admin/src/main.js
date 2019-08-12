// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import ES6Promise from 'es6-promise'
import VueAreaLinkage from 'vue-area-linkage';


import App from './App'
import Router from './router'
import Api from './router/api'
import Store from './store'
import Ajax from './resources/js/ajax'
import Utils from './resources/js/util.js'
import Dialog from './resources/js/dialog.js'

window.Utils = Utils;
window.Api = Api;
window.Ajax = Ajax;
window.Dialog = new Dialog();

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css';

Vue.use(Vuex)
Vue.use(ElementUI);
Vue.use(VueAreaLinkage);
ES6Promise.polyfill();

Vue.config.productionTip = false

/* eslint-disable no-new */
window.vm = new Vue({
  el: '#app',
  router:Router,
  store : Store,
  template: '<App/>',
  components: { App }
})
