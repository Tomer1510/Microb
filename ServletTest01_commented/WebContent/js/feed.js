$(document).ready(function(){
	isLoggedIn(function(isLogged){
		if (isLogged === true) {
			$.getJSON("GetFeed", function(ret){
				$.each(ret, function(i, message) {
					console.log(ret);
					addMessage(message); 
				}); 
			});
		}
	});
});