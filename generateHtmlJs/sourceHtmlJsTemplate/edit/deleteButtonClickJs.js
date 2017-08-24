		'click .editBtn' : function(e, value, row, index) {
			$("#id").val(row.id);
			${editFieldFill}
			$('#editError').html('注意：带<font color="red">*</font>为必填项');
			$("#editDialog").modal('show');
		},