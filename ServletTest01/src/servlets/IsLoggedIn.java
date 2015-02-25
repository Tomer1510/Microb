package servlets;
import general.servletResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/IsLoggedIn")
public class IsLoggedIn extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
 
	    HttpSession session = request.getSession();
	    String username = (String)session.getAttribute("username");
	    if (username != null) {
	    	servletResult result = new servletResult("true", username);
			response.getWriter().println(result.getJSONResult());
	    } else {
	    	servletResult result = new servletResult("false");
			response.getWriter().println(result.getJSONResult());
	        return;
	    }
    	
    }
 
}