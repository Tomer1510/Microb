/**
 * This script file contains helping scripts that can be used from every page
 * 
 */

/**
 * Returns the current page name 
 */
function get_current_page() {
	return window.location.pathname.split('/').slice(2).join("");
}


window.onload = function(){
	$("#navbar").load("navbar.html", navbar_init); //Load the top navbar, then call its init function
	$("#sidebar").load("sidebar.html", sidebar_init); //Load the sidebar, then call its init function
	
	/**
	 * Handles click events on the 'follow' buttons
	 */
	$("body").on('click', '.follow', function(){
		var this_ = this;
		var nickname;
		if ((nickname = $(this).data('nickname')).length === 0)
			return;
		else {
			$.ajax({
				url: 'ToggleFollow/'+nickname,
				dataType: 'json',
				type: 'GET',
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

/**
 * Sets a global array containing all the GET parameters
 */
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


/**
 * Checks whether or not the current user is following another user
 * @param nickname - nickname of the other user
 * @param callback - a callback function that should be called with the results
 */
function isFollowing(nickname, callback) {
	$.ajax({
		 type: 'get', 
		 dataType: 'json',
		 url: "IsFollowing/"+nickname, 
		 success: function(ret){
			 if (typeof callback === "function")
				 callback(eval(ret.value))
		 }
	 });
}


/**
 * Parses timestamp strings
 * @param timeString - a timestamp string
 * @returns the parsed time string
 */
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


/**
 * Turns all topics into 'topic links'
 * @param message - a message
 * @returns the changed message
 */
function parseTopics(message) {
	var subtext = message, index, offset=0;
	while ((index = subtext.indexOf("#")) !== -1) {
		offset+=index;
		subtext = subtext.substr(index);
		var topic = subtext.split(/[\s\n]+/)[0];
		message = message.substring(0, offset)  + "<span class=\"topic\" data-topic=\""+topic.substr(1).trim()+"\">" + topic + "</span>" + message.substring(offset+topic.length);
		subtext = subtext.substr(topic.length);
		offset += topic.length + 41 + topic.length - 1;
	}
	return message;
}


/**
 * Turns all (valid) mentions into links to the users' profiles
 * @param original message
 * @returns the new message with the links
 */
function parseMentions(message) {
	var subtext = message, index, offset=0;
	while ((index = subtext.indexOf("@")) !== -1)  {
		offset+=index;
		subtext = subtext.substr(index);
		var mention = subtext.split(/[\s\n]+/)[0];
		getUserDetails("Nickname", mention.substr(1).trim(), function(details){
			if (details.result === undefined) {
				message = message.substring(0, offset)  + "<a class=\"mention\" href=\"profile.html?nickname="+mention.substr(1).trim()+"\">" + mention + "</a>" + message.substring(offset+mention.length);
				subtext = subtext.substr(mention.length);
				offset += mention.length + 53 + mention.substr(1).trim().length;
			}
			else {
				offset += mention.length;
				subtext = subtext.substr(mention.length);
			}

		});
	}
	return message;
}


/**
 * 
 * @param message - the message object that the servlet returns
 * @param div - (optional) the div the message should be appended to
 */
function addMessage(message, div) {
	if (div === undefined)
		div = document.getElementById('messages');
	message.timestamp = parseTime(message.timestamp);
	var content = message.content;
	content = parseTopics(content);
	content = parseMentions(content);
	var newMsg = '<div class = "col-md-12 panel panel-default message" data-id="'+message.messageID+'">'
		+'<div class="panel-body"><div class="panel-header"><div style="display: inline-block;"><a href="profile.html?nickname='+message.authorNickname+'" class=\"author\">@'+message.authorNickname+'</a></div><div style="float: right; display: inline-block;">'+message.timestamp+'</div></div><hr>'
		+'<div class="message-content">'+content+'</div><br><button class="btn btn-default republish" data-toggle="modal" data-target="#newPost"><span class="glyphicon glyphicon-retweet"></span> <span class="msg-button hidden-xs hidden-sm"> Republish</span></button>&nbsp<button class="btn btn-default reply" data-toggle="modal" data-target="#newPost"><span class="glyphicon glyphicon-share-alt"></span> <span class="msg-button hidden-xs hidden-sm"> Reply</span></button></div></div>';
	
	div.innerHTML += newMsg;
		
}



/**
 * Checks if the user is logged in
 * @param callback - a function that should be called with the results
 */
function isLoggedIn(callback) {
	$.getJSON("IsLoggedIn", function(ret){
		if (typeof callback === "function")
			callback(eval(ret.result), ret.value);
	});
}



/**
 * Fetches user details for the specified user
 * @param field - field to user (nickname or username)
 * @param value - value of the field
 * @param callback - a function that should be called with the user details
 */
function getUserDetails(field, value, callback) {
	$.ajax({
		url: 'GetUserDetails/'+field+'/'+value,
		type: 'GET',
		dataType: 'json',
		async: false,
		success: function(ret) {
			if (typeof callback === "function") 
				callback(ret);
		}
	});
}
