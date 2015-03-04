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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Messages;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

/**
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 * 
 *  return required messages by msg-ID or nickname.
 */

public class GetMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMessage() {
        super();
    }
    
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * Return the JSON representation of required messages by msg-ID or nickname.
	 .
	 * @param  HttpServletRequest request - contain the parameter 'field' that represent which filter to use - nickname or msg-ID
	 * 										and the parameter 'value' that represent the 'field' value.
	 * @param  HttpServletResponse response - contains JSON representation of the messages.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String[] param = request.getPathInfo().replaceFirst("/", "").replaceAll("/", " ").split("\\s+"); // get parameters
		String field = param[0]; // get 'field' parameter
		String value = param[1]; // get 'value' parameter
		
		 if (field == null || value == null) { // sanity check
			PrintWriter writer = response.getWriter();
         	writer.println(( new servletResult("false") ).getJSONResult());
         	writer.close();
         	return;
		 }
		try{
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
    		Connection conn = ds.getConnection();
    		PreparedStatement pstmt;
    		switch (field) {
    			case "nickname":	pstmt = conn.prepareStatement(AppConstants.SELECT_MESSAGE_BY_NICKNAME_STMT); pstmt.setString(1, value);
    				break; // checks whether to return messages by nickname
    			case "id":  pstmt = conn.prepareStatement(AppConstants.SELECT_MESSAGE_BY_ID_STMT); pstmt.setInt(1, Integer.parseInt(value));
    				break; // or by msg-ID
    				
    			default: return;

    		}
    		
    		// getting all the required messages
    		ResultSet res = pstmt.executeQuery();
    		List<Messages> messages = new ArrayList<Messages>();
    		int i = 1;
    		while(res.next() && i <= 10) {
    			Messages resultMessage = new Messages(res.getInt(1), res.getString(2), res.getString(3), res.getString(4), res.getTimestamp(5), res.getInt(6));
    			messages.add(resultMessage);
    			i++;
    		}
    		PrintWriter writer = response.getWriter();
        	writer.println(convertToJSON.doConvert(messages)); // convert to JSON
        	writer.close();
    		conn.close();
    		
   
    		
		} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
