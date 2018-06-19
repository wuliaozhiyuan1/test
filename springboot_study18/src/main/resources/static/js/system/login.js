function genTimestamp() {
	var time = new Date();
	return time.getTime();
}
function changeCode() {
	$("#codeImg").attr("src", "code?t=" + genTimestamp());
}
$(document).ready(function() {
	changeCode();
	$("#codeImg").bind("click", changeCode);
});
//TOCMAT重启之后 点击左侧列表跳转登录首页 
if (window != top) {
	top.location.href = location.href;
}
//客户端校验
function check() {
	if ($("#loginname").val() == "") {
		$("#loginname").tips({
			side : 1,
			msg : '用户名不得为空',
			bg : '#AE81FF',
			time : 3
		});
	//	//showfh();
		$("#loginname").focus();
		return false;
	} else {
		$("#loginname").val(jQuery.trim($('#loginname').val()));
	}
	if ($("#password").val() == "") {
		$("#password").tips({
			side : 1,
			msg : '密码不得为空',
			bg : '#AE81FF',
			time : 3
		});
		//showfh();
		$("#password").focus();
		return false;
	}
	if ($("#code").val() == "") {
		$("#code").tips({
			side : 1,
			msg : '验证码不得为空',
			bg : '#AE81FF',
			time : 3
		});
		////showfh();
		$("#code").focus();
		return false;
	}
	$("#code").tips({
		side : 1,
		msg : '正在登录 , 请稍后 ...',
		bg : '#68B500',
		time : 10
	});
	return true;
}
//提交
function onsubmitClick() {
	if(check()){
		var loginname = $("#loginname").val();
		var password = $("#password").val();
		var code = "qq313596790fh"+loginname+",fh,"+password+"QQ978336446fh"+",fh,"+$("#code").val();
		$.ajax({
			type: "POST",
			url: 'login',
	    	data: {username:loginname,password:password},
			dataType:'json',
			cache: false,
			success: function(data){
				if("success" == data.result){
					saveCookie();
					window.location.href="main/index";
				}else if("usererror" == data.result){
					$("#loginname").tips({
						side : 1,
						msg : "用户名或密码有误",
						bg : '#FF5080',
						time : 15
					});
					//showfh();
					$("#loginname").focus();
				}else if("codeerror" == data.result){
					$("#code").tips({
						side : 1,
						msg : "验证码输入有误",
						bg : '#FF5080',
						time : 15
					});
//					//showfh();
					$("#code").focus();
				}else{
					$("#loginname").tips({
						side : 1,
						msg : "缺少参数",
						bg : '#FF5080',
						time : 15
					});
					//showfh();
					$("#loginname").focus();
				}
			}
		});
	}
}