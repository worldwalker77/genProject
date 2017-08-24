<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>${headTitle}</title>
	<#include "../common.ftl"/>
	<!-- 自定义 CSS 文件 -->
	<link rel="stylesheet" href="/css/base/backend.css" />
	<script src="/js/utils/common.js"></script>
</head>
<body>
	<div class="container-fluid">
		<ol class="breadcrumb">
	<li>${first-breadcrumb}</li>
	<li class="active">${second-breadcrumb}</li>
</ol>
		<div class="panel panel-default">
			<form action="" method="post">
			  <div class="panel-body">
				  <div class="row">
					<div class="col-xs-3">
						<div class="input-group">
						  <span class="input-group-addon">
								公司名称
						  </span>
						  <input type="text" class="form-control" id="expName"  maxlength="15" onkeyup="check(this);" onblur="check(this);"/>
						</div><!-- /input-group -->
					</div><!-- /col-xs-3 -->
					<div class="col-xs-3">
						<div class="input-group">
						  <span class="input-group-addon">
								公司电话
						  </span>
						  <input type="text" class="form-control" id="expPhone"  maxlength="15" onkeyup="check(this);" onblur="check(this);"/>
						</div><!-- /input-group -->
					</div><!-- /col-xs-3 -->
					<div class="col-xs-3">
						<div class="input-group">
						  <span class="input-group-addon">
								公司代码
						  </span>
						  <input type="text" class="form-control" id="expCode"  maxlength="15" onkeyup="check(this);" onblur="check(this);"/>
						</div><!-- /input-group -->
					</div><!-- /col-xs-3 -->
				</div>
				  <div class="col-xs-2">
					<div class="pull-right">
						<button type="reset" class="btn btn-primary" id="resetBtn"><i class="glyphicon glyphicon-remove"></i>reset</button>
					</div>
				</div><!-- /.col-xs-1 -->
				  <div class="col-xs-1">
					<div class="pull-right">
						<button type="button" class="btn btn-primary" id="searchBtn"><i class="glyphicon glyphicon-search"></i>search</button>
					</div>
				</div><!-- /.col-xs-3 -->
			  </div><!-- /.panel-body -->
			</form>
		 </div><!-- /.panel panel-default -->
		<div id="toolbar" class="btn-group">
			<button class="btn btn-primary" id="addBtn"><i class="glyphicon glyphicon-plus"></i>add</button>
		</div>
	<!-- table 插件 -->
	<#include "../table.ftl"/>
	</div>
	<div class="modal fade" id="editDialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
					
					<div class="form-group">
						<label for="editexpName" class="col-sm-2 control-label"><font color="red">*</font>公司名称</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="editexpName" placeholder="${placeholder}" maxlength="${maxlength}" onkeyup="check(this);" onblur="check(this);"/>
						</div>
					</div>
					<div class="form-group">
						<label for="editexpPhone" class="col-sm-2 control-label"><font color="red">*</font>公司电话</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="editexpPhone" placeholder="${placeholder}" maxlength="${maxlength}" onkeyup="check(this);" onblur="check(this);"/>
						</div>
					</div>
					<div class="form-group">
						<label for="editexpCode" class="col-sm-2 control-label"><font color="red">*</font>公司代码</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="editexpCode" placeholder="${placeholder}" maxlength="${maxlength}" onkeyup="check(this);" onblur="check(this);"/>
						</div>
					</div>
				</form>
			</div>
			<div style="text-align : center;" class="modal-footer">
				<button type="button" class="btn btn-primary" id="editBtn"><i class="glyphicon glyphicon-ok"></i> 确定</button>
				<button type="button" class="btn btn-default" data-dismiss="modal"><i class="glyphicon glyphicon-remove"></i> 取消</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal -->
</div>
	<script src="/js/utils/qiao.js"></script>
	<script src="/js/basic/express.js"></script>
</body>
</html>
