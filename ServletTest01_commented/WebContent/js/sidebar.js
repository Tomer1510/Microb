function sidebar_init() {
	var current_page = get_current_page();
	if (current_page === "index.html" || current_page === "")
			$("#sidebar li[data-page='home']").addClass('active');
	if (current_page === "messages.html")
		$("#sidebar li[data-page='inbox']").addClass('active');
		
}