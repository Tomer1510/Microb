
var navbar_init = function() {
	var mentions = [];
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
	
	isLoggedIn(function(ret){
		if (ret === false) 
			$('form[role="login"]').show();
		else if (ret === true){
			$("#loggedin").show();
			$('form[role="login"]').hide();
			$("#navbar #register").hide();
			$("#navbar-username").html(ret['value']);
			window.nickname=ret['value'];
		}
	});
	
	
	$("#navbar input[name=content]").keyup(function(){
		var text = $(this).val();
		if(text.indexOf('@') !== -1 && text.indexOf('@') !== text.length-1) {
			$(".post-autocomplete li").remove();
			var keyword = text.slice(text.indexOf('@')+1);
			searchUsers(keyword, function(users){
				if (users.length === 0)
					$(".post-autocomplete").hide();
				else
					$(".post-autocomplete").show();
				$.each(users, function(i, user){
					$(".post-autocomplete").append("<li>"+user.NickName+"</li>");
				});
			});
		} 
		else
			$(".post-autocomplete").hide();
	});
	
	$("#navbar").on('click', '.post-autocomplete li', function(){
		var nickname = $(this).html();
		var text = $("#navbar input[name=content]").val();
		text = text.slice(0, text.indexOf('@')) + nickname;
		$("#navbar input[name=content]").val(text);
		$(".post-autocomplete").hide();
		mentions.push({nickname: nickname, start: text.length - nickname.length, end: text.length});
		
	});
	
	$("#post-form").submit(function(){
		var text = $("#navbar input[name=content]").val();
		mentions.forEach(function(user, i){
			if (text.substring(user.start, user.end) !== user.nickname)
				mentions.splice(i, 1);
		});
		var mentionsElm = document.createElement("input");
		mentionsElm.type = "hidden";
		mentionsElm.name = "mentions";
		mentionsElm.value = JSON.stringify(mentions);
		$(this).append(mentionsElm);
	});
	
	
	
};