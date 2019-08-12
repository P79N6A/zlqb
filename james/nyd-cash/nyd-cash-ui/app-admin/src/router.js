import Vue from 'vue'
import VueRouter from 'vue-router'
import {Platform} from 'quasar'

import categories from '@/showcase/categories'
//import showcaseStore from '@/showcase/showcase-store'
import menus from 'views/menu'

Vue.use(VueRouter)

function load(component) {
  return () => import(`views/${component}.vue`)
}

let routes = [
  {
    path: '/index', component: load('index')
  }
]


let views = {
  path: '/',
  component: load('index'),
  children: []
}


function component(path, config) {
  return {
    path,
    meta: config,
    component: load(path)
  }
}





menus.forEach(menu => {
  if (menu.extract) {
    return
  }
  menu.features.forEach(feature => {
    let path = menu.hash + '/' + feature.hash

    if (!feature.tabs) {
      views.children.push(component(path, feature))
      return
    }

    feature.tabs.forEach(tab => {
      let subpath = path + '/' + tab.hash
      views.children.push(component(subpath, {
        title: tab.title,
        hash: '/' + path,
        iframeTabs: feature.iframeTabs,
        icon: feature.icon,
        tabs: feature.tabs
      }))
    })

    routes.push({
      path:  path,
      redirect: path + '/' + feature.tabs[0].hash
    })
  })
})


routes.push(views)
routes.push({path: '*', component: load('error404')})

const Router = new VueRouter({routes})

Router.beforeEach((to, from, next) => {
  document.documentElement.scrollTop = 0
  document.body.scrollTop = 0

  if (to.meta) {
    //showcaseStore.set(to.meta)
  }
  next()
})

export default Router
