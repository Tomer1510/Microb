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
import javax.servlet.http.HttpSession;

import model.Messages;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

/**
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 * 
 *  return users DISCOVERY messages as defined in the requirements doc.
 */

@SuppressWarnings("deprecation")
public class GetFeed extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFeed() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * Return the JSON representation of DISCOVERY messages as defined in the requirements doc.
	 *
	 * @param  HttpServletRequest request - contain the parameter 'returns' that represent which feed to return -
	 * 										from all users or only from users that the user is following after.
	 * @param  HttpServletResponse response - contains JSON representation of DISCOVERY messages.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String nickname = (String)session.getAttribute("nickname"); // get current user nickname
		String returns = request.getPathInfo(); // get 'returns' parameter
		if (nickname == null || returns == null) { // sanity check
			PrintWriter writer = response.getWriter();
         	writer.println(( new servletResult("false sanity") ).getJSONResult());
         	writer.close();
         	return;
		 }
		try{
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt;
    		if(returns.equals("/Following")) // checks whether to return only messages of followings
    		{	pstmt = conn.prepareStatement(AppConstants.SELECT_MESSAGES_BY_FOLLOWING); }
    		else if(returns.equals("/All")) // or from all users
    		{	pstmt = conn.prepareStatement(AppConstants.SELECT_ALL_MESSAGES_OF_NICKNAME_STMT); }
    		else { // sanity check
    			PrintWriter writer = response.getWriter();
             	writer.println(( new servletResult("false arg") ).getJSONResult());
             	writer.close();
             	return;
    		}
    		pstmt.setString(1, nickname);
    		
    		ResultSet res = pstmt.executeQuery();
    		
    		// getting all the required messages
    		List<Messages> messages = new ArrayList<Messages>(); 
    		int i = 1;
    		while(res.next() && i <= 10) {
    			Messages resultMessage = new Messages(res.getInt(1), res.getString(2), res.getString(3), res.getString(4), res.getTimestamp(5), res.getInt(6));
    			messages.add(resultMessage);
    			i++;
    		}
    		PrintWriter writer = response.getWriter();
        	writer.println(convertToJSON.doConvert(messages)); // converting to JSON
        	writer.close();
    		conn.close();
    		
   
    		
		} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
