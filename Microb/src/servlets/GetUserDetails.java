package servlets;

import general.AppConstants;
import general.convertToJSON;
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
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Users;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

/**
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 * 
 *  return details of required user.
 */
@SuppressWarnings("deprecation")
public class GetUserDetails extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * Return the JSON representation of required user.
	 *
	 * @param  HttpServletRequest request - contain the parameter 'field' that represent which filter to use - nickname or username
	 * 										and the parameter 'value' that represent the 'field' value.
	 * @param  HttpServletResponse response - contains JSON representation of the user.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
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
    		if (field.equals("Username")) { // checks whether to return user by username
    			pstmt = conn.prepareStatement(AppConstants.SELECT_USER_BY_USERNAME_STMT);
    		} else { // or by nickname
    			pstmt = conn.prepareStatement(AppConstants.SELECT_USER_BY_NICKNAME_STMT);
    		}
    		
    		// getting the required user details
    		pstmt.setString(1, value);
    		ResultSet res = pstmt.executeQuery();
    		if (!res.next()) {
    			PrintWriter writer = response.getWriter();
            	writer.println(( new servletResult("false") ).getJSONResult());
            	writer.close();
            	conn.close();
            	return;
    		}
    		
    		Users resultUser = new Users(res.getString(1), res.getString(3), res.getString(4), res.getString(5));
    		
    		PrintWriter writer = response.getWriter();
        	writer.println(convertToJSON.doConvert(resultUser)); // converting to JSON
        	writer.close();
    		conn.close();
    		
		} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
