import Vue from 'vue'
import Router from 'vue-router'
import routes from './routes.js'
import * as types from '@/store/types.js'
import store from '@/store'
Vue.use(Router)
const router = new Router({
    routes: routes
})
router.afterEach((to, from, next) => {
        document.title = to.meta.title[1] || '';
    })
    // store.dispatch(types.UPDATE_ROLE_MENU_LIST, {}).then(res => {
    //     console.log(res, 'resresres')
    // })
export default router