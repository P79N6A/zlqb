var basePath = $("#basePath").val();
var custInfoId = $("#custInfoId").val();
var vm = new Vue({
    el: '#appDet',
    data: {
        pageSize: 10,
        pageNo: 1,
        items: [],
        options: {
            pullDownRefresh: {
                threshold: 90,
                stop: 40,
                txt: '刷新成功'
            },
            pullUpLoad: {
                threshold: 0,
                txt: {
                    more: '往上拉，加载更多',
                    noMore: '~我是有底线的~'
                }
            }
        },
    },
    mounted() {
        this.queryQuestionList();
    },
    methods: {
        onPullingDown() {
            // Mock async load.
            setTimeout(() => {
                this.pageNo = 1;
                this.items = [];
                this.queryQuestionList();
            }, 1000)
        },
        onPullingUp() {
            // Mock async load.
            var _this = this;
            setTimeout(function () {
                _this.pageNo = _this.pageNo + 1;
                _this.queryQuestionList();
            }, 1000)
        },
        queryQuestionList() {
            var params = {
                pageNo: this.pageNo,
                pageSize: this.pageSize,
                custInfoId: custInfoId,
                serialInfo: "true"
            };
            var _this = this;
            $.ajax({
                type: 'post',
                url: basePath + "/web/mobile/activity/invite/rewardSerialList",
                data: params,
                success: function (data) {
                    if (data.code == 1 && data.data.length != 0) {
                        _this.items = _this.items.concat(data.data);
                    } else if(data.code != 1){
                    	$('.totast_cre').html(data.msg).show();
                        setTimeout(function(){
                            $('.totast_cre').hide();
                            window.history.back();
                        },1500);
                    } else{
                    	_this.$refs.scroll.forceUpdate();
                    }
                }
            });
        },
    }
})