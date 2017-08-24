update : function() {
		${updateDtoJs}
		if (Manager.valid()) {
			var url = "${addUrl}";
			if (dto.id) {
				url = "${updateUrl}";
			}
			$.ajax({
				url : url,
				type : "POST",
				dataType : "json",
				data : dto,
				success : function(data) {
					if(data.code == 0){
						$('#editDialog').modal('hide');
						successTip(data.desc);
						Manager.refresh();	
					}else{
						failTip(data.desc);
					}
				}
			});
		}
	},