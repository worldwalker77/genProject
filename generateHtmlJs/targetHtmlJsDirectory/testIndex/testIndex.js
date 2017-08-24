//全局变量
//页面表格对象
var table = $('.table.table-striped:eq(0)');

// 加载表格数据
$(function() {
	Manager.loadTable();
	
	// 查询按钮点击事件,refresh方法存在bug，无法从第一页开始
	$('#searchBtn').click(function() {
		Manager.search();
	});
	
	//编辑或新增提交按钮
	$('#editSumitBtn').click(function() {
		Manager.update();
	});
	//新增按钮
	$('#addBtn').click(function() {
		Manager.showAddModal();
	});
	
});

//快递公司管理

var Manager = {
	//查询
	search : function(){
		var res = table.bootstrapTable('getData').length;
		if(res > 0){
			Manager.selectPage();			
		}else{
			Manager.refresh();					
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
			url : '/test/list',
			contentType : 'application/x-www-form-urlencoded',
			showColumns : true,
			toolbar : '#toolbar',
			minimumCountColumns : 6, // 设置最少显示列个数
			idField : 'id',
			queryParams : Manager.queryParams,
			responseHandler : Manager.responseHandler,

			columns : [ 
{
field : 'id',
title : '编号'},{
field : 'merchantName',
title : '商家名称'},{
field : 'csrName',
title : '客服名称'},{
field : 'customerName',
title : '顾客名称'},{
field : 'status',
title : '状态',
formatter : statusFormatter},{
title : '操作',
formatter : operationFormatter,
events : Manager.operateEvents}
]
		});
	},
	//查询请求参数
	queryParams : function (params) {
		
		var dto = {
merchantName:$.trim($('#merchantName').val()),
csrName:$.trim($('#csrName').val()),
customerName:$.trim($('#customerName').val())
};

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
	
	﻿update : function() {
		var dto = {
id:$.trim($('#editid').val()),
merchantName:$.trim($('#editmerchantName').val()),
csrName:$.trim($('#editcsrName').val()),
customerName:$.trim($('#editcustomerName').val())

};
		if (Manager.valid()) {
			var url = "/test/add";
			if (dto.id) {
				url = "/test/update";
			}
			$.ajax({
				url : url,
				type : "POST",
				dataType : "json",
				data : dto,
				success : function(data) {
					if(data.code == 0){
						$('#editDialog').modal('hide');
						successTip(data.desc);
						Manager.refresh();	
					}else{
						failTip(data.desc);
					}
				}
			});
		}
	},
	
	showAddModal : function(){
		$('#editid').val('');
		$('#editError').html('注意：带<font color="red">*</font>为必填项');	
		$('#editForm')[0].reset();
		$('#editDialog').modal('show');
	},
	
	﻿operateEvents : {
		﻿		'click .editBtn' : function(e, value, row, index) {
			$('#editid').val(row.id);$('#editmerchantName').val(row.merchantName);$('#editcsrName').val(row.csrName);$('#editcustomerName').val(row.customerName);
			$('#editError').html('注意：带<font color="red">*</font>为必填项');
			$("#editDialog").modal('show');
		},
		﻿		'click .statusBtn' : function(e, value, row, index) {
			var status = row.status;
			var color = status == 0 ? "text-success":"text-danger";
			var option = status == 0 ? "启用":"禁用";
			var label = status == 0 ? "label-success":"label-danger";
			var text = ['<strong class="',color,'">确认要',option,'<span class="label ',label,'"> ',row.csrName,' </span>吗?</strong>'].join('');
			var options = {
				id : 'modStatus',
				msg : text,
				backdrop : true,
				mstyle : 'width:400px;'
			}
			qiao.bs.confirm(options, function() {
				var url = '/test/updateStatus';
				var params = {
					'id' : row.id,
					'status' : status == 0 ? 1 : 0
				};
				$.post(url, params, function(data) {
					if (data.code == 0) {
						successTip(data.desc);
						Manager.refresh();			
					} else {
						failTip(data.desc);
					}
				}, 'json');
			});
		},
	},
	﻿	valid : function () {
		var merchantName= $.trim($('#editmerchantName').val());
var csrName= $.trim($('#editcsrName').val());
var customerName= $.trim($('#editcustomerName').val());
if(merchantName== null || validCode(merchantName.replace(/[-]/g,''))){
$('#editError').text('提示：商家名称为1-15位中文，字母，数字或-');
return false;}
if(csrName== null || validCode(csrName.replace(/[-]/g,''))){
$('#editError').text('提示：客服名称为1-15位中文，字母，数字或-');
return false;}
if(customerName== null || validCode(customerName.replace(/[-]/g,''))){
$('#editError').text('提示：顾客名称为1-15位中文，字母，数字或-');
return false;}

		return true;
	}
}