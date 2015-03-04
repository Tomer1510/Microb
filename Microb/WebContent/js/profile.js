$(document).ready(function(){
	
	
	if (urlParams.nickname === undefined && urlParams.username === undefined)
		urlParams.nickname =  $.ajax({type: 'get', dataType: 'json', url: "IsLoggedIn", success: function(ret){return ret}, async: false }).responseJSON.value;
	isLoggedIn(function(ret, nickname){
		if (ret === true && nickname === urlParams.nickname)
			$("#user_details .follow").remove();
	});
	var field = (urlParams.nickname === undefined)?"Username":"Nickname", 
			value = (urlParams.nickname === undefined)?urlParams.username:urlParams.nickname;
	
	
	/**
	 * Gets the details about the user who's profile we're in
	 * After getting the results, it also fetches other details like followees and followers for the user
	 */
	getUserDetails(field, value, function(ret){		
			//Sets the follow button's text (i.e. follow or unfollow)
			isFollowing(ret["NickName"], function(following){
				$("#user_details .follow").text(following?"Unfollow":"Follow him!");
			});
			
			
			//Fetches the user's top 10 followees and adds them to the "following" list
			$.ajax({
				'type': 'POST', 
				url: 'GetTop10Following', 
				dataType: 'json', 
				data: {nickname: ret.NickName}, 
				success: function(users){
					$('.followButtons button[data-type="following"]').text("Following ("+users.length+")");
					users.forEach(function(user){
						isFollowing(user.result, function(following){
								var text = following?"Unfollow":"Follow him!";
								$("#following ul").append('<li><a href="profile.html?nickname='+user.result+'">@'+user.result+'</a> &nbsp&nbsp <button class="btn btn-default follow" data-nickname="'+user.result+'">'+text+'</button></li>');							
						});
					});
				} 
			});
			
			
			//Fetches the user's top 10 followers and adds them to the "followers" list
			$.ajax({
				'type': 'POST', 
				url: 'GetTop10Followers', 
				dataType: 'json', 
				data: {nickname: ret.NickName}, 
				success: function(users){
					$('.followButtons button[data-type="followers"]').text("Followers ("+users.length+")");
					users.forEach(function(user){
						isFollowing(user.result, function(following){
								var text = following?"Unfollow":"Follow him!";
								$("#followers ul").append('<li><a href="profile.html?nickname='+user.result+'">@'+user.result+'</a> &nbsp&nbsp <button class="btn btn-default follow" data-nickname="'+user.result+'">'+text+'</button></li>');							
						});
					});
				} 
			});
			
			
		/**
		 * Handles toggles for the 'follow view' (i.e. display followers or display following)
		 */
		$(".followButtons button").click(function(){
			var type = $(this).data('type');
			if (type === 'followers') {
				$('.followButtons button[data-type="following"]').removeClass('active');
				$(this).addClass('active');
				$("#followers").show();
				$("#following").hide();
			}
			else if (type === 'following') {
				$('.followButtons button[data-type="followers"]').removeClass('active');
				$(this).addClass('active');
				$("#followers").hide();
				$("#following").show();
			}
		});
		
		
		$("#user_details .follow").data('nickname', ret["NickName"]);
		
		// If the servlet did not return false - display the information received
		if (ret.result === undefined) {
			var table = $("#user_details tbody");
			var fields = ['Username', 'NickName', 'Description'];
			if (ret.ProfileImage !== undefined) {
				table.append('<tr class="noborder"><td colspan="3"><img class="profile_pic" src="'+ret.ProfileImage+'"</td></tr>');
			}
			table.append('<tr class="space"><td colspan="3"></td></tr>');
			$.each(fields, function(i, field) {
				table.append('<tr><td>'+field+':</td><td class="space"></td><td>'+ret[field]+'</td></tr>');
			});
			
			//Get the user's 10 recent messages
			$.ajax({
				 type: 'get', 
				 dataType: 'json',
				 url: "GetMessage", 
				 data: {field: 'nickname', value: ret["NickName"]}, 
				 success: function(ret){
					 $.each(ret, function(i, message) {
						 addMessage(message); 
					 });
				 }
			 });
		}
	});
	
	
	
	
});