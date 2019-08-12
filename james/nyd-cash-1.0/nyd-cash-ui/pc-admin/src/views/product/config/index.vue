<template>
  <div class="grid-container">
    <div class="search-form">
      <el-form :inline="true" :model="form.query">
        <el-form-item label="名称">
          <el-input v-model="form.query.name" placeholder="名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="doSeach">查询</el-button>
          <el-button @click="doClear">清空</el-button>
          <el-button @click="add">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="grid-content">
      <el-table :data="tableData" v-loading.body="loading" stripe style="width: 100%">
        <el-table-column prop="id" label="#" width="150"/>
        <el-table-column prop="batch" label="批次号" width="150"/>
        <el-table-column prop="model" label="型号" width="150"/>
        <el-table-column prop="count" label="数量" width="150"/>
        <el-table-column prop="ptime" label="生产时间" width="150"/>
        <el-table-column prop="gtime" label="保修时间" width="150"/>

        <el-table-column fixed="right" label="操作" width="200" class-name="action-column">
          <template scope="scope">
            <div class="action-column">
              <el-button type="text" size="small" @click.native.prevent="showDetail(scope.row)">详情</el-button>
              <el-button type="text" size="small">启用</el-button>
              <el-button type="text" size="small" @click.native.prevent="edite(scope.row)">编辑</el-button>
              <el-button type="text" size="small" @click.native.prevent="edite(scope.row)">执行生产</el-button>
              <el-button type="text" size="small" @click.native.prevent="deleteRow(scope.$index, tableData)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="grid-page">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="form.query.pageNo"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="form.query.pageSize"
        layout="total,->, prev, pager, next, jumper, sizes"
        :total="tableData.length">
      </el-pagination>
    </div>

    <!-- 详情弹框 -->
    <el-dialog title="客户详情" :visible.sync="dialogVisible" size="dl-small" :modal-append-to-body="false"
               :close-on-press-escape="false">
      <detail :detail="detail"/>
    </el-dialog>
  </div>
</template>

<script>
  //客户详情
  import Detail from './detail.vue'

  export default {
    name: 'grid1',
    data: function () {
      return {
        loading: true,
        dialogVisible: false,
        tableData: [],
        detail: {},
        form: {
          edit: {
            id: '',
            // 品牌
            brand: '',
            // 生产所在省
            province: '',
            // 生产所在市
            city: '',
            // 系列
            serial: '',
            // 型号
            model: '',
            // 存放仓库
            warehouse: '',
            // 区域代码
            areaCode: '',
            //生产日期
            ptime: '',
            // 保修日期
            gtime: '',
            //生产数量
            count: '',
            //生产批次
            batch: '',
            descr: '',
          },
          query: {
            name: '',
            pageNo: 1,
            pageSize: 10,
          }
        }
      }
    },
    mounted: function () {
      this.loadData()
    },
    components: {
      "detail": Detail
    },
    methods: {
      loadData: function () {
        var me = this;
        this.loading = true;
        var params = this.form.query;
        Utils.ajax(Api.production.list, params, (success) => {
          //this.options.roleOptions.roles = success.data;
          me.tableData = success.data.list;
        });
        setTimeout(() => {
          this.loading = false;
        }, 100);
      },
      doSeach: function () {
        this.currentPage = 1;
        console.log('submit!');
        this.loadData();
      },
      doClear: function () {
        this.formInline = {
          type: '',
          name: '',
          state: ''
        }
      },
      deleteRow: function (index, rows) {
        this.$confirm('确定删除该该条记录吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          rows.splice(index, 1);
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      handleSizeChange: function (val) {
        this.pageSize = val;
        console.log(`每页 ${val} 条`);
        this.loadData();
      },
      handleCurrentChange: function (val) {
        this.pageNo = val;
        this.loadData();
        console.log(`当前页: ${val}`);
      },
      add: function () {
        sessionStorage.removeItem("_factory");
        this.$router.push({path: '/production/config/add'});
      },
      edite: function (row) {
        sessionStorage.setItem("_factory", JSON.stringify(row));
        this.$router.push({path: '/production/config/edit/' + row.id});
      },
      showDetail: function (detail) {
        this.detail = detail;
        this.dialogVisible = true;
      }
    }
  }
</script>
<style lang="less">
  .grid-container {
    height: auto;
    overflow: hidden;

  .action-column {
    text-align: right;
  }

  .color-gred {
    color: #999;
  }

  }

  .el-dialog--dl-small {
    width: 600px;
  }
</style>
