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
	
	//编辑提交按钮
	$('#editBtn').click(function() {
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

			columns : [ 
{
field : 'id',
title : '公司编号'},{
field : 'expName',
title : '公司名称'},{
field : 'expPhone',
title : '公司电话'},{
field : 'expCode',
title : '公司代码'},{
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
expName:$.trim($('#expName').val()),
expPhone:$.trim($('#expPhone').val()),
expCode:$.trim($('#expCode').val())
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
	
	update : function() {
		var dto = {
id:$.trim($('#editid').val()),
expName:$.trim($('#editexpName').val()),
expPhone:$.trim($('#editexpPhone').val()),
expCode:$.trim($('#editexpCode').val())

};
		if (Manager.valid()) {
			var url = "1";
			if (dto.id) {
				url = "1";
			}
			$.ajax({
				url : url,
				type : "POST",
				dataType : "json",
				data : dto,
				success : function(data) {
					if(data.result == -1){
						failTip("操作失败");
					}else if(data.result == 2){
						failTip("操作失败");
					}else{
						$('#editDialog').modal('hide');
						successTip("操作成功");
						Manager.refresh();			
					}
				}
			});
		}
	},
	
	showAddModal : function(){
		$('#id').val('');
		$('#editError').html('注意：带<font color="red">*</font>为必填项');	
		$('#editForm')[0].reset();
		$('#editDialog').modal('show');
	},
	
	operateEvents : {
				'click .editBtn' : function(e, value, row, index) {
			$("#id").val(row.id);
			$('#editid').val(row.id);$('#editexpName').val(row.expName);$('#editexpPhone').val(row.expPhone);$('#editexpCode').val(row.expCode);
			$('#editError').html('ע�⣺��<font color="red">*</font>Ϊ������');
			$("#editDialog").modal('show');
		},
				'click .statusBtn' : function(e, value, row, index) {
			var status = row.status;
			var color = status == 0 ? "text-success":"text-danger";
			var option = status == 0 ? "����":"����";
			var label = status == 0 ? "label-success":"label-danger";
			var text = ['<strong class="',color,'">ȷ��Ҫ',option,'<span class="label ',label,'"> ',row.expName,' </span>��?</strong>'].join('');
			var options = {
				id : 'modStatus',
				msg : text,
				backdrop : true,
				mstyle : 'width:400px;'
			}
			qiao.bs.confirm(options, function() {
				var url = '1';
				var params = {
					'id' : row.id,
					'status' : status == 0 ? 1 : 0
				};
				$.post(url, params, function(data) {
					if (data.result == 1) {
						successTip("�����ɹ�");
						Manager.refresh();			
					} else {
						failTip("����ʧ��");
					}
				}, 'json');
			});
		},
	}
	
}