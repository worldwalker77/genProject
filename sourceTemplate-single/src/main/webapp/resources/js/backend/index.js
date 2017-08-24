$(function() {
    $("#myTabContent iframe").attr("width", $("nav").width());
    window.onresize = function () {
    	$("#myTabContent iframe").attr("width", $("nav").width());
    };
    $('#myTab li').click(function (e) {
       $(this).addClass("active").siblings().removeClass('active');
       var hrefStr = $($(this).children()[0]).attr("href");
       if("#client-version-controll" == hrefStr){
    	   $("#myTabContent iframe").attr("src", contextPath + "/backend/versioncontroll");
       }else if("#biling-detail" == hrefStr){
    	   $("#myTabContent iframe").attr("src", contextPath + "/backend/versioncontroll");
       }else if("#member-query" == hrefStr){
    	   $("#myTabContent iframe").attr("src", contextPath + "/backend/versioncontroll");
       }else if("#with-drawal-record" == hrefStr){
    	   $("#myTabContent iframe").attr("src", contextPath + "/backend/versioncontroll");
       }else if("#client-status-monitor" == hrefStr){
    	   $("#myTabContent iframe").attr("src", contextPath + "/backend/versioncontroll");
       }else{
    	   $("#myTabContent iframe").attr("src", contextPath + "/backend/versioncontroll");
       };
    });
    //设置iframe高度，高度为浏览器当前窗口可视区域高度-nav导航条高度-tab标签页高度
    var height = $(window).height() - $("#id-nav").height() - $("#myTab").height();
    $("#iframepage").height(height);
    
});

function say(height){
//	$("#iframepage").height(height);
//	iframepage.window.say();
}

