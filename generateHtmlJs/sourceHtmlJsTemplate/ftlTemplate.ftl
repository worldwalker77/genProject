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
		${breadCrumbArea}
		<div class="panel panel-default">
			<form action="" method="post">
			  <div class="panel-body">
				  ${selectRowsArea}
				 
			  </div><!-- /.panel-body -->
			</form>
		 </div><!-- /.panel panel-default -->
		${addButtonArea}
	<!-- table 插件 -->
	<#include "/common/table.ftl"/>
	</div>
	${editDialogArea}
	<script src="/js/utils/qiao.js"></script>
	<script src="/js/test/testIndex.js"></script>
</body>
</html>
