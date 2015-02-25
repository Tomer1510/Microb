function get_current_page() {
	return window.location.pathname.split('/').slice(2).join("");
}

window.onload = function(){
	$("#navbar").load("navbar.html", navbar_init);
	$("#sidebar").load("sidebar.html", sidebar_init);
	
};

var urlParams;
(window.onpopstate = function () {
    var match,
        pl     = /\+/g,  // Regex for replacing addition symbol with a space
        search = /([^&=]+)=?([^&]*)/g,
        decode = function (s) { return decodeURIComponent(s.replace(pl, " ")); },
        query  = window.location.search.substring(1);

    urlParams = {};
    while (match = search.exec(query))
       urlParams[decode(match[1])] = decode(match[2]);
})();