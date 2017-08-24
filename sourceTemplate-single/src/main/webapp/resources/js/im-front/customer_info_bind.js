var customer_info_bind = (function() 
{
	var customer_info_bind = 
	{
		init : function(jsonInfo)
		{
			onInit();
		},
		eventInit : function()
		{
			$(".mkdir").bind("click", onCreateCustomerBindInfo);
			$(".bind").live("click", onBindCustomerInfo);
			$("#search_phone").bind("click", onQueryCustomerInfoByPhone);
			$("#phone_number").bind('keypress', function(event)
			{
				if (event.keyCode == 13)
				{
					onQueryCustomerInfoByPhone();
				}
			});
		}
	};
	
	function onInit()
	{
		var infor_name = $("#infor_name").text();
		if (infor_name.length == 0)
		{
			$(".bind-wrap").show();
		}
		else
		{
			$(".infor").show();
			$(".scroll-wrap").show();
		}
	}
	
	function onQueryCustomerInfoByPhone()
	{	
		var contextPath = $("#contextPath").val();
		var phoneNumber = $.trim($("#phone_number").val());
		$(".success").empty();
		if (phoneNumber.length == 0)
		{
			$("#info").text("查询前要输入手机号码哦。");
			$(".text").focus();
			return false;
		}
		if (phoneNumber.length != 11) 
		{
			$("#info").text("输入的手机号码不对哦。");
			$(".text").focus();
			return false;
		};
		var phoneReg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
		if (!phoneReg.test(phoneNumber))
		{
			$("#info").text("输入的手机号码格式不对哦。");
			$(".text").focus();
			return false;
		}
		$("#info").text("该QQ未绑定客户资料卡");

		var param = {"mobile" : phoneNumber};
		$.ajax({
			url 		: contextPath + "/r/findCrm",
			type 		: 'post',
			dataType 	: 'json',
			data 		: param,
			success 	: function(result)
			{
				if (result) 
				{
					if (result.data) 
					{
						if (result.code == 0)
						{
							var html;
							for (var i = 0; i < result.data.length; i++)
							{					
								html = "<span class=\"name\" id=\"success_name\">";
								html += result.data[i].realName;
								html += "</span>&nbsp;<span>";
								html += result.data[i].mobile;
								html += "</span>&nbsp;<button class=\"bind\" id=\"";
								html += result.data[i].userAccountId;
								html += "\">绑定</button>";
							}
							$(".success").empty().append(html);
						}
						else
						{
							$("#info").text(result.desc);
						}
					}
					else
					{
						$(".fail").show();
						$(".title").hide();
						$(".searchForm").hide();
					}
				}
			}
		});
	}
	
	function onBindCustomerInfo()
	{
		var contextPath = $("#contextPath").val();
		var accountId = $(".bind").attr("id");
		var qqNumber = $("#qqNumberVal").val();
		var param = {"accountId" : accountId, "qq" : qqNumber};
		$.ajax({
			url 		: contextPath + "/r/bindCrm",
			type 		: 'post',
			dataType 	: 'json',
			data 		: param,
			success 	: function(result)
			{
				if (result) 
				{
					if (result.code == 0 && result.data == true)
					{
						$(".bind-wrap").hide();
						$(".bind-link").show();
						setTimeout(function(){$(".infor").show();location.reload();},1000);
					}
					else
					{
						$("#info").text(result.desc);
					}
				}
			}
		});
	}
	
	function onCreateCustomerBindInfo()
	{
		var crmUrl = $("#crmUrl").val();
		var qqNumber = $("#qqNumberVal").val();
		if (!qqNumber)
		{
			return;
		}

		var token = getCookie("zg_sso_token");
		var html = crmUrl + "/openAccount/index?qqNumber=";
		html += qqNumber;
		if (token)
		{
			html += "&token=";
			html += token;
		}
		window.open(html);
	}
	
	function getCookie(name)
	{  
	    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));  
	    if(arr != null)
	    {  
	     return unescape(arr[2]);
	    }
	    else
	    {  
	     return null;  
	    }
	}
	
	function onEnterKeySearcher()
	{
	  if(event.keyCode == 13)
	  {
	  	$("#search_phone").click();
	  }
	}
	
	return customer_info_bind;
})();