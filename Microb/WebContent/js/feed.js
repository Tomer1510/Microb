$(document).ready(function(){
	var discover = "All";
	function updateFeed() {
		$.ajax({
			url: "GetFeed", 
			dataType: 'json',
			type: 'POST',
			data: {returns: discover},
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
	
	
	$(".discover-toggle").click(function(){
		var type = $(this).data('discover');
		if (type === "All") {
			$(this).addClass('active');
			$('.discover-toggle[data-discover="Following"]').removeClass("active");
		}
		else if (type === "Following") {
			$(this).addClass('active');
			$('.discover-toggle[data-discover="All"]').removeClass("active");					
		}
		discover = type;
		updateFeed();
	});
	
});