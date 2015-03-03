function sidebar_init() {
	var current_page = get_current_page();
	if (current_page === "index.html" || current_page === "")
			$("#sidebar li[data-page='home']").addClass('active');
	if (current_page === "profile.html")
		$("#sidebar li[data-page='profile']").addClass('active');
		
}