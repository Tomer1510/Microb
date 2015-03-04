$(document).ready(function(){
	
	
	
	if (urlParams.nickname === undefined && urlParams.username === undefined)
		urlParams.nickname =  $.ajax({type: 'get', dataType: 'json', url: "IsLoggedIn", success: function(ret){return ret}, async: false }).responseJSON.value;
	isLoggedIn(function(ret, nickname){
		if (ret === true && nickname === urlParams.nickname)
			$("#user_details .follow").remove();
	});
	var field = (urlParams.nickname === undefined)?"Username":"Nickname", 
			value = (urlParams.nickname === undefined)?urlParams.username:urlParams.nickname;
	getUserDetails(field, value, function(ret){		
			isFollowing(ret["NickName"], function(following){
				$("#user_details .follow").text(following?"Unfollow":"Follow him!");
			});
			
			
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