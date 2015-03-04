
var navbar_init = function() {
	var mentions = [];
	var republish = 0;
	
	isLoggedIn(function(ret, nickname){
		if (ret === false) {
			$('form[role="login"]').hide();
			if (window.location.pathname.indexOf("register.html") === -1)
				window.location = "register.html";
		}
		else if (ret === true){
			$("#loggedin").show();
			$('form[role="login"]').hide();
			$("#navbar #register").hide();
			$("#navbar-nickname").html(nickname);
			window.nickname=nickname;
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
	
	function addHiddenElm(name, value) {
		var elm = document.createElement("input");
		elm.type = "hidden";
		elm.name = name;
		elm.value = JSON.stringify(value);
		return elm;
	}
	
	$("#post-form").submit(function(){
		
		
		var text = $(this).find("textarea[name=content]").val();
		if (text.length > 140)
			return false;
		mentions.forEach(function(user, i){
			if (text.substring(user.start, user.end) !== user.nickname)
				mentions.splice(i, 1);
		});
		
			/*document.createElement("input");
		mentionsElm.type = "hidden";
		mentionsElm.name = "mentions";
		mentionsElm.value = JSON.stringify(mentions);
		$(this).append(addHiddenElm("mentions", mentions));*/
		var topics = [];
		text.split(" ").forEach(function(word){
			if(word[0] === '#' && word.length > 1)
				topics.push({'topic': word.substr(1)});
		});
		$(this).append(addHiddenElm("topics", topics));
		$(this).append(addHiddenElm("republish", republish));
	});
	
	$("#post-form textarea[name=content]").keyup(function(){
		var len = $(this).val().length;
		$("#post-form .letter-counter").text(len)
		if (len > 140) {
			$("#post-form .letter-counter").addClass('red');
		}
		else {
			$("#post-form .letter-counter").removeClass('red');
		}
	});
	
	$("body").on('click', '.republish', function(){
		var originalMessage = $(this).parent().find('.message-content').text();
		$("#post-form textarea[name=content]").val("RE: "+originalMessage+"\n").keyup();
		republish = $(this).parents('.message').data('id');
	
	});
	
	$('#post-form button[data-dismiss="modal"]').click(function(){
		console.log("test");
		republish = 0;
	})
	
};