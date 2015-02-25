
var navbar_init = function() {
	$("body").on('click', 'form[role="login"] > button', function(){
		var username = $(this).parent().find('input[name="Username"]').val();
		var password = $(this).parent().find('input[name="Password"]').val();
		$.ajax({
			url: 'Login', 
			type: 'POST',
			dataType: 'json',
			data: {username: username, password: password}, 
			success: function(ret){
				window.location = window.location;
			}
		});
	});
	
	$.getJSON("IsLoggedIn", function(ret){
		if (!eval(ret['result'])) 
			$('form[role="login"]').show();
		else {
			$("#loggedin").show();
			$('form[role="login"]').hide();
			$("#navbar #register").hide();
			$("#navbar-username").html(ret['value']);
			window.username=ret['value'];
		}
	});
	
};