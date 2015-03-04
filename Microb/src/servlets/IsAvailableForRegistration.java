package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import general.AppConstants;
import general.servletResult;

/**
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 * 
 *  return whether the username/nickname is available for registration.
 */
@SuppressWarnings("deprecation")
public class IsAvailableForRegistration extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IsAvailableForRegistration() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * Return whether the username/nickname is available for registration.
	 *
	 * @param  HttpServletRequest request - contain the parameter 'field' that represent which filter to use - nickname or username
	 * 										and the parameter 'value' that represent the 'field' value.
	 * @param  HttpServletResponse response - contains JSON representation of the availability.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try{
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
    		Connection conn = ds.getConnection();
    		
    		String[] param = request.getPathInfo().replaceFirst("/", "").replaceAll("/", " ").split("\\s+"); // get parameters
    		String field = param[0]; // get 'field' parameter
    		String value = param[1]; // get 'value' parameter
    		
    		if ( (!field.equals("Nickname") && !field.equals("Username")) || value==null ) // sanity check
    		{
    			servletResult result = new servletResult("Invalid request");
    			response.getWriter().println(result.getJSONResult());
    			System.out.println("Invalid request");
    			return;
    		}
    		
    		PreparedStatement pstmt;
    		if (field.equals("Username")) { // checks whether to check availability by username
    			pstmt = conn.prepareStatement(AppConstants.COUNT_USER_BY_USERNAME_STMT);
    		} else { // or by nickname
    			pstmt = conn.prepareStatement(AppConstants.COUNT_USER_BY_NICKNAME_STMT);
    		}
    		
    		// the JSON result
    		pstmt.setString(1, value);
    		ResultSet res = pstmt.executeQuery();
    		res.next();
    		if(res.getInt(1) == 0) {
    			servletResult result = new servletResult("AVAILABLE");
    			response.getWriter().println(result.getJSONResult());
    			pstmt.close();
    			conn.close();
    			return;
    			
    		} else {
    			servletResult result = new servletResult("TAKEN");
    			response.getWriter().println(result.getJSONResult());
    			pstmt.close();
    			conn.close();
    			return;
    			
    		}
    		
    		
    		
		} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
