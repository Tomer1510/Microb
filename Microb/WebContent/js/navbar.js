
var navbar_init = function() {
	var republish = 0; //We user this to store the messageID of the republished message (in the event that the user republishes a message)
	
	
	/**
	 * If the user is not logged in, it redirects to the registration/login page
	 * If the user is logged in, it updates the "logged in as" span in the navbar
	 */
	isLoggedIn(function(ret, nickname){
		if (ret === false) {
			window.location = "register.html";
		}
		else if (ret === true){
			$("#navbar-nickname").html(nickname);
			window.nickname=nickname;
		}
	});
		
	
	
	/**
	 * Helper function that creates a new hidden element with the value it gets as a parameter
	 */
	function addHiddenElm(name, value) {
		var elm = document.createElement("input");
		elm.type = "hidden";
		elm.name = name;
		elm.value = JSON.stringify(value);
		return elm;
	}
	
	
	/**
	 * Handles the new message form submit
	 * Validates the content (makes sure it's no longer than 140 letters), pre-processes the topics and finally adds the values that the servlets expects
	 */
	$("#post-form").submit(function(){
		
		//First validate that the message contents don't exceed the 140 letter limit
		var text = $(this).find("textarea[name=content]").val();
		if (text.length > 140)
			return false;	
		
		//Pre-process topics in the message so they can be stored in the database
		var topics = [];
		text.split(/[\s\n]+/).forEach(function(word){
			if(word[0] === '#' && word.length > 1)
				topics.push({'topic': word.substr(1).trim()});
		});
		
		//Add hidden elements to the form for the topics we parsed and the republish var
		$(this).append(addHiddenElm("topics", topics));
		$(this).append(addHiddenElm("republish", republish));
	});
	
	
	
	
	/**
	 * Event listener for the message textarea - adds the letter counter functionality for the user
	 */
	$("#post-form textarea[name=content]").keyup(function(){
		var len = $(this).val().length;
		$("#post-form .letter-counter").text(len)
		if (len > 140) {
			$("#post-form .letter-counter").addClass('red');
		}
		else {
			$("#post-form .letter-counter").removeClass('red');
		}
	});
	
	
	
	/**
	 * Handles clicks on 'republish' buttons
	 * It sets the republish variable, opens the new message modal and sets its contents to the original message
	 */
	$("body").on('click', '.republish', function(){
		var originalMessage = $(this).parent().find('.message-content').text();
		$("#post-form textarea[name=content]").val("RE: "+originalMessage+"\n").keyup();
		republish = $(this).parents('.message').data('id');
	});
	
	
	
	
	/**
	 * Handles clicks on 'reply' buttons
	 * It opens the new message modal and sets its contents to the original message's author (i.e. adds the mention)
	 */
	$("body").on('click', '.reply', function(){
		var author = $(this).parent().find('.author').text();
		$("#post-form textarea[name=content]").val(author+"\n").keyup();
	});
	
	
	
	/**
	 * Event listener for the 'cancel' button in the new post modal
	 * In case the user clicks on cancel, we clear the contents of the unfinished new post, and therefore also clear the republish var
	 */
	$('#post-form button[data-dismiss="modal"]').click(function(){
		republish = 0;
		$("#post-form textarea[name=content]").val("");
	})
	
};