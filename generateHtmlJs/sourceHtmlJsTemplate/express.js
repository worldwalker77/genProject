//全局变量
//页面表格对象
var table = $('.table.table-striped:eq(0)');

// 加载表格数据
$(function() {
	ExpressManager.loadTable();
	
	// 查询按钮点击事件,refresh方法存在bug，无法从第一页开始
	$('#searchBtn').click(function() {
		ExpressManager.search();
	});
	
	//编辑提交按钮
	$('#editCityBtn').click(function() {
		ExpressManager.update();
	});
	//新增按钮
	$('#showAddBtn').click(function() {
		ExpressManager.showAddModal();
	});
	
});

//快递公司管理

var ExpressManager = {
	//查询
	search : function(){
		var res = table.bootstrapTable('getData').length;
		if(res > 0){
			ExpressManager.selectPage();			
		}else{
			ExpressManager.refresh();					
		}
	},
	//查询起始页归1
	selectPage : function() {
		table.bootstrapTable('selectPage', 1);
	},
	//刷新表格数据
	refresh : function() {
		table.bootstrapTable('refresh');
	},
	//加载表格
	loadTable : function(){
		table.bootstrapTable({
			method : 'post',
			url : contextPath + '/basic/express/list',
			contentType : 'application/x-www-form-urlencoded',
			showColumns : true,
			toolbar : '#toolbar',
			minimumCountColumns : 6, // 设置最少显示列个数
			idField : 'pkid',
			queryParams : ExpressManager.queryParams,
			responseHandler : ExpressManager.responseHandler,

			columns : [ {
				field : 'pkid',
				title : '公司编号'
			}, {
				field : 'expName',
				title : '公司名称'
			}, {
				field : 'expPhone',
				title : '公司电话'
			}, {
				field : 'expWebsite',
				title : '公司网址',
				formatter : ExpressManager.webFormatter
			}, {
				field : 'expCode',
				titleTooltip : '重要数据，不确定情况下，不要修改!',
				formatter : ExpressManager.codeFormatter,
				title : '公司代码'
			}, {
				field : 'status',
				formatter : ExpressManager.statusFormatter,
				title : '状态'
			}, {
				field : 'lastModifyedTime',
				title : '最后修改时间',
				visible : false
			}, {
				formatter : ExpressManager.operationFormatter,
				events : ExpressManager.operateEvents,
				title : '操作'
			} ]
		});
	},
	//查询请求参数
	queryParams : function (params) {
		var dto = {};
		dto.expName = $.trim($("#expName").val());
		dto.expPhone = $.trim($("#expPhone").val());
		dto.expCode = $.trim($("#expCode").val()).toLowerCase();

		// pageSize改变，从第一页开始
		if (currentPageSize != params.limit) {
			// 设置从起始页开始
			table.bootstrapTable('getOptions').pageNumber = 1;
			// 必须设置
			params.offset = 0;
		}
		currentPageSize = params.limit;
		dto.start = params.offset;
		dto.end = params.offset + params.limit;
		return dto;
	},
	//表格回调函数
	responseHandler : function (res) {
		if (res.success) {
			return {
				"rows" : res.rows,
				"total" : res.total
			};
		} else {
			return {
				"rows" : [],
				"total" : 0
			};
		}
	},
	// 快递公司名称格式化
	codeFormatter: function (value, row, index) {
		return ['<strong class="text-success">',value,'</strong>'].join('');
	},
	// 状态格式化
	statusFormatter : function (value, row, index) {
		var text = value == 1 ? '启用' : '禁用';
		var color = value == 1 ? 'label-success' : 'label-danger';
		return [ '<span class="label ', color, '">', text, '</span>' ].join('');
	},
	// 网站格式化
	webFormatter : function (value, row, index) {
		return [ '<a href="', value, '" target="_blank">', value, '</a>' ].join('');
	},
	update : function() {
		var dto = {
			pkid : $('#pkid').val(),
			expName : $.trim($('#editExpName').val()),
			expPhone : $.trim($('#editExpPhone').val()),
			expCode : $.trim($('#editExpCode').val()),
			expWebsite : $.trim($('#editExpSite').val())
		};
		if (ExpressManager.valid()) {
			var url = contextPath + "/basic/express/addExp";
			if (dto.pkid) {
				url = contextPath + "/basic/express/updateExp";
			}
			$.ajax({
				url : url,
				type : "POST",
				dataType : "json",
				data : dto,
				success : function(data) {
					if(data.result == -1){
						ExpressManager.dangerAlert();
					}else if(data.result == 2){
						ExpressManager.uniqueAlert();
					}else{
						$('#editExp').modal('hide');
						ExpressManager.successAlert();
						ExpressManager.refresh();			
					}
				}
			});
		}
	},
	showAddModal : function(){
		$('#pkid').val('');
		$('#editExpError').html('注意：带<font color="red">*</font>为必填项');	
		$('#editExpForm')[0].reset();
		$('#editExp').modal('show');
	},
	dangerAlert : function(){
		qiao.bs.alert({
			backdrop : true,
			msg : '<strong class="text-danger">快递公司操作失败！</strong>',
			mstyle : 'width:300px;'
		});
	},
	successAlert : function(){
		qiao.bs.alert({
			backdrop : true,
			msg : '<strong class="text-success">快递公司操作成功！</strong>',
			mstyle : 'width:300px;'
		});
	},
	uniqueAlert : function(){
		qiao.bs.alert({
			backdrop : true,
			msg : '<strong class="text-danger">公司名称已存在！</strong>',
			mstyle : 'width:300px;'
		});
	},
	// 操作列
	operationFormatter :function (value, row, index) {
		var text = row.status == 0 ? "启用" : "禁用";
		var color = row.status == 0 ? 'btn-success' : 'btn-danger';
		return [ '<div class="btn-group btn-group-xs">',
				 '<button type="button" class="btn btn-primary btn-xs editBtn">',
				 '<i class="glyphicon glyphicon-pencil"></i> 编辑</button>',
				 '<button type="button" class="btn ', color, ' btn-xs statusBtn">',
				 '<i class="glyphicon glyphicon-cog"></i> ', text, '</button>',
				 '</div>' ].join('');
	},
	operateEvents : {
		'click .editBtn' : function(e, value, row, index) {
			$("#pkid").val(row.pkid);
			$("#editExpName").val(row.expName);
			$("#editExpPhone").val(row.expPhone);
			$("#editExpCode").val(row.expCode);
			$("#editExpSite").val(row.expWebsite);
			$('#editExpError').html('注意：带<font color="red">*</font>为必填项');
			$("#editExp").modal('show');
		},
		'click .statusBtn' : function(e, value, row, index) {
			var status = row.status;
			var color = status == 0 ? "text-success":"text-danger";
			var option = status == 0 ? "启用":"禁用";
			var label = status == 0 ? "label-success":"label-danger";
			var text = ['<strong class="',color,'">确认要',option,'快递公司<span class="label ',label,'"> ',row.expName,' </span>吗?</strong>'].join('');
			var options = {
				id : 'modStatus',
				msg : text,
				backdrop : true,
				mstyle : 'width:400px;'
			}
			qiao.bs.confirm(options, function() {
				var url = contextPath + '/basic/express/modStatus';
				var params = {
					'pkid' : row.pkid,
					'status' : status == 0 ? 1 : 0
				};
				$.post(url, params, function(data) {
					if (data.result == 1) {
						ExpressManager.successAlert();
						ExpressManager.refresh();			
					} else {
						ExpressManager.dangerAlert();
					}
				}, 'json');
			});
		}
	},
	valid : function () {
		var expName = $.trim($("#editExpName").val());
		var expPhone = $.trim($("#editExpPhone").val());
		var expCode = $.trim($("#editExpCode").val()).toLowerCase();
		var expWebsite = $.trim($("#editExpSite").val()).toLowerCase();
		if (expName == null || !validChars(expName.replace(/[-]/g,''))) {
			$('#editExpError').text("提示：公司名称为1-15位中文，字母，数字或-");
			return false;
		}
		if (expPhone == null || !validServerPhone(expPhone.replace(/[-]/g,''))) {
			$('#editExpError').text("提示：公司电话为5-20位数字和-组成");
			return false;
		}
		if (expCode == null || !validCode(expCode.replace(/[-]/g,''))) {
			$('#editExpError').text("提示：公司代码为1-15位数字，字母下划线或-");
			return false;
		}
		if (expWebsite == null || !validHttp(expWebsite)) {
			$('#editExpError').text("提示：公司网址为http://或https://开头网址");
			return false;
		}
		return true;
	}
}