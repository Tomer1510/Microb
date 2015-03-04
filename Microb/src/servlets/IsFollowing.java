package servlets;

import general.AppConstants;
import general.servletResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;



/**
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 * 
 *  return whether the current user is following after some user.
 */

public class IsFollowing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IsFollowing() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * Return whether the username/nickname is available for registration.
	 *
	 * @param  HttpServletRequest request - contain the parameter 'nickname' for filtering the results
	 * @param  HttpServletResponse response - contains JSON representation of the answer.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 HttpSession session = request.getSession();
		 String sourceNickname = (String)session.getAttribute("nickname"); // get nickname of the current user parameter
		 String targetNickname = request.getPathInfo().replaceFirst("/", ""); // get 'nickname' parameter
		 if (sourceNickname == null || targetNickname == null) { // sanity check
			PrintWriter writer = response.getWriter();
         	writer.println(( new servletResult("false") ).getJSONResult());
         	writer.close();
         	return;
		 }
		try{
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.SELECT_FOLLOWING);
    		
    		pstmt.setString(1, sourceNickname);
    		pstmt.setString(2, targetNickname);
    	
    		ResultSet res = pstmt.executeQuery();
    		
    		// the JSON result
    		if (res.next() != false && res.getInt(1) != 0) {
    			servletResult result = new servletResult("true", "true");
        		PrintWriter writer = response.getWriter();
            	writer.println(result.getJSONResult());
            	writer.close();
    		} else {
    			servletResult result = new servletResult("true", "false");
        		PrintWriter writer = response.getWriter();
            	writer.println(result.getJSONResult());
            	writer.close();
    		}
    		
    		pstmt.close();		   		
    		conn.commit();   		
    		conn.close();
    		
		} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
