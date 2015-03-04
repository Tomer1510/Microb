$(document).ready(function(){
	var discover = "All";
	var topic = "";
	
	/**
	 * Update the "discover" part of the feed, this gets called after page load and afterwards with setInterval()
	 */
	function updateDiscover() {
		$.ajax({
			url: "GetFeed/"+discover, 
			dataType: 'json',
			type: 'GET',
			success: function(ret){	
				document.getElementById('discover_messages').innerHTML = "";
				$.each(ret, function(i, message) {
					addMessage(message, document.getElementById('discover_messages')); 
				}); 
			}
		});
	}
	
	
	/**
	 * Update the "topic" part of the feed, this gets called after page load and afterwards with setInterval()
	 */
	function updateTopic() {
		if (topic !== "") {		
			$.ajax({
				url: "GetMessagesOfTopic/"+topic,
				dataType: 'json',
				type: 'GET',
				success: function(messages){
					document.getElementById('topic_messages').innerHTML = "";
					$.each(messages, function(i, message) {
						addMessage(message, document.getElementById('topic_messages')); 
					}); 
				}
			});
		}
	}
	
	
	/**
	 * After validating the user's session it initializes the feed views, and initializes the setInterval() calls
	 * that update the views every 15 seconds
	 */
	isLoggedIn(function(isLogged){
		if (isLogged === true) {
			setInterval(function(){
				updateDiscover();
				updateTopic();
			}, 15000);
			updateDiscover();
			updateTopic();
		
			
		}
	});
	
	
	/**
	 * Event listener for topic links in posts - when the user clicks on a topic it updates the topic view
	 */
	$("body").on("click", ".topic", function(){
		topic = $(this).data('topic');
		updateTopic();
	});
	
	
	/**
	 * Event listener for the 2 view options in the "discover" view of the feed
	 */
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
		updateDiscover();
	});
	
});