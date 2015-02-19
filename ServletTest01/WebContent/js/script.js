window.onload = function(){
	$("#navbar").load("navbar.html", navbar_init);
	$("#sidebar").load("sidebar.html", sidebar_init);
	
	$("#navbar").addClass('col-md-8');
	$("#sidebar").addClass('col-md-2');
	
};

