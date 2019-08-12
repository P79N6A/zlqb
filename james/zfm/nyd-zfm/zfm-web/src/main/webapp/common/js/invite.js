var basePath = $("#basePath").val();
var custInfoId = $("#custInfoId").val();
var activityStatus = $("#activityStatus").val();

var invite = {
    init: function() {
        this.tabFn();
        this.qrcode();
        this.copyBtn();
        this.checkRewardWithdraw();
    },
    qrcode: function() { // 生成二维码
        var qrcode = new QRCode('qrcode');
        qrcode.makeCode($('#addressLink').text());
    },   
    copyBtn: function() { // 复制链接
        
        $('#copyBtn').click(function() {
            var clipboard = new ClipboardJS('.copyBtn', {
                target: function() {
                    return document.getElementById('addressLink');
                }
            });
            clipboard.on('success', function (e) {
                $('.totast_cre').html('复制成功').fadeIn('1500');
                setTimeout(function(){
                    $('.totast_cre').hide()
                },1500)
            });
            clipboard.on('error', function (e) {
                $('.totast_cre').html('你的手机不支持的复制功能').fadeOut('1500')
                setTimeout(function(){
                    $('.totast_cre').hide()
                },1500)
            });
        })
    },
    tabFn: function() { // tab切换
        $('.invest_tab ul li').click(function(){
            var index = $(this).attr('data-index');
            var indexSiblings = $($(this).siblings()).attr('data-index');
            if( index == 0 && activityStatus == 0){
            	common.jqalert({
            	    title: '提示',
            	    yesfn: '确定',
            	    content: '活动已结束'
            	});
            	$("#yes_btn").click(function(){
            	    $('#jq-alert').fadeOut(function () {
            	      $("#jq-alert").remove()
            	    });
            	});
            	return;
            }
            $(this).addClass('active');
            $($(this).siblings()).removeClass('active');
            $('.part_com_tab').eq(index).css({
                display: 'block'
            });
            $('.part_com_tab').eq(indexSiblings).css({
                display: 'none'
            });
           
        })
    },
    /**
     * 点击奖励金提现数据校验事件
     * taohui
     */
    checkRewardWithdraw: function(){
    	//点击奖励金提现
    	$(".withdraw_btn").click(function(){
    		var params = {
              custInfoId: custInfoId
            };
    		$(".withdraw_btn").attr('disabled','disabled');
    		//提现校验数据
            $.ajax({
                type: 'post',
                url: basePath + "/web/mobile/activity/invite/checkRewardWithdraw",
                data: params,
                success: function (data) {
                    if(data.code == "A0009" || data.code == "1"){
                    	var yesfnStr = "去绑定";
                    	if(data.code == "1"){
                    		yesfnStr = "确定";
                    	}
                    	// 请先绑定银行卡
                    	common.jqalert({
                    	    title: '提示',
                    	    yesfn: yesfnStr,
                    	    content: data.msg,
                    	    nofn: '取消'
                    	})
                    	$("#no_btn").click(function(){
                    	    $('#jq-alert').fadeOut(function () {
                    	      $("#jq-alert").remove()
                    	    });
                    	    $(".withdraw_btn").removeAttr('disabled','disabled');
                    	})
	                	$("#yes_btn").click(function(){
	                		// 成功
	                		if(data.code == "1"){
	                			$("#jq-alert").remove();
	                			setTimeout("invite.rewardWithdraw()", 2000 );
	                		}else{
	                			$(".withdraw_btn").removeAttr('disabled','disabled');
	                			window.location.href=basePath+"/web/account/toOpenPage?custInfoId="+custInfoId+"&type=1";
	                			$('#jq-alert').fadeOut(function () {
		   	                	      $("#jq-alert").remove()
		   	                	 });
	                		}
	                	})
                    } else {
                    	// 失败
                    	common.jqalert({
                    	    title: '提示',
                    	    yesfn: '确定',
                    	    content: data.msg
                    	});
                    	$("#yes_btn").click(function(){
                    		$(".withdraw_btn").removeAttr('disabled','disabled');
                    	    $('#jq-alert').fadeOut(function () {
                    	      $("#jq-alert").remove()
                    	    });
                    	});
                    }
                	
                }
            });
    	});
    },
    /**
     * 奖励金提现事件
     * taohui
     */
    rewardWithdraw: function(){
    	var params = {
                custInfoId: custInfoId
              };
    	$.ajax({
            type: 'post',
            url: basePath + "/web/mobile/activity/invite/rewardWithdraw",
            data: params,
            success: function (data) {
                if(data.code == "A0009"){
                	// 请先绑定银行卡
                	common.jqalert({
                	    title: '提示',
                	    yesfn: '去绑定',
                	    content: data.msg,
                	    nofn: '取消'
                	})
                	$("#no_btn").click(function(){
                		$(".withdraw_btn").removeAttr('disabled','disabled');
                	    $('#jq-alert').fadeOut(function () {
                	      $("#jq-alert").remove()
                	    });
                	})
                	$("#yes_btn").click(function(){
                		$(".withdraw_btn").removeAttr('disabled','disabled');
                		window.location.href=basePath+"/web/account/toOpenPage?custInfoId="+custInfoId+"&type=1";
                	    $('#jq-alert').fadeOut(function () {
                	      $("#jq-alert").remove()
                	    });
                	})
                } else {
                	// 失败
                	common.jqalert({
                	    title: '提示',
                	    yesfn: '确定',
                	    content: data.msg
                	});
                	$("#yes_btn").click(function(){
                		if(data.code == "1"){
                			window.location.reload();
                		}
                	    $('#jq-alert').fadeOut(function () {
                	      $(".withdraw_btn").removeAttr('disabled','disabled');
                	      $("#jq-alert").remove()
                	    });
                	});
                }
            }
        });
    }
}
$(function(){
    invite.init();
})