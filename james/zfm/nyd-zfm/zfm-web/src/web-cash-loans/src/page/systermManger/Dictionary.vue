<template>
 <div class="dictionary-wrap">
	<div class="clearfix">
	 	<div class="dictionary-con ft">
	 		<div class="clearfix">
	 			<div class="ft" style="width:60%">
	 				<el-input placeholder="请输入大类名称" v-model='search' class="input-with-select" clearable>
				    <el-button slot="append" icon="el-icon-search" @click='searchDet'></el-button>
				  </el-input>
	 			</div>
	 			<el-button class="rt" type="primary" @click="addbigClass()">新增大类</el-button>
	 		</div>
	 		<div class="tables resetTable">
	 			<el-table
				    :data="tableData3"
				    border
				    style="width: 100%" v-loading="loadingTable1">
				    <el-table-column
				    	width='70px'
				    	:show-overflow-tooltip="true"
				    	align='center'
				    	label="序号"
	      				type="index">
	    			</el-table-column>
				    <el-table-column
				        prop="name"
				        align='center'
				      	:show-overflow-tooltip="true"
				      	label="大类名称">
				      	<template slot-scope="scope">
					        <el-button
					        	@click.native.prevent="addDetail(scope)"
					          	type="text"
					          	size="small">
					          	{{scope.row.name}}
					        </el-button>
				      	</template>
				    </el-table-column>
				    <el-table-column
				    	:show-overflow-tooltip="true"
				    	align='center'
				      	prop="code"
				     	label="大类编码">
				    </el-table-column>
				    <el-table-column
				    	:show-overflow-tooltip="true"
				    	align='center'
				      	label="操作">
				      <template slot-scope="scope">
				        <el-button
				         	@click.native.prevent="modifyData(scope)"
				          	type="text"
				          	size="small">
				          	修改
				        </el-button>
				      </template>
				    </el-table-column>
				</el-table>
	 		</div>
	 		<div class="com_pad20 alignCen">
	 			<pagination
					:currentPage = 'currentPage'
					:total = 'total'
					:myPageSizes = 'pageSize'
					@handleSizeChange = 'handleSizeChange'
					@handleCurrentChange = 'handleCurrentChange'
	 				>
	 			</pagination>
	 		</div>
	 		<div>
	 			<!--新增大类修改 ---SRATR---->
	 			<el-dialog :close-on-click-modal ='false' :title="bigClassTitle" class="dialog-wrap" :visible.sync="dialogFormVisible" width='400px' @close="addDiaClose">
				    <el-form :model="form" ref="form" :rules="form_rules">
					    <el-form-item label="大类名称：" :label-width="formLabelWidth"   prop="name">
					      <el-input v-model.trim ="form.name" auto-complete="off" :maxlength='50'></el-input>
					    </el-form-item>
					    <el-form-item label="大类编码：" :label-width="formLabelWidth"  prop="code" >
					      <el-input v-model.trim="form.code" auto-complete="off" :disabled='flagCode' :maxlength='20'></el-input>
					    </el-form-item>
				    </el-form>
				  	<div slot="footer" class="dialog-footer">
					    <el-button @click="dialogFormVisible = false">取 消</el-button>
					    <el-button type="primary" :loading="buttonLoading"  @click="updateDictionary()" >确 定</el-button>
				  	</div>
				</el-dialog>
				<!--新增大类修改 ---END---->
	 		</div>
	 	</div>
 		<div class="addDetail ft">
 			<!--新增明细 ---START---->
 			<div class="detail-wrap resetTable"  v-show='orShowDetail'>
 				<div class="m20 rt" style="padding-bottom: 15px;"><el-button type="primary" @click='showDetail'>新增明细</el-button></div>
 				<el-table
				    :data="detailData"
				    v-loading="loadingTable"
				    border
				    style="width: 100%">
				    <el-table-column
				    	width='70px'
				    	:show-overflow-tooltip="true"
				    	align='center'
				    	label="序号"
	      				type="index">
	    			</el-table-column>
				    <el-table-column
				        prop="name"
				        align='center'
				      	:show-overflow-tooltip="true"
				      	label="明细名称">
				    </el-table-column>
				    <el-table-column
				    	align='center'
				    	:show-overflow-tooltip="true"
				      	prop="price"
				      	label="明细值"
				      	>
				    </el-table-column>
				     <el-table-column
				    	:show-overflow-tooltip="true"
				    	align='center'
				      	prop="detailCode"
				     	label="明细编码">
				    </el-table-column>
				    <el-table-column
				    	:show-overflow-tooltip="true"
				    	align='center'
				      	prop="statusName"
				     	label="状态">
				    </el-table-column>

					<el-table-column
				    	:show-overflow-tooltip="true"
				    	align='center'
				      	prop="reorder"
				     	label="排序">
				    </el-table-column>
				    <el-table-column
				    	:show-overflow-tooltip="true"
				    	align='center'
				      	prop="address"
				      	label="操作">
				      <template slot-scope="scope">
				        <el-button
				        	v-if="scope.row.status==1"
				        	@click.native.prevent="modifyDetData(scope)"
				          	type="text"
				          	size="small">
				          	修改
				        </el-button>
				        <el-button
				          	type="text"
				          	v-if="scope.row.status==1"
				          	size="small"
				          	@click.native.prevent="detailClose(scope,'停用')">
				          	停用
				        </el-button>
				        <el-button
				          	type="text"
				          	size="small"
				          	v-if="scope.row.status==0"
				          	@click.native.prevent="detailOpen(scope,'启用')"
				          	>
				          	启用
				        </el-button>
				      </template>
				    </el-table-column>
				</el-table>
				<div class="com_pad20 alignCen">
		 			<pagination
						:currentPage = 'currentPage_s'
						:total = 'total_s'
						:myPageSizes = 'pageSize2'
						@handleSizeChange = 'handleSizeChange_s'
						@handleCurrentChange = 'handleCurrentChange_s'
		 				>
		 			</pagination>
		 		</div>
				<div>
					<el-dialog :close-on-click-modal ='false' :title="detailTitle" class="dialog-wrap" :visible.sync="detailDialogFormVisible" width='400px' @close="addDiaClose2">
					    <el-form :model="detailForm" ref="detailForm" :rules="detailForm_rules">
						    <el-form-item label="明细名称：" :label-width="formLabelWidth" prop="name2">
						      <el-input  v-model.trim="detailForm.name2" :maxlength='50'></el-input>
						    </el-form-item>
						    <el-form-item label="明细值：" :label-width="formLabelWidth" prop="price2">
						      <!--<el-input v-model.trim="detailForm.price2" auto-complete="off" :maxlength='100'></el-input>-->
						      <el-input v-model.trim="detailForm.price2" auto-complete="off" ></el-input>
						    </el-form-item>
						    <el-form-item label="明细编码：" :label-width="formLabelWidth" prop="detailCode" >
						      <el-input v-model.trim="detailForm.detailCode" auto-complete="off" :disabled='flagDetailCode' :maxlength='20'></el-input>
						    </el-form-item>
						    <el-form-item label="排序：" :label-width="formLabelWidth" prop="index" >
						      <el-input style='width:130px' v-model.trim="detailForm.index" auto-complete="off" :maxlength='20'></el-input>数字越小排序越前
						    </el-form-item>
					    </el-form>

					  	<div slot="footer" class="dialog-footer">
						    <el-button @click="detailDialogFormVisible = false">取 消</el-button>
						    <el-button type="primary" @click="updateModifyDetail(detailForm)" :loading = "loadingDetail">确 定</el-button>
					  	</div>
					</el-dialog>
				</div>
 			</div>
 			<!--新增明细 ---END---->
 		</div>
	</div>
</div>
</template>

<script>
import api from "@/api/index.js";
// import pageSize from "@/api/myPageSize";
import Pagination from "@/components/Pagination";
export default {
  name: "SYP_Dictionary",
  data() {
    return {
      loadingDetail: false,
      buttonLoading: false,
      title: "数据字典",
      tableData3: [],
      currentPage: 1,
      total: 0,
      pageNo: 1,
      pageSize: 10,
      pageSize_s: 10,
      currentPage_s: 1,
      total_s: 0,
      pageNo2: 1,
      pageSize2: 10,
      dialogFormVisible: false,
      innerVisible: false,
      form: {
        name: "",
        price: "",
        code: ""
      },
      form_rules: {
        name: [
          { required: true, message: "请输入大类名称", trigger: "change" }
        ],
        price: [
          { required: true, message: "请输入大类值", trigger: "change" }
        ],
        code: [
          { required: true, message: "请输入编码", trigger: "change" }
        ]
      },
      saveLoading: false,
      formLabelWidth: "100px",
      detailData: [],
      detailDialogFormVisible: false,
      detailForm: {
        name2: "",
        price2: "",
        detailCode: "",
        status: 1,
        index: ""
      },
      detailForm_rules: {
        name2: [
          { required: true, message: "请输入小类名称", trigger: "change" }
        ],
        price2: [
          { required: true, message: "请输入小类值", trigger: "change" }
        ],
        detailCode: [
          { required: true, message: "请输入编码", trigger: "change" }
        ],
        index: [
          { required: true, message: "请输入排序", trigger: "change" }
        ]
      },
      detInnerVisible: false,
      saveDetLoading: false,
      bigClassTitle: "",
      detailTit: "保存",
      detailTitle: "",
      orShowDetail: false,
      maxid: "",
      minid: "", //小类id
      dictionaryId: "", //新增小类时传的大类id
      code: "", //新增小类时传的大类code
      resCode: "",
      glg_id: "",
      glg_code: "",
      flagCode: false,
      flagDetailCode: false,
      loadingTable: false,
      loadingTable1:false,
      search: ""
    };
  },
  created() {
    // if (pageSize.getMyPageSize(this.pageSize)) {
    //   this.pageSize = pageSize.getMyPageSize(this.pageSize);
    // }
  },
  methods: {
    checkSpace() {
    },
    searchDet() {
      this.pageNo = 1
      this.queryPageDictionary();
    },
    addbigClass() {
      this.dialogFormVisible = true;
      this.bigClassTitle = "新增大类";
      this.maxid = "";
      this.flagCode = false;
    },
    modifyData(scope) {
      this.bigClassTitle = "修改大类";
      this.form = Object.assign(this.form, scope.row);
      this.maxid = scope.row.id;
      this.flagCode = true; ////修改时，保存表单时不传code
      this.dialogFormVisible = true;
      //  		console.log(row)
    },
    addDiaClose() {
      Object.assign(this.form, {
        name: "",
        price: "",
        code: ""
      });
      this.$nextTick(() => {
        this.$refs.form.clearValidate();
      });
    },
    addDiaClose2() {
      Object.assign(this.detailForm, {
        name2: "",
        price2: "",
        detailCode: "",
        index: ""
      });
      this.$nextTick(() => {
        this.$refs.detailForm.clearValidate();
      });
    },
    statusReplace(arry) {
      if (arry) {
        for (let i in arry) {
          if (arry[i].status == 1) {
            //	    				arry[i].status = '可用'
            arry[i].statusName = "可用";
          } else if (arry[i].status == 0) {
            //	    				arry[i].status = '停用'
            arry[i].statusName = "停用";
          }
        }
      }
      return arry;
    },
    queryPageDictionary() {
      this.loadingTable1=true;
      api
        .queryPageDictionary({
          pageNo: this.pageNo,
          pageSize: this.pageSize,
          name: this.search
        })
        .then(res => {
          // if (res.data.code == 1) {
            this.loadingTable1=false;
            this.tableData3 = res.data.data;
            this.tableData3 = this.statusReplace(this.tableData3);
            console.log(898999999, this.tableData3);
            this.total = res.data.total;
          // }
        });
    },
    handleSizeChange(val) {
      this.currentPage = 1;
      this.pageNo = 1;
      // let myPageSize = JSON.parse(localStorage.getItem("myPageSize"));
      // myPageSize.W_Dictionary = val;
      // localStorage.setItem("myPageSize", JSON.stringify(myPageSize));
      this.pageSize = val;
      this.queryPageDictionary();
    },
    handleCurrentChange(val) {
      this.pageNo = val;
      this.currentPage = val;
      this.queryPageDictionary();
    },

    updateDictionary() {
      //确认新增,修改大类
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          let pararms = {
            id: this.maxid,
            name: this.form.name,
            code: this.form.code
            //  					price:this.form.price
          };
          if (this.flagCode) {
            //修改时不传pararms.code
            delete pararms.code;
          }
          console.log(pararms, "/////////////");
         
          api.updateDictionary(pararms).then(res => {
            if (res.data.code == 1) {
              this.$message.success(res.data.msg);
              this.dialogFormVisible = false;
              this.queryPageDictionary();
            } else {
              this.$notify({
                title: "提示",
                message: res.data.msg,
                duration: 1500
              });
            }
          });
          this.buttonLoading = false;
        }
      });
    },
    updateDictionaryDetail() {
      // 大类明细值保存
      this.buttonLoading = true;
      console.log(99999, this.detailForm);
      let pararms = {
        id: this.minid,
        dictionaryId: this.dictionaryId,
        name: this.detailForm.name2,
        code: this.code,
        detailCode: this.detailForm.detailCode,
        price: this.detailForm.price2,
        status: this.detailForm.status,
        reorder: this.detailForm.index
      };
      if (this.flagDetailCode) {
        delete pararms.detailCode;
      }
      this.loadingDetail = true
      api.updateDictionaryDetail(pararms).then(res => {
        this.loadingDetail = false
        if (res.data.code == 1) {
          this.$message.success(res.data.msg);
          this.detailDialogFormVisible = false;
          console.log(666666111111);

          api.queryPageDictionaryDetail({
              pageNo: this.pageNo2,
              pageSize: this.pageSize2,
              dictionaryId: this.dictionaryId,
              code: this.code
            })
            .then(res => {
              if (res.data.code == 1) {
                this.detailData = res.data.data;
                this.detailData = this.statusReplace(this.detailData);
                this.queryPageDictionaryDetailFn();
                console.log(666666);
              }
            });
        } else {
          this.$notify({
            title: "提示",
            message: res.data.msg,
            duration: 1500
          });
        }
      });
      // this.buttonLoading = false;
    },
    updateModifyDetail(detailForm) {
      // 大类明细值保存
      this.$refs["detailForm"].validate(valid => {
        if (valid) {
          this.updateDictionaryDetail();
        }
      });
    },
    closeInnerVisible() {
      this.innerVisible = false;
      this.saveLoading = true;
    },
    addDetail(scope) {
      //根据大类id查询小类列表
      this.currentPage_s = 1; // 点击大类的时候都展示该类第一页
      this.pageNo2 = 1;
      console.log(89898, scope);
      this.orShowDetail = true;
      this.glg_id = scope.row.id;
      this.code = scope.row.code;
      this.resCode = scope.row.code;
      this.loadingTable = true;
      api
        .queryPageDictionaryDetail({
          pageNo: this.pageNo2,
          pageSize: this.pageSize2,
          dictionaryId: scope.row.id,
          code: scope.row.code
        })
        .then(res => {
          this.loadingTable = false;
          // if (res.data.code == 1) {
            this.total_s = res.data.total;
            this.detailData = res.data.data;
            this.detailData = this.statusReplace(this.detailData);
            this.dictionaryId = scope.row.id;
            this.code = scope.row.code;
            console.log(this.dictionaryId, "this.dictionaryId");
            console.log(res.data, "this.dictionaryId");
          // }
        });
      //  		this.queryPageDictionaryDetailFn(scope.row.id)
      //this.detailData = scopeds
      //  		this.$refs['detail' +scopeds.$index ].style.display = 'block'
      //document.getElementById('detail' + scopeds.$index).style.display = 'block'
      //  		console.log(this.$ref['detail' +scopeds.$index ])
    },
    queryPageDictionaryDetailFn(id) {
      api
        .queryPageDictionaryDetail({
          pageNo: this.pageNo2,
          pageSize: this.pageSize2,
          dictionaryId: this.glg_id,
          code: this.code
        })
        .then(res => {
          // if (res.data.code == 1) {
            this.total_s = res.data.total;
            this.detailData = res.data.data;
            this.detailData = this.statusReplace(this.detailData);
            //					this.dictionaryId = scope.row.id
            console.log(this.dictionaryId, "this.dictionaryId");
            console.log(res.data, "this.dictionaryId");
            console.log(this.dictionaryId == this.glg_id, "this.dictionaryId");
          // }
        });
    },
    handleSizeChange_s(val) {
      this.currentPage_s = 1;
      this.pageNo2 = 1;
      // pageSize.setMyPageSize(val);
      // let myPageSize = JSON.parse(localStorage.getItem('myPageSize'))
      // myPageSize.W_DictionaryDet = val
      // localStorage.setItem('myPageSize',JSON.stringify(myPageSize))
      this.pageSize2 = val;
      this.queryPageDictionaryDetailFn();
    },
    handleCurrentChange_s(val) {
      this.pageNo2 = val;
      this.currentPage_s = val;
      this.queryPageDictionaryDetailFn();
    },
    showDetail() {
      //新增明细
      this.detailTit = "保存";
      this.detailTitle = "新增明细";
      this.detailDialogFormVisible = true;
      this.minid = "";
      this.flagDetailCode = false;
      this.detailForm.status = 1;
      this.code = this.resCode;

      console.log(12313122332);
      console.log(this.dictionaryId, "this.dictionaryId");
    },
    closeDetDialogFormVisible() {
      this.detInnerVisible = true;
    },
    closeDetInnerVisible() {
      this.detInnerVisible = false;
      this.saveDetLoading = false;
      this.detailDialogFormVisible = false;
    },
    detailClose(scope, b) {
      //停用
      this.minid = scope.row.id;
      this.dictionaryId = scope.row.dictionaryId;
      this.code = null;
      this.$confirm("确认停用?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        type: "warning"
      }).then(() => {
        this.detailForm.status = 0;
        this.updateDictionaryDetail();
      });
    },
    detailOpen(scope, b) {
      //启用
      this.minid = scope.row.id;
      this.dictionaryId = scope.row.dictionaryId;
      this.code = null;
      this.$confirm("确认启用?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        closeOnClickModal: false
      }).then(() => {
        this.detailForm.status = 1;
        this.updateDictionaryDetail();
      });
    },
    modifyDetData(scope, b) {
      //this.detailForm.name = scope.row.name
      //this.detailForm.desc = scope.row.address
      this.flagDetailCode = true;
      //  		console.log(this.flagDetailCode,'this.flagDetailCode')
      this.detailForm = {
        detailCode: scope.row.detailCode,
        dictionaryId: scope.row.dictionaryId,
        code: null,
        id: scope.row.id,
        name2: scope.row.name,
        price2: scope.row.price,
        status: scope.row.status,
        index: scope.row.reorder
      };
      this.minid = scope.row.id;
      this.dictionaryId = scope.row.dictionaryId;
      this.code = null;
      if (b == "停用") {
        this.detailForm.status = 0;
      } else {
        this.detailForm.status = 1;
      }
      this.detailDialogFormVisible = true;
      this.detailTit = "修改";
      this.detailTitle = "修改明细";
    }
  },
  components: {
    //	  	TitCommon,
    Pagination
  },
  mounted() {
    this.queryPageDictionary();
  }
};
</script>
<style  lang='less'>
.dictionary-wrap {
  .dictionary-con {
    padding-top: 20px;
    .tit-con {
      color: #000;
      font-size: 16px;
      font-weight: 700;
      font-style: italic;
    }
    .tables {
      padding-top: 15px;
    }
    .dialog-wrap {
      .el-input {
        width: 260px;
      }
    }
  }
  .detail-wrap {
    /*display: none;*/
    .m20 {
      padding: 20px 0;
    }
  }
  .addDetail {
    width: 59%;
    margin-left: 1%;
  }
  .dictionary-con {
    width: 40%;
  }
}
</style>
