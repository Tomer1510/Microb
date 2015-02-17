$(document).ready(function(){
	
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
			$.post("checkAvailablity", {field: field, value: value}, function(output){
				if (output.trim()=="AVAILABLE") {
					$("#register_"+field.toLowerCase()).attr('class', 'has-feedback has-success');
					$("#"+field.toLowerCase()+"_error").hide();
					(this_).parent().children(".glyphicon-ok").show();
					(this_).parent().children(".glyphicon-remove").hide();
					eval(field.toLowerCase()+"_ok=true;");
					
				} else if (output.trim()=="TAKEN") {
					$("#register_"+field.toLowerCase()).attr('class', 'has-feedback has-error');
					(this_).parent().children(".glyphicon-ok").hide();
					(this_).parent().children(".glyphicon-remove").show();
					eval(field.toLowerCase()+"_ok=false;");

	
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
			$.post("RegisterUser", parms, function(ret){
				if (ret.trim() == "SUCCESS") {
					//TODO: LOGIN CODE
					window.location = "index.html?msg=registered&status=success"
				}
				else {
					alert("An unexpected error occured: "+ret.trim());
				}
			});
		}
	});
	
});

