<template>
  <q-layout ref="layout" view="Lhh lpr lFf" reveal>
    <q-toolbar slot="header">
      <q-btn flat v-go-back.single="store.backRoute" class="cordova-only electron-only">
        <q-icon name="arrow_back"/>
      </q-btn>
      <q-toolbar-title>
        <q-icon
          v-show="store.icon"
          style="font-size: 2rem; margin-right: 5px;"
          :name="store.icon"
        />
        {{ store.title }}
      </q-toolbar-title>
      <q-btn flat class="within-iframe-hide" @click="$refs.layout.toggleLeft()">
        <q-icon name="menu"/>
      </q-btn>
    </q-toolbar>

    <!-- <q-tabs
       slot="navigation"
       v-if="store.tabs.length > 0"
       :class="{'within-iframe-hide': !store.iframeTabs}"
     >
       <q-route-tab
         v-for="tab in store.tabs"
         :key="tab"
         slot="title"
         :icon="tab.icon"
         :to="`/views${store.hash}/${tab.hash}`"
         :label="tab.label"
         exact
         replace
       />
     </q-tabs>-->

    <q-scroll-area slot="left" style="width: 100%; height: 100%;" class="bg-grey-3">
      <div class="row flex-center bg-white" style="width: 100%; height: 100px;">
        <!--<img src="~assets/quasar-logo.svg" style="height: 75px; width 75px;"/>-->
        <div style="margin-left: 15px">
          <!--Quasar v{{ $q.version }}-->
        </div>
      </div>
      <q-list style="padding: 0px" no-border>
        <!--<q-side-link
          item
          to="/"
          exact
          replace
        >
          <q-item-side icon="home" />
          <q-item-main label="Showcase home" />
          <q-item-side right icon="chevron_right" />
        </q-side-link>
        <q-item-separator />-->
        <template v-for="category in categories">
          <!-- <q-list-header>

           </q-list-header>-->
          <q-toolbar>
            <q-toolbar-title>
              {{ category.title }}
            </q-toolbar-title>
            <q-btn flat>
              <q-icon name="more_vert"/>
            </q-btn>
          </q-toolbar>
          <q-side-link
            item
            v-for="feature in category.features"
            :key="feature"
            :to="`/${category.hash}/${feature.hash}`"
            replace
          >
            <q-item-side :icon="feature.icon"/>
            <q-item-main :label="feature.title"/>
            <q-item-side right icon="chevron_right"/>
          </q-side-link>
          <!--<q-item-separator />-->
        </template>
      </q-list>
    </q-scroll-area>

    <router-view/>
  </q-layout>
</template>

<script>
  import store from './showcase-store'
  import categories from './menu'
  import {
    QScrollArea,
    QList,
    QSideLink,
    QListHeader,
    QItemSide,
    QItemMain,
    QItemSeparator,
    QBtn,
    QLayout,
    QToolbar,
    QToolbarTitle,
    QIcon,
    QTabs,
    QRouteTab,
    GoBack
  } from 'quasar'

  export default {
    components: {
      QScrollArea,
      QList,
      QSideLink,
      QListHeader,
      QItemSide,
      QItemMain,
      QItemSeparator,
      QBtn,
      QLayout,
      QToolbar,
      QToolbarTitle,
      QIcon,
      QTabs,
      QRouteTab
    },
    directives: {
      GoBack
    },
    data() {
      return {
        categories,
        store: store.state
      }
    }
  }
</script>
