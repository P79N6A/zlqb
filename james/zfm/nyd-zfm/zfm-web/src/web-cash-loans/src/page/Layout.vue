<template>
  <div class="manage_page fillcontain">
    <header class="main-header">
      <MainHeader @toggleMenu="toggleMenu"></MainHeader>
    </header>
    <div class="ly_left" :class="{wid50: isCollapse}">
      <div class="scroll_bar">
        <el-menu
        :default-active = "$route.path"
        style="min-height: 100%;"
        background-color="#001529"
        text-color="#fff"
        active-text-color="#fff"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        :unique-opened="true"
      >
        <div v-for="item in muneList" :key="item.id">
          <el-menu-item :index="item.url  || ''" v-if="item.childrenModule == null">
            <i :class="item.iconUrl" class="icon_mune_com"></i>
            {{item.name}}
          </el-menu-item>
          <el-submenu :index="item.id + ''" v-else>
            <template slot="title">
              <i :class="item.iconUrl" class="icon_mune_com"></i>
              {{item.name}}
            </template>
            <el-menu-item
              :index="itemChildren.url"
              v-for="itemChildren in item.childrenModule"
              :key="itemChildren.id"
            >{{itemChildren.name}}</el-menu-item>
          </el-submenu>
        </div>
      </el-menu>
      </div>
    </div>
    <div class="ly_right" :class="{marginLeft: isCollapse}">
      <div>
        <head-top ></head-top>      
        <!-- <h2 v-else class="index_word">欢迎您登录现金贷后台管理系统</h2>   -->
      </div>
      <div class="ly_router_wrap" style="margin-bottom:20px">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import HeadTop from "@/components/HeadTop";
import MainHeader from "@/components/MainHeader";
import { mapState, mapActions } from "vuex";
export default {
  data() {
    return {
      isCollapse: false,
      width: "200",
      showHead: false,
    };
  },
  computed: {
    defaultActive: function() {
      return this.$route.path.replace("/", "");
    },
    ...mapState({
      muneList: state => state.rolerMenuList
    })
  },
  mounted() {
  },
  methods: {
    toggleMenu(val) {
      this.isCollapse = val;
    },
    ...mapActions({
      updateRolerMenuList: "UPDATE_ROLE_MENU_LIST"
    })
  },
  watch: {
    isCollapse(val) {
      if (val) {
        this.width = "0";
      } else {
        this.width = "200";
      }
    },
    "$route": {
      handler(val) {
         if (this.$route.path  == '/index') {
            this.showHead = false
          } else {
            this.showHead = true
          }
      },
      deep: true
    },
  },
  components: {
    HeadTop,
    MainHeader
  }
};
</script>


<style lang="less" scoped>
.ly_router_wrap {
  padding: 10px 10px 50px;
}
.ly_left {
  // min-height: 100%;
  background-color: #001529;
  width: 220px;
  // float: left;
   transition: width 0.4s;
  position: absolute;
    left: 0;
    top: 64px;
    bottom: 0;
    /* margin-bottom: -17px; */
    /* margin-right: -17px; */
    overflow: hidden;
    .scroll_bar {
      height: 100%;
      overflow: hidden;
      overflow-y: scroll;
    }
    /*webkit内核*/
      // .scroll_bar::-webkit-scrollbar {
      //     width:0px;
      //     height:0px;
      // }
      // .scroll_bar::-webkit-scrollbar-button    {
      //     background-color:rgba(0,0,0,0);
      // }
      // .scroll_bar::-webkit-scrollbar-track     {
      //     background-color:rgba(0,0,0,0);
      // }
      // .scroll_bar::-webkit-scrollbar-track-piece {
      //     background-color:rgba(0,0,0,0);
      // }
      // .scroll_bar::-webkit-scrollbar-thumb{
      //     background-color:rgba(0,0,0,0);
      // }
      // .scroll_bar::-webkit-scrollbar-corner {
      //     background-color:rgba(0,0,0,0);
      // }
      // .scroll_bar::-webkit-scrollbar-resizer  {
      //     background-color:rgba(0,0,0,0);
      // }
      // .scroll_bar::-webkit-scrollbar {
      //     width:0;
      //     height:0;
      // }
      // /*o内核*/
      // .scroll_bar .-o-scrollbar{
      //     -moz-appearance: none !important;   
      //     background: rgba(0,0,0,0) !important;  
      // }
      // .scroll_bar::-o-scrollbar-button    {
      //     background-color:rgba(0,0,0,0);
      // }
      // .scroll_bar::-o-scrollbar-track     {
      //     background-color:rgba(0,0,0,0);
      // }
      // .scroll_bar::-o-scrollbar-track-piece {
      //     background-color:rgba(0,0,0,0);
      // }
      // .scroll_bar::-o-scrollbar-thumb{
      //     background-color:rgba(0,0,0,0);
      // }
      // .scroll_bar::-o-scrollbar-corner {
      //     background-color:rgba(0,0,0,0);
      // }
      // .scroll_bar::-o-scrollbar-resizer  {
      //     background-color:rgba(0,0,0,0);
      // }
      // /*IE10,IE11,IE12*/
      // .scroll_bar{
      //     -ms-scroll-chaining: chained;
      //     -ms-overflow-style: none;
      //     -ms-content-zooming: zoom;
      //     -ms-scroll-rails: none;
      //     -ms-content-zoom-limit-min: 100%;
      //     -ms-content-zoom-limit-max: 500%;
      //     -ms-scroll-snap-type: proximity;
      //     -ms-scroll-snap-points-x: snapList(100%, 200%, 300%, 400%, 500%);
      //     -ms-overflow-style: none;
      //     overflow: auto;
      // }


}
.ly_right {
  height: 100%;
  overflow: auto;
  margin-left: 220px;
  transition: margin-left 0.4s;
}
.marginLeft {
  margin-left: 54px;
  // transition: all 0.4s
   transition: margin-left 0.4s;
}
.wid50 {
  width: 54px;
  transition: width 0.4s;
  //  transition: margin-left 0.4s;
}

</style>
