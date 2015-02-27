$(document).ready(function(){
	
	
	
	if (urlParams.nickname === undefined && urlParams.username === undefined)
		urlParams.username =  $.ajax({type: 'get', dataType: 'json', url: "IsLoggedIn", success: function(ret){return ret}, async: false }).responseJSON.value;
	$.ajax({
		url: 'GetUserDetails',
		type: 'POST',
		dataType: 'json',
		data: {
			field: (urlParams.nickname === undefined)?"Username":"Nickname", 
			value: (urlParams.nickname === undefined)?urlParams.username:urlParams.nickname
		},
		success: function(ret) {
			$("#user_details .follow").text(isFollowing(ret["NickName"])?"Unfollow":"Follow");
			
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
		}
	});
	
	
	
	 
	
	
});