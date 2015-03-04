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

function parseTime(timeString) {
	var now = Date.now();
	var timestamp = new Date(timeString)
	if (now - timestamp < 1000*60) {
		return parseInt((now-timestamp)/1000) + " seconds ago";
	} 
	else if (now - timestamp < 1000*60*60) {
		return parseInt((now-timestamp)/(1000*60)) + " minutes ago";
	}
	else if (now - timestamp < 1000*60*60*24) {
		return parseInt((now-timestamp)/(1000*60*60)) + " hours ago";
	} 
	else {
		return timeString; 
	}

}

function parseTopics(message) {
	var subtext = message, index, offset=0;
	while ((index = subtext.indexOf("#")) !== -1) {
		offset+=index;
		subtext = subtext.substr(index);
		var topic = subtext.split(" ")[0];
		message = message.substring(0, offset)  + "<span class=\"topic\" data-topic=\""+topic.substr(1)+"\">" + topic + "</span>" + message.substring(offset+topic.length);
		subtext = subtext.substr(topic.length);
		offset += topic.length + 41 + topic.length - 1;
	}
	return message;
}


function addMessage(message, div) {
	if (div === undefined)
		div = document.getElementById('messages');
	message.timestamp = parseTime(message.timestamp);
	var content = message.content;
	content = parseTopics(content);
	var newMsg = '<div class = "col-md-12 panel panel-default message">'
		+'<div class="panel-body"><div class="panel-header"><div style="display: inline-block;"><a href="profile.html?nickname='+message.authorNickname+'">@'+message.authorNickname+'</a></div><div style="float: right; display: inline-block;">'+message.timestamp+'</div></div><hr>'
		+'<div class="message-content">'+content+'</div><br><br><button class="btn btn-default republish" data-toggle="modal" data-target="#newPost">Republish</button></div></div>';
	
	div.innerHTML += newMsg;
		
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
