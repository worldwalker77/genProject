<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>${headTitle}</title>
	<#include "/common/common.ftl"/>
	<!-- 自定义 CSS 文件 -->
	<link rel="stylesheet" href="/css/base/backend.css" />
	<script src="/js/utils/common.js"></script>
</head>
<body>
	<div class="container-fluid">
		﻿<ol class="breadcrumb">
	<li>测试</li>
	<li class="active">测试</li>
</ol>
		<div class="panel panel-default">
			<form action="" method="post">
			  <div class="panel-body">
				  				<div class="row">

﻿					<div class="col-xs-3">
						<div class="input-group">
						  <span class="input-group-addon">
								商家名称
						  </span>
						  <input type="text" class="form-control" id="merchantName"  maxlength="15" onkeyup="validCode(this);" onblur="validCode(this);"/>
						</div><!-- /input-group -->
					</div>
﻿					<div class="col-xs-3">
						<div class="input-group">
						  <span class="input-group-addon">
								客服名称
						  </span>
						  <input type="text" class="form-control" id="csrName"  maxlength="15" onkeyup="validCode(this);" onblur="validCode(this);"/>
						</div><!-- /input-group -->
					</div>
﻿					<div class="col-xs-3">
						<div class="input-group">
						  <span class="input-group-addon">
								顾客名称
						  </span>
						  <input type="text" class="form-control" id="customerName"  maxlength="15" onkeyup="validCode(this);" onblur="validCode(this);"/>
						</div><!-- /input-group -->
					</div>
﻿				<div class="pull-right">
					<div class="col-xs-1">
						<button type="reset" class="btn btn-primary" id="resetBtn"><i class="glyphicon glyphicon-remove"></i>清空</button>
					</div><!-- /.col-xs-1 -->
				</div>
﻿				<div class="pull-right">
					<div class="col-xs-1">
						<button type="button" class="btn btn-primary" id="searchBtn"><i class="glyphicon glyphicon-search"></i>查询</button>
					</div><!-- /.col-xs-1 -->
				</div>
				</div>

				 
			  </div><!-- /.panel-body -->
			</form>
		 </div><!-- /.panel panel-default -->
		﻿<div id="toolbar" class="btn-group">
			<button class="btn btn-primary" id="addBtn"><i class="glyphicon glyphicon-plus"></i>增加</button>
		</div>
	<!-- table 插件 -->
	<#include "/common/table.ftl"/>
	</div>
	﻿<div class="modal fade" id="editDialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header bg-primary">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">${headTitle}</h4>
			</div>
			<div class="modal-body">
				<div class="alert alert-danger alert-dismissable" id="editError">
				   注意：带<font color="red">*</font>为必填项
				</div>
				<form id="editForm" class="form-horizontal" role="form">
					<div>
						<input type='hidden' id='editid' value=''>
					</div>
					
﻿					<div class="form-group">
						<label for="editmerchantName" class="col-sm-2 control-label"><font color="red">*</font>商家名称</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="editmerchantName" placeholder="${placeholder}" maxlength="${maxlength}" onkeyup="validCode(this);" onblur="validCode(this);"/>
						</div>
					</div>
﻿					<div class="form-group">
						<label for="editcsrName" class="col-sm-2 control-label"><font color="red">*</font>客服名称</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="editcsrName" placeholder="${placeholder}" maxlength="${maxlength}" onkeyup="validCode(this);" onblur="validCode(this);"/>
						</div>
					</div>
﻿					<div class="form-group">
						<label for="editcustomerName" class="col-sm-2 control-label"><font color="red">*</font>顾客名称</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="editcustomerName" placeholder="${placeholder}" maxlength="${maxlength}" onkeyup="validCode(this);" onblur="validCode(this);"/>
						</div>
					</div>
				</form>
			</div>
			<div style="text-align : center;" class="modal-footer">
				<button type="button" class="btn btn-primary" id="editSumitBtn"><i class="glyphicon glyphicon-ok"></i> 确定</button>
				<button type="button" class="btn btn-default" data-dismiss="modal"><i class="glyphicon glyphicon-remove"></i> 取消</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal -->
</div>
	<script src="/js/utils/qiao.js"></script>
	<script src="/js/test/testIndex.js"></script>
</body>
</html>
