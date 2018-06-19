var validate = function(){
	var flag = true;
	$("input[required=required]").each(function(){
		if($(this).val() == ""){
			var message = $(this).closest("td").prev().text();
			$(this).tips({
				side:3,
	            msg:'请输入' + message,
	            bg:'#AE81FF',
	            time:2
			})
			$(this).focus();
			flag = false
			return;
		}
	});
	if(!flag){
		return false;
	}
	$("textarea[required=required]").each(function(){
		if($(this).val() == ""){
			var message = $(this).closest("td").prev().text();
			$(this).tips({
				side:3,
	            msg:'请输入' + message,
	            bg:'#AE81FF',
	            time:2
			})
			$(this).focus();
			flag = false;
			return;
		}
	});
	if(!flag){
		return false;
	}
	$("select[required=required]").each(function(){
		if($(this).val() == null){
			var message = $(this).closest("td").prev().text();
			$(this).next("div").tips({
				side:3,
				msg:'请输入' + message,
				bg:'#AE81FF',
				time:2
			})
			$(this).next("div").find("input").focus();
			flag = false;
			return;
		}
	});
	if(!flag){
		return false;
	}
	$("input[email=email]").each(function(){
		function ismail(mail){
			return(new RegExp(/^(?:[a-zA-Z0-9]+[_\-\+\.]?)*[a-zA-Z0-9]+@(?:([a-zA-Z0-9]+[_\-]?)*[a-zA-Z0-9]+\.)+([a-zA-Z]{2,})+$/).test(mail));
		}
		if($(this).val() != ""){
			var val = $(this).val();
			if(!ismail(val)){
				var message = $(this).closest("td").prev().text();
				$(this).tips({
					side:3,
					msg:message + "非email格式",
					bg:'#AE81FF',
					time:2
				})
				$(this).focus();
				flag = false;
				return;
			}
		}
	});
	if(!flag){
		return false;
	}
	$("input[phone=phone]").each(function(){
		function isphone(phone){
			return(new RegExp(/^0?1[3|4|5|7|8][0-9]\d{8}$/).test(phone));
		}
		if($(this).val() != ""){
			var val = $(this).val();
			if(!isphone(val)){
				var message = $(this).closest("td").prev().text();
				$(this).tips({
					side:3,
					msg:message + "非手机号码格式",
					bg:'#AE81FF',
					time:2
				})
				$(this).focus();
				flag = false;
				return;
			}
		}
	});
	if(!flag){
		return false;
	}
	$("input[fax=fax]").each(function(){
		function isfax(fax){
			return(new RegExp(/^(\d{3,4}-)?\d{7,8}$/).test(fax));
		}
		if($(this).val() != ""){
			var val = $(this).val();
			if(!isfax(val)){
				var message = $(this).closest("td").prev().text();
				$(this).tips({
					side:3,
					msg:message + "非电话号码格式",
					bg:'#AE81FF',
					time:2
				})
				$(this).focus();
				flag = false;
				return;
			}
		}
	});
	if(!flag){
		return false;
	}
	$("input[postCode=postCode]").each(function(){
		function ispostCode(postCode){
			return(new RegExp(/^[1-9][0-9]{5}$/).test(postCode));
		}
		if($(this).val() != ""){
			var val = $(this).val();
			if(!ispostCode(val)){
				var message = $(this).closest("td").prev().text();
				$(this).tips({
					side:3,
					msg:message + "非邮编格式",
					bg:'#AE81FF',
					time:2
				})
				$(this).focus();
				flag = false;
				return;
			}
		}
	});
	if(!flag){
		return false;
	}
	return true;
}
$("input[required=required]").each(function(){
	var $redSpan = $("<span style='color:red'>*</span>")
	$(this).after($redSpan);
	
});
$("textarea[required=required]").each(function(){
	var $redSpan = $("<span style='color:red'>*</span>")
	$(this).after($redSpan);
	
});
$("select[required=required]").each(function(){
	var $redSpan = $("<span style='color:red'>*</span>")
	$(this).after($redSpan);
	
});