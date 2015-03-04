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

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

/**
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 * 
 *  return top-10 following of required user.
 */
@SuppressWarnings("deprecation")
public class GetTop10Following extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTop10Following() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * Return the JSON representation top-10 followings of required user.
	 .
	 * @param  HttpServletRequest request - contain the parameter 'nickname' that represent the user to filter by.
	 * @param  HttpServletResponse response - contains JSON representation of the top-10 users.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String nickname = request.getPathInfo().replaceFirst("/", ""); // get 'nickname' parameter
		if (nickname == null) { // sanity check
			PrintWriter writer = response.getWriter();
         	writer.println(( new servletResult("false") ).getJSONResult());
         	writer.close();
         	return;
		 }
		try{
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.SELECT_TOP_FOLLOWING_BY_NICKNAME);
    		pstmt.setString(1, nickname);
    		pstmt.setString(2, nickname);
    		
    		ResultSet res = pstmt.executeQuery();
    		
    		// getting all the required users
    		List<servletResult> users = new ArrayList<servletResult>(); 
    		int i = 1;
    		while(res.next() && i <= 10) {
    			servletResult resultUser = new servletResult(res.getString(1));
    			users.add(resultUser);
    			i++;
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
