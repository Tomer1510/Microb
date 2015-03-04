$(document).ready(function(){
	//If the user is already logged in - redirect to index
	isLoggedIn(function(ret){
		if (ret === true)
			window.location = "index.html";
	});
	
	
	//Handles the login button
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
	
	
	var username_ok = false; //Used for username validation
	var nickname_ok = false; //Used for nickname validation
	
	//Handles real-time nickname & username validation (checks for duplicates)
	$('input[name="Username"], input[name="Nickname"]').keyup(function(){
		var field = $(this).attr('name');
		var value = $(this).val().trim();
		var this_ = $(this);
		if ($(this).val().length == 0) { //If empty, then mark as valid for now but set the variable to false, because empty fields are invalid
			$(this).parent().attr('class', '');
			$(this).parent().children(".glyphicon-remove").hide();
			$(this).parent().children(".glyphicon-ok").hide();
			eval(field.toLowerCase()+"_ok=false;");
		}
		else { //If not empty - check if it's taken or available, and indicate the result to the user
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
	
	
	/**
	 * Handles register form submit
	 */
	$("#do_register").click(function(){
		//If username or nickname invalid - show the error to the user
		if (!username_ok)
			$("#username_error").show();
		if (!nickname_ok)
			$("#nickname_error").show();
		if ($("#register_description input[name=Description]").val().length > 50) //Check that the description has valid length
			return false;
		if (nickname_ok && username_ok && $("#register_description input[name=Description]").val().length <= 50) { //If everything ok - preceed
			var parms = {};
			$("#register_form input").each(function(){ //Collect all parameters and put in the object
				parms[$(this).attr('name')] = $(this).val();
			});
			
			//Now send all the data to the registration servlet, if everything is ok - redirect to the index
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
	
	
	//Implements letter counter for the description field 
	$("#register_description input[name=Description]").keyup(function(){
		var len = $(this).val().length;
		$("#register_description .letter-counter").text(len)
		if (len > 50) {
			$("#register_description .letter-counter").addClass('red');
		}
		else {
			$("#register_description .letter-counter").removeClass('red');
		}
	});
	
});

