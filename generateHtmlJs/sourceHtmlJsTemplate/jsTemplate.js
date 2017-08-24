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
			url : '${selectUrl}',
			contentType : 'application/x-www-form-urlencoded',
			showColumns : true,
			toolbar : '#toolbar',
			minimumCountColumns : 6, // 设置最少显示列个数
			idField : 'id',
			queryParams : Manager.queryParams,
			responseHandler : Manager.responseHandler,

			${columnsJs}
		});
	},
	//查询请求参数
	queryParams : function (params) {
		
		${selectDtoJs}

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
	
	${updateMethodJs}
	
	showAddModal : function(){
		$('#editid').val('');
		$('#editError').html('注意：带<font color="red">*</font>为必填项');	
		$('#editForm')[0].reset();
		$('#editDialog').modal('show');
	},
	
	${operateEventsJs}
	${validMethodJs}
}