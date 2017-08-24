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
					${editRowsArea}
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