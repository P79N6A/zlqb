webpackJsonp([9],{"26OP":function(e,t){},EIeK:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=a("4YfN"),s=a.n(i),o=(a("BW2B"),a("cMGX")),l={props:{treeData:{type:Array,default:function(){return[]}},id:{type:String,default:"id"},showCheckbox:{type:Boolean,default:!0},defaultProps:{type:Object,default:function(){return{children:"children",label:"name"}}},checkedArr:{type:Array,default:function(){return[]}}},data:function(){return{}},mounted:function(){},methods:{handleNodeClick:function(e){this.$emit("handleNodeClick",e)}}},n={render:function(){var e=this.$createElement,t=this._self._c||e;return t("div",{staticClass:"treeData"},[t("el-tree",{ref:"tree",attrs:{data:this.treeData,"node-key":this.id,props:this.defaultProps,"default-checked-keys":this.checkedArr,"show-checkbox":""},on:{"node-click":this.handleNodeClick}})],1)},staticRenderFns:[]};var r=a("C7Lr")(l,n,!1,function(e){a("hQqS")},"data-v-3f44d5fa",null).exports,d=a("2bvH"),c=a("gyMJ"),u={data:function(){return{treeData:[],checkedArr:[],defaultProps:{children:"childrenModule",label:"name"},search:"",tableData:[],alertTitle:"",currentPage:1,total:0,pageNo:1,pageSize:10,title:"新增角色",dialogFormVisible:!1,centerDialogVisible:!1,showCancel:!1,ruleForm:{name:""},formLabelWidth:"80px",rules:{name:[{required:!0,message:"请输入角色名",trigger:"blur"},{min:1,max:20,message:"请输入1~20个字符",trigger:"blur"}]},type:"",loadingTable:!1,rowId:"",loadingbtn:!1,loadingTree:!1}},mounted:function(){this.sysRolelist()},computed:{},methods:s()({saveTree:function(){var e=this,t={pageNo:1,pageSize:1e4,sysModuleIds:this.$refs.tree.$refs.tree.getCheckedKeys().join(",")||"",id:this.rowId};this.loadingTree=!0,c.a.sysUpdateRole(t).then(function(t){setTimeout(function(){e.loadingTree=!1},1500),e.centerDialogVisible=!1,e.sysRolelist(),e.updateRolerMenuList({loginUserName:JSON.parse(localStorage.getItem("user_data")).loginName}),t.data.success?e.$notify({title:"提示",type:"success",message:t.data.msg,duration:1500}):e.$notify({title:"提示",type:"error",message:t.data.msg,duration:1500})})},menuSet:function(e){this.sysGetModuleListByRoleId(e.id)},sysGetModuleListByRoleId:function(e){var t=this;this.rowId=e,c.a.sysGetModuleListByRoleId({roleId:e}).then(function(e){t.centerDialogVisible=!0,e.data.success?(t.treeData=e.data.data.moduleList,t.checkedArr=e.data.data.moduleIds):t.$notify({title:"提示",message:e.data.msg,duration:1500})})},searchFn:function(){this.pageNo=1,this.pageSize=10,this.currentPage=1,this.sysRolelist()},sysRolelist:function(){var e=this;this.loadingTable=!0;var t={pageNo:this.pageNo,pageSize:this.pageSize,name:this.search};c.a.sysRolelist(t).then(function(t){e.loadingTable=!1,t.data.success&&null!=t.data.data?(e.tableData=t.data.data,e.total=t.data.total):e.$notify({title:"提示",message:t.data.msg,duration:1500})})},sysAddRole:function(){var e=this,t={pageNo:1,pageSize:1e4,name:this.ruleForm.name};this.loadingbtn=!0,c.a.sysAddRole(t).then(function(t){setTimeout(function(){e.loadingbtn=!1},400),t.data.success?(e.dialogFormVisible=!1,e.$refs.ruleForm.resetFields(),e.sysRolelist()):e.$notify({title:"提示",message:t.data.msg,duration:400})})},sysUpdateRole:function(){var e=this,t={pageNo:1,pageSize:1e4,name:this.ruleForm.name,id:this.ruleForm.id};this.loadingbtn=!0,c.a.sysUpdateRole(t).then(function(t){e.sysRolelist(),setTimeout(function(){e.loadingbtn=!1},400),t.data.success?e.dialogFormVisible=!1:e.$notify({title:"提示",message:t.data.msg,duration:1500})})},sysRemoveRole:function(e){var t=this;c.a.sysRemoveRole({roleId:e}).then(function(e){t.sysRolelist(),t.$notify({title:"提示",message:e.data.msg,duration:1500})})},celFn:function(){this.centerDialogVisible=!1,this.$refs.tree.$refs.tree.setCheckedKeys([])},sysRemoveUser:function(e){var t=this;this.$confirm("确定要删除角色吗？","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning",closeOnClickModal:!1}).then(function(){t.sysRemoveRole(e.id)}).catch(function(){})},addInformain:function(e,t){if(this.dialogFormVisible=!0,this.type=e,"modify"==e)return this.title="修改角色",void(this.ruleForm={name:t.name,id:t.id});"add"==e&&(this.title="新增角色",this.ruleForm={name:"",id:""})},submitForm:function(e){var t=this;this.$refs[e].validate(function(e){if(!e)return!1;"add"==t.type&&t.sysAddRole(),"modify"==t.type&&t.sysUpdateRole()})},resetForm:function(e){this.dialogFormVisible=!1,this.ruleForm={name:""},this.$refs[e].resetFields()},handleCheckedCitiesChange:function(e){},handleSizeChange:function(e){this.currentPage=1,this.pageNo=1,this.pageSize=e,this.sysRolelist()},handleCurrentChange:function(e){this.pageNo=e,this.currentPage=e,this.sysRolelist()}},Object(d.b)({updateRolerMenuList:"UPDATE_ROLE_MENU_LIST"})),watch:{},components:{Pagination:o.a,Tree:r}},m={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"systerm_user_manger"},[a("div",{staticClass:"com_pad10_20 user_manger_search com_1px"},[a("el-form",{staticClass:"form_set_bot"},[a("el-form-item",{attrs:{label:"角色名：",prop:"name"}},[a("el-input",{staticClass:"wid200",attrs:{placeholder:"请输入角色名",clearable:""},model:{value:e.search,callback:function(t){e.search=t},expression:"search"}}),e._v(" "),a("el-button",{staticClass:"marrt20",attrs:{type:"primary",size:"medium"},on:{click:e.searchFn}},[e._v("查询")]),e._v(" "),a("el-button",{staticClass:"marrt20",attrs:{type:"primary",size:"medium"},on:{click:function(t){return e.addInformain("add")}}},[e._v("新增")])],1)],1)],1),e._v(" "),a("div",{staticClass:"table_list"},[a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loadingTable,expression:"loadingTable"}],staticStyle:{width:"100%"},attrs:{data:e.tableData,border:""}},[a("el-table-column",{attrs:{align:"center",type:"index",width:"50",label:"序号"}}),e._v(" "),a("el-table-column",{attrs:{align:"center",prop:"name",label:"角色名","show-overflow-tooltip":""}}),e._v(" "),a("el-table-column",{attrs:{align:"center",prop:"createTime",label:"创建时间","show-overflow-tooltip":""}}),e._v(" "),a("el-table-column",{attrs:{align:"center",prop:"updateTime",label:"更新时间","show-overflow-tooltip":""}}),e._v(" "),a("el-table-column",{attrs:{align:"center",prop:"address",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",disabled:1==t.row.defaultFlag,size:"small"},on:{click:function(a){return e.addInformain("modify",t.row)}}},[e._v("修改")]),e._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){return e.menuSet(t.row)}}},[e._v("菜单配置")]),e._v(" "),a("el-button",{attrs:{disabled:1==t.row.defaultFlag,type:"text",size:"small"},on:{click:function(a){return e.sysRemoveUser(t.row)}}},[e._v("删除")])]}}])})],1)],1),e._v(" "),a("div",{staticClass:"pagination_wrap com_cen"},[a("Pagination",{attrs:{currentPage:e.currentPage,total:e.total,myPageSizes:e.pageSize},on:{handleSizeChange:e.handleSizeChange,handleCurrentChange:e.handleCurrentChange}})],1),e._v(" "),a("el-dialog",{attrs:{title:e.title,visible:e.dialogFormVisible,"close-on-click-modal":!1,"show-close":!1,width:"600px"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[a("el-form",{ref:"ruleForm",attrs:{model:e.ruleForm,rules:e.rules}},[a("el-form-item",{attrs:{label:"角色名：","label-width":e.formLabelWidth,prop:"name"}},[a("el-input",{attrs:{"auto-complete":"off",placeholder:"请输入角色名",minlength:"1",maxlength:"20"},model:{value:e.ruleForm.name,callback:function(t){e.$set(e.ruleForm,"name",t)},expression:"ruleForm.name"}})],1)],1),e._v(" "),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{size:"medium"},on:{click:function(t){return e.resetForm("ruleForm")}}},[e._v("取 消")]),e._v(" "),a("el-button",{attrs:{loading:e.loadingbtn,size:"medium",type:"primary"},on:{click:function(t){return e.submitForm("ruleForm")}}},[e._v("确 定")])],1)],1),e._v(" "),a("el-dialog",{attrs:{title:"角色菜单配置",visible:e.centerDialogVisible,"close-on-click-modal":!1,width:"600px"},on:{"update:visible":function(t){e.centerDialogVisible=t}}},[a("div",[a("Tree",{ref:"tree",attrs:{treeData:e.treeData,checkedArr:e.checkedArr,defaultProps:e.defaultProps}})],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{size:"medium"},on:{click:e.celFn}},[e._v("取 消")]),e._v(" "),a("el-button",{attrs:{type:"primary",size:"medium",loading:e.loadingTree},on:{click:e.saveTree}},[e._v("确 定")])],1)])],1)},staticRenderFns:[]};var h=a("C7Lr")(u,m,!1,function(e){a("26OP")},"data-v-5595a888",null);t.default=h.exports},hQqS:function(e,t){}});