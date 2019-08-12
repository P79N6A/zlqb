<template>
  <div class="sgp">
    <br/><br/>
    <p>使用：</p>
    <p>1): vue2使用：https://cn.vuejs.org/</p>

    <p>2): element使用： http://element.eleme.io/#/nyd-cash-CN</p>
    <br/><br/>
    <p>
    <p>项目结构如下：</p>
    <p> |-----dist              (打包后生成的文件夹)  </p>
    <p> |-----src</p>
    <p> |    |----commoncss   (公共css文件夹)</p>
    <p> |      |----commonjs    (公共js文件夹)</p>
    <p> |      |----components  (公共组件文件夹)</p>
    <p> |      |----fonts       (字体库文件夹)</p>
    <p> |      |----image       (图片文件夹)</p>
    <p> |      |----router      (路由文件夹)</p>
    <p> |      |----vue         (vue页面文件夹)</p>
    <p> |      |----vuex        (全局状态文件夹)</p>
    <p> |      |----App.vue</p>
    <p> |      |----main.js     (项目总入口)</p>
    <p> |      |----resouse.js  (请求路径) </p>
    <p> |-----.babelrc</p>
    <p> |-----.eslintrc.js</p>
    <p> |-----.postcssrc.js</p>
    <p> |-----index.html</p>
    <p> |-----package.json</p>
    <p> |-----README.md</p>
    <p> |-----webpack.config.js</p>
    <p> |-----webpack.prod.config.js</p>
  </div>
</template>
<script>
  export default {
    name: 'index',
    data () {
      return {}
    },
    mounted (){
      //Utils.isLogin();//判断是否登录
    },
    methods: {
      selectReport(){

      }
    }
  }
</script>
<style scoped>
  .sgp p {
    font-size: 17px;
    color: red;
    margin: 0 0 0 30px;
  }

</style>
