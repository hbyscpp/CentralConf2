/**
 * 更改dialog标题
 */
jQuery.chgDialogTitle = function(id, title) {
	$("div[aria-describedby='" + id + "']").find(".ui-dialog-titlebar").find("span.ui-dialog-title").find("h4").find("span").text(title);
};
/**
 * alert提示
 */
jQuery.smartAlert = function(params) {
	if(params instanceof Object) {
		var content = params.content;
		$.smallBox({
			title : "提示",
			content : content,
			color : "#296191",
			iconSmall : "fa fa-bell bounce animated",
			timeout : 4000
		});
	} else {
		alert("参数不是对象");
	}
};
/**
 * 成功提示
 */
jQuery.smartSuccess = function(params) {
	if(params instanceof Object) {
		var content = params.content;
		$.smallBox({
			title : "提示",
			content : content,
			color : "rgb(115, 158, 115)",
			iconSmall : "fa fa-check bounce animated",
			timeout : 4000
		});
	} else {
		alert("参数不是对象");
	}
};
/**
 * 失败提示
 */
jQuery.smartFailure = function(params) {
	if(params instanceof Object) {
		var content = params.content;
		$.smallBox({
			title : "提示",
			content : content,
			color : "rgb(196, 106, 105)",
			iconSmall : "fa fa-times bounce animated",
			timeout : 4000
		});
	} else {
		alert("参数不是对象");
	}
};
/**
 * 格式化时间
 */
jQuery.formatDate = function(params) {
	var date = params.date;//为空是今天
	var format = params.format;//格式
	if(date) {
		tempDate = new Date(date);
	} else {
		tempDate = new Date();
	}
	console.log(tempDate + "-------" + format);
	var o = {   
		"M+" : tempDate.getMonth()+1,                 //月份   
		"d+" : tempDate.getDate(),                    //日   
		"h+" : tempDate.getHours(),                   //小时   
		"m+" : tempDate.getMinutes(),                 //分   
		"s+" : tempDate.getSeconds(),                 //秒   
		"q+" : Math.floor((tempDate.getMonth()+3)/3), //季度   
		"S"  : tempDate.getMilliseconds()             //毫秒   
	};   
	if(/(y+)/.test(format))   
		format = format.replace(RegExp.$1, (tempDate.getFullYear()+"").substr(4 - RegExp.$1.length));   
	for(var k in o) {
		if(new RegExp("("+ k +")").test(format))   
			format = format.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
	}
	console.log(format);
	return format;
};
/**
 * 请求远程页面的弹窗
 */
jQuery.remoteDialog = function(params) {
	if(params instanceof Object) {
		var width = params.width;
		var height = params.height;
		var title = params.title;
		var url = params.url;
		$.ajax({
			url: url,
			type: "get",
			dataType: "html",
			async: false,
			success: function(html){
				$(html).dialog({
					autoOpen : true,
					width : width,
					height: height,
					resizable : false,
					modal : true
				});
				$(html).dialog("open");
			}
		});
	} else {
		alert("参数不是对象");
	}
};