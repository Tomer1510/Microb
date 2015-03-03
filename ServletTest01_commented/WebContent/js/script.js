function get_current_page() {
	return window.location.pathname.split('/').slice(2).join("");
}

window.onload = function(){
	$("#navbar").load("navbar.html", navbar_init);
	$("#sidebar").load("sidebar.html", sidebar_init);
	
	$("body").on('click', '.follow', function(){
		var this_ = this;
		var nickname;
		if ((nickname = $(this).data('nickname')).length === 0)
			return;
		else {
			$.ajax({
				url: 'ToggleFollow',
				dataType: 'json',
				type: 'POST',
				data: {nickname: nickname},
				success: function(res) {
					if (eval(res.value) === false) {
						$(this_).text('Follow');
					} else if (eval(res.value) === true) {
						$(this_).text('Unfollow');
					}
				}
			});
			
		}
	});
};

var urlParams;
(window.onpopstate = function () {
    var match,
        pl     = /\+/g,  // Regex for replacing addition symbol with a space
        search = /([^&=]+)=?([^&]*)/g,
        decode = function (s) { return decodeURIComponent(s.replace(pl, " ")); },
        query  = window.location.search.substring(1);

    urlParams = {};
    while (match = search.exec(query))
       urlParams[decode(match[1])] = decode(match[2]);
})();

function isFollowing(nickname, callback) {
	$.ajax({
		 type: 'post', 
		 dataType: 'json',
		 url: "IsFollowing", 
		 data: {nickname: nickname}, 
		 success: function(ret){
			 if (typeof callback === "function")
				 callback(eval(ret.value))
		 }
	 });
}


function addMessage(message) {
	var content = message.content, offset = 0;
	message.mentions.forEach(function(mention){
		content = content.substring(0, mention.start+offset) + "<a href=\"profile.html?nickname="+mention.nickname+"\" class=\"mention\">" + mention.nickname + "</a>" + content.substring(mention.end+offset);
		offset += 53 + mention.nickname.length;
	});
	var newMsg = '<div class = "col-md-12 panel panel-default">'
		+'<div class="panel-heading"><div style="display: inline-block;">'+message.authorNickname+'</div><div style="float: right; display: inline-block;">'+message.timestamp+'</div></div>'
		+'<div class="panel-body">'+content+'</div></div>';
	
	document.getElementById('messages').innerHTML += newMsg;
		
}

function isLoggedIn(callback) {
	$.getJSON("IsLoggedIn", function(ret){
		if (typeof callback === "function")
			callback(eval(ret.result), ret.value);
	});
}

function searchUsers(keyword, callback) {
	$.ajax({
		url: 'SearchUsers',
		type: 'POST',
		dataType: 'json',
		data: {
			keyword: keyword
		},
		success: function(users) {
			if (typeof callback === "function")
				callback(users);
		}, 
	});
}


function getUserDetails(field, value, callback) {
	$.ajax({
		url: 'GetUserDetails',
		type: 'POST',
		dataType: 'json',
		data: {
			field: field,
			value: value
		},
		success: function(ret) {
			if (typeof callback === "function") 
				callback(ret);
		}
	});
}