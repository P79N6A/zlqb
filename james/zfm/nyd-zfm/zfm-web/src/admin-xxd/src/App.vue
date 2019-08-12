<template>
  <div id="app" class="fillcontain">
    <router-view></router-view>
  </div>
</template>
<script>
import {mapActions} from 'vuex'
import api from '@/api/index'
import router from '@/router/routes.js'
export default {
  created() {
    this.getUserInfo()
  },
  methods: {
    getUserInfo() {
      api.getUserInfo().then(res => {
        if (res.data.success) {
          localStorage.setItem('user_data',JSON.stringify(res.data.data))    
        } else {
          this.$notify.error({
            title: "提示",
            message: res.data.msg
          });
        }
      })
    },
    ...mapActions({
      updateRolerMenuList: "UPDATE_ROLE_MENU_LIST"
    })
  },
  watch: {
  }
};
</script>
<style lang="scss" scoped>
.fillcontain{
  overflow: hidden;
}
</style>
