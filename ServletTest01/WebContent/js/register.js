$(document).ready(function(){
	
	isLoggedIn(function(ret){
		if (ret === true)
			window.location = "index.html";
	});
	
	$("body").on('click', 'form[role="login"] > button', function(){
		var username = $(this).parent().find('input[name="Username"]').val();
		var password = $(this).parent().find('input[name="Password"]').val();
		$.ajax({
			url: 'Login', 
			type: 'POST',
			dataType: 'json',
			data: {username: username, password: password}, 
			success: function(ret){
				window.location = "index.html";
			}
		});
	});
	
	
	var username_ok = false;
	var nickname_ok = false;
	
	$('input[name="Username"], input[name="Nickname"]').keyup(function(){
		var field = $(this).attr('name');
		var value = $(this).val().trim();
		var this_ = $(this);
		if ($(this).val().length == 0) {
			$(this).parent().attr('class', '');
			$(this).parent().children(".glyphicon-remove").hide();
			$(this).parent().children(".glyphicon-ok").hide();
			eval(field.toLowerCase()+"_ok=false;");
		}
		else {
			$.ajax({
				dataType: 'json',
				type:'POST',
				url:"checkAvailablity", 
				data: {field: field, value: value},
				success: function(result){
					if (result['result']=="AVAILABLE") {
						$("#register_"+field.toLowerCase()).attr('class', 'has-feedback has-success');
						$("#"+field.toLowerCase()+"_error").hide();
						(this_).parent().children(".glyphicon-ok").show();
						(this_).parent().children(".glyphicon-remove").hide();
						eval(field.toLowerCase()+"_ok=true;");
						
					} else if (result['result']=="TAKEN") {
						$("#register_"+field.toLowerCase()).attr('class', 'has-feedback has-error');
						(this_).parent().children(".glyphicon-ok").hide();
						(this_).parent().children(".glyphicon-remove").show();
						eval(field.toLowerCase()+"_ok=false;");
					}
				}
			});
		}
		
		
	});
	
	
	
	$("#do_register").click(function(){
		if (!username_ok)
			$("#username_error").show();
		if (!nickname_ok)
			$("#nickname_error").show();
		if (nickname_ok && username_ok) {
			var parms = {};
			$("#register_form input").each(function(){
				parms[$(this).attr('name')] = $(this).val();
			});
			$.ajax({
				dataType: 'json',
				type:'POST', 
				url:"RegisterUser", 
				data: parms, 
				success: function(ret){
					if (ret['result'] == "SUCCESS") {
						//TODO: LOGIN CODE
						window.location = "index.html?msg=registered&status=success";
					}
					else {
						alert("An unexpected error occured: "+ret['result']);
					}
				} 
			});
		}
	});
	
});

