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

/**
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 * 
 *  return whether the user is currently logged-in.
 */

public class IsLoggedIn extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     * 
     * Return whether the user is currently logged-in.
     *
     * @param  HttpServletRequest request - contain the parameter 'nickname' for filtering the results
     * @param  HttpServletResponse response - contains JSON representation of the answer.
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
 
	    HttpSession session = request.getSession();
	    String nickname = (String)session.getAttribute("nickname"); // get 'nickname' parameter
	    if (nickname != null) {
	    	servletResult result = new servletResult("true", nickname);
			response.getWriter().println(result.getJSONResult());
	    } else {
	    	servletResult result = new servletResult("false");
			response.getWriter().println(result.getJSONResult());
	        return;
	    }
    	
    }
 
}