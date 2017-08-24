<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>快递公司信息-找钢网积分商城</title>
	<#include "../common.ftl"/>
	<!-- 自定义 CSS 文件 -->
	<link rel="stylesheet" href="${contextPath}/css/backend.css" />
	<script src="${contextPath}/js/common.js"></script>
</head>
<body>
	<div id="express_div_1" class="container-fluid">
	<ol class="breadcrumb">
		  <li>基础管理</li>
		  <li class="active">快递公司管理</li>
	</ol>
		<div id="express_div_2" class="panel panel-default">
		<form id="express_form_1" action="" method="post">
		  <div id="express_div_3" class="panel-body">
		    <div id="express_div_4" class="row">
			  <div id="express_div_5" class="col-xs-3">
			    <div id="express_div_6" class="input-group">
			      <span class="input-group-addon">
			        	公司名称
			      </span>
			      <input type="text" class="form-control" id="expName"  maxlength="15"/>
			    </div><!-- /input-group -->
			  </div><!-- /.col-xs-3 -->
			  <div id="express_div_7" class="col-xs-3">
			    <div id="express_div_8" class="input-group">
			      <span class="input-group-addon">
			        	公司电话
			      </span>
			      <input type="text" class="form-control" id="expPhone" maxlength="20" onkeyup="trimNumber(this);" onblur="trimNumber(this);"/>
			    </div><!-- /input-group -->
			  </div><!-- /.col-xs-3 -->
			  <div id="express_div_9" class="col-xs-3">
			    <div id="express_div_10" class="input-group">
			      <span class="input-group-addon">
			        	公司代码
			      </span>
			      <input type="text" class="form-control" id="expCode"  maxlength="15" onkeyup="checkCn(this);" onblur="checkCn(this);"/>
			    </div><!-- /input-group -->
			  </div><!-- /.col-xs-3 -->
			  <div id="region_div_34" class="col-xs-2">
			    <div id="region_div_35" class="pull-right">
			    	<button type="reset" class="btn btn-primary" id="resetBtn"><i class="glyphicon glyphicon-remove"></i>清空</button>
			    </div>
			</div><!-- /.col-xs-1 -->
			  <div id="express_div_11" class="col-xs-1">
			    <div id="express_div_12" class="pull-right">
			    	<button type="button" class="btn btn-primary" id="searchBtn"><i class="glyphicon glyphicon-search"></i>查询</button>
			    </div>
			  </div><!-- /.col-xs-3 -->
			</div><!-- /.row -->	    
		  </div><!-- /.panel-body -->
		  </form>
		 </div><!-- /.panel panel-default -->
		<div id="toolbar" class="btn-group">
		    <button class="btn btn-primary" id="showAddBtn"><i class="glyphicon glyphicon-plus"></i> 新增公司</button>
		</div>
	<!-- table 插件 -->
	<#include "../table.ftl"/>
	</div>
	
	
	<!-- 快递公司 -->
	<div class="modal fade" id="editExp" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div id="jobManager_div_28" class="modal-dialog">
		<div id="jobManager_div_29" class="modal-content">
			<div id="jobManager_div_30" class="modal-header bg-primary">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">管理快递公司</h4>
			</div>
			<div id="jobManager_div_31" class="modal-body">
				<div class="alert alert-danger alert-dismissable" id="editExpError">
				   注意：带<font color="red">*</font>为必填项
				</div>
				<form id="editExpForm" class="form-horizontal" role="form">
				<div id="jobManager_div_32">
					<input type='hidden' id='pkid' value=''>
				</div>
				<div id="jobManager_div_33" class="form-group">
					<label for="editExpName" class="col-sm-2 control-label"><font color="red">*</font>公司名称</label>
					<div id="jobManager_div_34" class="col-sm-10">
						<input type="text" class="form-control" id="editExpName" placeholder="必填，1-15个字符,不可重名" maxlength="15"/>
					</div>
				</div>
				<div id="jobManager_div_35" class="form-group">
					<label for="editExpPhone" class="col-sm-2 control-label"><font color="red">*</font>公司电话</label>
					<div id="jobManager_div_36" class="col-sm-10">
						<input type="text" class="form-control" id="editExpPhone" placeholder="必填，5-20位" maxlength="20" onkeyup="trimNumber(this);" onblur="trimNumber(this);"/>
					</div>
				</div>
				<div id="jobManager_div_33" class="form-group">
					<label for="editExpCode" class="col-sm-2 control-label"><font color="red">*</font>公司代码</label>
					<div id="jobManager_div_34" class="col-sm-10">
						<input type="text" class="form-control" id="editExpCode" placeholder="必填，1-15位" maxlength="15" onkeyup="checkCode(this);" onblur="checkCode(this);"/>
					</div>
				</div>
				<div id="jobManager_div_33" class="form-group">
					<label for="editExpSite" class="col-sm-2 control-label"><font color="red">*</font>公司网址</label>
					<div id="jobManager_div_34" class="col-sm-10">
						<input type="text" class="form-control" id="editExpSite" placeholder="必填，http/https开头" maxlength="50" onkeyup="checkCn(this);" onblur="checkCn(this);"/>
					</div>
				</div>
				</form>
			</div>
			<div style="text-align : center;" class="modal-footer">
				<button type="button" class="btn btn-primary" id="editCityBtn"><i class="glyphicon glyphicon-ok"></i> 确定</button>
				<button type="button" class="btn btn-default" data-dismiss="modal"><i class="glyphicon glyphicon-remove"></i> 取消</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal -->
	</div>
	
	<script src="${contextPath}/js/common/qiao.js"></script>
	<!-- 快递公司查询js -->
	<script src="${contextPath}/js/basic/express.js"></script>
</body>
</html>
