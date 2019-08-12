class Dialog{
  constructor(){}
  alert (message = '' , opts = {}){
    let opt = {
      message:  message,
      title: '提示',            //头部名称
      type : '',                //图标颜色 warning、info、error、success
      showCancelButton: true,   //是否显示取消按钮
      showConfirmButton: true,  //是否显示确定按钮
      confirm : false,          //你确定要删除吗？
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      callback: function(){},          //确定按钮名称
    }
    Object.assign(opt, opts);
    if( opt.confirm ){//confirm
      opt.type = 'warning';
      window.VM__.$confirm(opt.message, opt.title, opt);
    }else{//alert
      window.VM__.$alert(opt.message, opt.title, opt);
    }
  }
  message (message = '' , type = 'info'){
    window.VM__.$message({type : type , message : message});
  }
}
export default Dialog;
/*！
使用：
open1() {
  	var self = this;
  	Dialog.alert('你确定要删除吗？',{
    confirm : true,
    callback : function( txt ){
      	if( txt === 'confirm'){//点击确定按钮
        	Dialog.message('删除成功!','success');
      	}
    }
  	});
},
open() {
  	Dialog.alert('这是alert',{
    showConfirmButton : false,
    cancelButtonText : "abc",
    callback : function( txt ){
      	if( txt === 'confirm'){//点击确定按钮
        	Dialog.message("这是message"+`${ txt }`,'error');
      	}
    }
});
*/
