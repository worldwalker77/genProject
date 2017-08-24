		'click .statusBtn' : function(e, value, row, index) {
			var status = row.status;
			var color = status == 0 ? "text-success":"text-danger";
			var option = status == 0 ? "启用":"禁用";
			var label = status == 0 ? "label-success":"label-danger";
			var text = ['<strong class="',color,'">确认要',option,'<span class="label ',label,'"> ',row.${comfirmColumn},' </span>吗?</strong>'].join('');
			var options = {
				id : 'modStatus',
				msg : text,
				backdrop : true,
				mstyle : 'width:400px;'
			}
			qiao.bs.confirm(options, function() {
				var url = '${statusUrl}';
				var params = {
					'id' : row.id,
					'status' : status == 0 ? 1 : 0
				};
				$.post(url, params, function(data) {
					if (data.code == 0) {
						successTip(data.desc);
						Manager.refresh();			
					} else {
						failTip(data.desc);
					}
				}, 'json');
			});
		},