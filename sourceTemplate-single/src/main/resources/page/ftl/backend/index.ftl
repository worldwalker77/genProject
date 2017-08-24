<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<#include "ftl/common/common.ftl"/>
<!-- 自定义 CSS 文件 -->
<link rel="stylesheet" href="${contextPath}/resources/datetimepicker/css/bootstrap-datetimepicker.min.css" />
<title>管理后台</title>
</head>
<body style="overflow:hidden">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<nav id="id-nav" class="navbar navbar-default"  style="margin-bottom:0">
				  <div class="container-fluid">
				    <!-- Brand and toggle get grouped for better mobile display -->
				    <div class="navbar-header">
				      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				        <span class="sr-only">Toggle navigation</span>
				        <span class="icon-bar"></span>
				        <span class="icon-bar"></span>
				        <span class="icon-bar"></span>
				      </button>
				      <a class="navbar-brand" href="#"><font style="font-weight:bold;" size="5">管理后台</font></a>
				    </div>
				
				    <!-- Collect the nav links, forms, and other content for toggling -->
				    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				      <div class="pull-right">
				        <a href="http://passport.zhaogangren.com/logout?zg_sso_system_alias=purchase.im.imserver.api&returnUrl=http://www.pangmaomiao.com/backend/index">退出</a>
				      </div>
				    </div><!-- /.navbar-collapse -->
				    
				  </div><!-- /.container-fluid -->
				</nav>
				
				<ul id="myTab" class="nav nav-tabs">
				   <li class="active">
				      <a href="#client-version-controll">版本控制</a>
				   </li>
				   <li>
				   	  <a href="#biling-detail">账单明细查询</a>
				   </li>
				   <li>
				   	  <a href="#member-query">会员查询</a>
				   </li>
				   <li>
				   	  <a href="#with-drawal-record">提现记录查询</a>
				   </li>
				   <li>
				   	  <a href="#client-status-monitor">客户端状态监控</a>
				   </li>
				</ul>
				<div id="myTabContent" class="tab-content">
				   <div class="tab-pane fade in active">
				      <iframe name="iframepage" id="iframepage" src="${contextPath}/backend/versioncontroll" frameborder="0" scrolling="auto" height="700"></iframe>
				   </div>
				</div>
				
			</div>	
		</div>
	</div>
	

<script src="${contextPath}/resources/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script src="${contextPath}/resources/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="${contextPath}/resources/js/jquery/jquery.min.js"></script>
<script src="${contextPath}/resources/js/jquery/jquery-ui-1.11.4.min.js"></script>
<script src="${contextPath}/resources/js/backend/index.js"></script>
</body>
</html>
