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
				$("#user_details .follow").text(following?"Unfollow":"Follow");
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