					<div class="form-group">
						<label for="${editField}" class="col-sm-2 control-label"><font color="red">*</font>${title}</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="${editField}" placeholder="${placeholder}" maxlength="${maxlength}" onkeyup="${checkMethod}(this);" onblur="${checkMethod}(this);"/>
						</div>
					</div>