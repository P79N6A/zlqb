<template>
  <div>
    <div class="border-1px padding-10px">
      <div class="common-reset-inptu">
        <div class="common-display">
          <span>客户姓名：</span>
          <el-input clearable placeholder="请输入" v-model="formData.custName"></el-input>
        </div>
        <div class="common-display">
          <span class="common-label-text">手机号码：</span>
          <el-input clearable placeholder="请输入" v-model="formData.mobile"></el-input>
        </div>
        <div class="common-display" v-if="limitFormData.receiveUserName">
          <span>贷后专员：</span>
          <el-input clearable placeholder="请输入" v-model="formData.receiveUserName"></el-input>
        </div>
        <div class="common-display">
          <span class="common-label-text">贷款编号：</span>
          <el-input clearable placeholder="请输入" v-model="formData.loanNo"></el-input>
        </div>        
      </div>
      <div class="common-reset-inptu">   
        <div class="common-display" v-if="limitFormData.LoanOrderState">
          <span class="common-label-text">订单状态：</span>
          <el-select
            v-model="formData.urgeStatus"
            placeholder="请选择"
            clearable
          >
            <el-option
              v-for="item in LoanOrderState"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </div>     
        <div class="common-display" v-if="limitFormData.refundDate">
          <span class="common-label-text">分配时间：</span>
          <el-date-picker
            v-model="formData.createTime"
            type="daterange"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </div>
        <div class="common-display">
          <span class="common-label-text">应还日期：</span>
          <el-date-picker
            v-model="formData.promiseRepayment"
            type="daterange"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </div>
        <el-button type="primary" @click="searchHander">查询</el-button>
        <el-button type="primary" @click="disCustHander" v-if="limitFormData.disCustHanderBtn">人工分配</el-button>
      </div>
    </div>    
  </div>
</template>
<script>
import { LoanOrderState } from "@/utils/constOptions";
export default {
  props: {
    limitFormData: {
      type: Object,
      default() {
        return {
          // 人工分配 按钮
          disCustHanderBtn: true,
          // 分配时间
          refundDate: true,
          // 订单状态
          LoanOrderState: true,
          // 贷后专员 
          receiveUserName: true
        };
      }
    }
  },
  data() {
    return {
      formData: {},
      LoanOrderState
    };
  },
  methods: {
    disCustHander() {
      this.dialogFormVisible = true
      this.$emit("disCustHander");
    },
    searchHander() {
      this.$emit("searchHand", this.formData);
    }
  }
};
</script>
<style lang="less" scoped>
.el-range-editor {
  width: 230px;
}
.common-display {
  margin-right: 10px;
}
</style>