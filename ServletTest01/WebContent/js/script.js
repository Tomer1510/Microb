function get_current_page() {
	return window.location.pathname.split('/').slice(2).join("");
}

window.onload = function(){
	$("#navbar").load("navbar.html", navbar_init);
	$("#sidebar").load("sidebar.html", sidebar_init);
	
};

