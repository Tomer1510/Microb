function sidebar_init() {
	if (window.location.pathname.indexOf('index.html') !== -1)
			$("#sidebar li[data-page='home']").addClass('active');
	if (window.location.pathname.indexOf('messages.html') !== -1)
		$("#sidebar li[data-page='inbox']").addClass('active');
		
}