
var navbar_init = function() {
	$("body").on('click', 'form[role="login"] > button', function(){
		var username = $(this).parent().find('input[name="Username"]').val();
		var password = $(this).parent().find('input[name="Password"]').val();
		$.post('Login', {username: username, password: password}, function(ret){
			window.location = window.location;
		});
	});
	
	$.get("IsLoggedIn", function(ret){
		if (ret.trim() === "FALSE") 
			$('form[role="login"]').show();
		else {
			$("#loggedin").show();
			$('form[role="login"]').hide();
			$("#navbar-username").html(ret.trim());
		}
	});
	
};