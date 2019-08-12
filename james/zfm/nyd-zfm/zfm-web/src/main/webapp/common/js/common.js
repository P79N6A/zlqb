var common = {
    basePath: function () {
        var localObj = window.location;
        var contextPath = localObj.pathname.split("/")[1];
        var basePath = localObj.protocol + "//" + localObj.host + "/" + contextPath;
        return basePath
    },
    jqalert: function (param) { // 弹框
        var title = param.title,
            content = param.content,
            yesfn = param.yesfn,
            nofn = param.nofn;
        var htm = '';
        htm += '<div class="jq-alert" id="jq-alert"><div class="alert">';
        if (title) htm += '<h2 class="title">' + title + '</h2>';
        if (content) {
            htm += '<div class="content">' + content + '</div>';
        }
        var y = '';
        htm += '<div class="fd-btn">'
        
        if (nofn) {
            y += '<a href="javascript:void(0);" class="confirm" id="no_btn">' + nofn + '</a>'            
        }
        if (yesfn) {
        	y += '<a href="javascript:void(0);" class="confirm" id="yes_btn">' + yesfn + '</a>'
         
        } 
        htm +=y+ '</div></div>';
        $('body').append(htm);
    },
}