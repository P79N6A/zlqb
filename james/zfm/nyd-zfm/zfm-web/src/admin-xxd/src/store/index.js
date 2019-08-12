import Vue from 'vue';
import Vuex from 'vuex'
import createLogger from 'vuex/dist/logger';
import actions from './actions'
import mutations from './mutations'
import getters from './getters'

const debug = process.env.NODE_ENV !== 'production';
Vue.use(Vuex);

var state = {
    rolerMenuList: [], //鑿滃崟鍒楄〃
    diffDays: 0
}

var store = new Vuex.Store({
    actions,
    mutations,
    getters,
    state,
    modules: {},
    strict: debug,
    plugins: debug ? [createLogger()] : []
});

if (module.hot) {
    module.hot.accept([
        './getters',
        './actions',
        './mutations'
    ], () => {
        store.hotUpdate({
            getters: require('./getters'),
            actions: require('./actions'),
            mutations: require('./mutations')
        })
    })
}
export default store;