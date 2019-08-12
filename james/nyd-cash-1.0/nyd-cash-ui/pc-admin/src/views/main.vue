<template>
  <div id="main">
    <v-header :navdata="navData"></v-header>
    <div class="layout-container">
      <transition name="fade" mode="out-in">
        <router-view :navdata="navData"></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
  import Header from '@/components/common/header'
  import Menu from '@/router/categories'
  export default {
    name: 'main',
    data: function () {
      return {
        navData: []
      }
    },
    created: function () {
      this.navData = Menu
    },
    components: {
      "v-header": Header
    }
  }
</script>

<style lang="less">
  .layout-container {
    position: absolute;
    right: 0;
    top: 50px;
    bottom: 0;
    left: 0;
    overflow: hidden;
  }

  .fade-enter-active, .fade-leave-active {
    transition: opacity 0.25s ease-out;
  }

  .fade-enter, .fade-leave-to {
    opacity: 0;
  }

  .page-bread {
    height: 40px;
    padding: 0 15px;
    background: #fff;
    border-bottom: 1px solid #eaeaea;

  .el-breadcrumb {
    line-height: 40px;
  }

  }
  .search-form {
    height: auto;
    overflow: hidden;
    padding: 22px 15px 0;
    background: #f9f9f9;
  }

  .grid-toolbar {
    height: auto;
    overflow: hidden;
    padding: 10px 20px;

  .tool-btn {
    display: inline-block;
    padding: 8px 15px;
    border: none;
    background: none;
    font-size: 12px;
    border-radius: 3px;
    outline: none;
    cursor: pointer;
    margin: 0 2px;
  }

  .btn-refresh {
    background: #76c884 url(../assets/icon_refresh.png) no-repeat 10px center;
    background-size: 15px 15px;
    padding-left: 32px;
    color: #fff;
  }

  .btn-add {
    background: #efa759 url(../assets/icon_add.png) no-repeat 10px center;
    background-size: 15px 15px;
    padding-left: 32px;
    color: #fff;
  }

  .btn-export {
    background: url(../assets/icon_export.png) no-repeat 10px center;
    background-size: 13px 13px;
    padding-left: 25px;
    color: #777;
  }

  }
  .grid-content {
    height: auto;
    overflow: hidden;
    padding: 0 10px;

  .el-table th {
    background: #fff;

  }

  .el-table__header-wrapper {
    border-bottom: 1px solid #eaeaea;
  }

  .el-table__header-wrapper thead div, .el-table__fixed-header-wrapper thead div {
    background: #fff;
    padding: 20px 18px 10px;
    color: #444;
    font-weight: bold;
  }

  .el-table td {
    height: 48px;
  }

  .el-button--text {
    color: #4796ef;
  }

  .el-table--striped .el-table__body tr.el-table__row--striped td {
    background: #f4f8fb;
  }

  }
  .grid-page {
    height: auto;
    overflow: hidden;
    padding: 25px 15px;

  .el-pagination__sizes {
    margin: 0 0 0 10px;
  }

  .el-pagination__total {
    margin: 0;
  }

  }
  .statistic-content {
    height: auto;
    overflow: hidden;
    padding: 10px;
    color: #f97042;
    border: 1px solid #fee7a9;
    border-radius: 4px;
    background: #fffbee;
    text-align: center;
    font-size: 16px;

  span {
    display: inline-block;
    padding: 0 5px;
    font-size: 14px;
  }

  }
</style>
