<template>
  <div class="treeData">
    <el-tree
      :data="treeData"
      ref="tree"
      :node-key="id"
      :props="defaultProps"
      :default-checked-keys = 'checkedArr'
      show-checkbox
      @node-click="handleNodeClick"
    ></el-tree>
  </div>
</template>

<script>
export default {
  props: {
    treeData: {
      //用于展示tree
      type: Array,
      default: function() {
        return [];
      }
    },
    id: {
      type: String,
      default: "id"
    },
    showCheckbox: {
      type: Boolean,
      default: true
    },
    defaultProps: {
      type: Object,
      default: function() {
        return {
          children: "children",
          label: "name"
        };
      }
    },
    checkedArr: {
      type: Array,
      default: function() {
        return [];
      }
    }
  },
  data() {
    return {};
  },
  mounted() {},
  methods: {
    handleNodeClick(selectData) {
      // console.log(selectData,"selectData")
      this.$emit("handleNodeClick", selectData);
    }
  }
};
</script>

<style scoped lang="scss">
.treeData {
  overflow: auto;
  height: 200px;
  width: 100%;
  padding-top: 17px;
}
</style>