$(document).ready(function(){
	function addMessage(message) {
		var newMsg = '<div class = "col-md-12 panel panel-default">'
			+'<div class="panel-heading"><div style="display: inline-block;">'+message.authorNickname+'</div><div style="float: right; display: inline-block;">'+message.timestamp+'</div></div>'
			+'<div class="panel-body">'+message.content+'</div></div>';
		
		document.getElementById('messages').innerHTML += newMsg;
			
	}
	
	
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
			if (ret.result === undefined) {
				var table = $("#user_details");
				var fields = ['Username', 'NickName', 'Description'];
				if (ret.ProfileImage !== undefined) {
					table.append('<tr><td colspan="3"><img class="profile_pic" src="'+ret.ProfileImage+'"</td></tr>');
				}
				table.append('<tr class="space"><td colspan="3"></td></tr>');
				$.each(fields, function(i, field) {
					table.append('<tr><td>'+field+':</td><td class="space"></td><td>'+ret[field]+'</td></tr>');
				});
				
				
				$.ajax({
					 type: 'get', 
					 dataType: 'json',
					 url: "GetMessage", 
					 data: {field: 'username', value: urlParams.username}, 
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