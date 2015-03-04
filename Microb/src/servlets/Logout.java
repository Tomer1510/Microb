package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



/**
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 * 
 *  Performing logout action of the current user.
 */

public class Logout extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * Performing logout action of the current user.
	 */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
 
	    HttpSession session = request.getSession();
	    session.invalidate(); // end the session for the current user
	    response.sendRedirect("index.html?msg=loggedout");
	    
    	
    }
 
}