<template>
  <div class="infoTab">
        <div class="title" >
          <span class="titname">认证信息图片</span>
        </div>
        <div class="photo" >
           <img :src="pics.frontUrl" >
           <p>证件正面</p>
        </div>
        <div class="photo" >
           <img :src="pics.backUrl" >
           <p>证件反面</p>
        </div>
        <div class="photo" >
           <img :src="pics.livingUrl" >
           <p>活体照片</p>
        </div>
  </div>
</template>
<script>
import HeadTop from "@/components/HeadTop";
import Pagination from "@/components/Pagination";
import api from "@/api/index";
export default {
  data() {
    return {
      pics: {},
    }
  },
  mounted(){
    this.queryUserPicture()
  },
  methods: {
    queryUserPicture(){
      api.queryUserPicture({
        orderNo:this.$route.query.orderNo,
        userName:this.$route.query.userName,
        userId:this.$route.query.userId
      }).then(res => {
          if (res.data.success) {
            this.pics = res.data.data
          }
      })
    },
  },
}
</script>
<style lang="less" scoped>
 
</style>
