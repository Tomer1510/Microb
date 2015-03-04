$(document).ready(function(){
	
	function updateFeed() {
		$.ajax({
			url: "GetFeed", 
			dataType: 'json',
			type: 'POST',
			data: {returns: 'Following'},
			success: function(ret){	
				document.getElementById('discover_messages').innerHTML = "";
				$.each(ret, function(i, message) {
					addMessage(message, document.getElementById('discover_messages')); 
				}); 
			}
		});
	}
	
	isLoggedIn(function(isLogged){
		if (isLogged === true) {
			setInterval(function(){
				updateFeed();
			}, 15000);
			updateFeed();
			
		
			
		}
	});
	
	$("body").on("click", ".topic", function(){
		var topic = $(this).data('topic');
		document.getElementById('topic_messages').innerHTML = "";
		$.ajax({
			url: "GetMessagesOfTopic",
			dataType: 'json',
			type: 'POST',
			data: {topic: topic},
			success: function(messages){
				$.each(messages, function(i, message) {
					addMessage(message, document.getElementById('topic_messages')); 
				}); 
			}
		});
	});
	
});