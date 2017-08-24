		'click .editBtn' : function(e, value, row, index) {
			${editFieldFillJs}
			$('#editError').html('注意：带<font color="red">*</font>为必填项');
			$("#editDialog").modal('show');
		},