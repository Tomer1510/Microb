package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Performing logout action of the current user.
 * 
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 */

@SuppressWarnings("deprecation")
public class Logout extends HttpServlet implements SingleThreadModel {
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