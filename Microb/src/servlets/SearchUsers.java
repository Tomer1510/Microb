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
import java.util.ArrayList;
import java.util.List;

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
 *  return list of users that match given keyword.
 */

@SuppressWarnings("deprecation")
public class SearchUsers extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchUsers() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * return JSON representation list of users that match given keyword.
	 *
	 * @param  HttpServletRequest request - contain the parameter 'keyword' for the SQL searching statement
	 * @param  HttpServletResponse response - contains JSON representation of the availability.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String keyword = request.getPathInfo().replaceFirst("/", ""); // get 'keyword' parameter
		if (keyword == null) { // sanity check
			PrintWriter writer = response.getWriter();
         	writer.println(( new servletResult("false") ).getJSONResult());
         	writer.close();
         	return;
		 }
		try{
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(String.format(AppConstants.SEARCH_USERS_BY_NICKNAME, keyword)); 
    		
    		// getting all the required users
    		ResultSet res = pstmt.executeQuery();
    		List<Users> users = new ArrayList<Users>(); 
    		while(res.next()) {
    			Users user = new Users(res.getString(1), res.getString(3), res.getString(4), res.getString(5));
    			users.add(user);
    		}
    		PrintWriter writer = response.getWriter();
        	writer.println(convertToJSON.doConvert(users)); // converting to JSON
        	writer.close();
    		conn.close();
    		
   
    		
		} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
