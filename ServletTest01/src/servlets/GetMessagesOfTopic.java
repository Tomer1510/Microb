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
 * Servlet implementation class GetMessagesOfTopic
 */
public class GetMessagesOfTopic extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMessagesOfTopic() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String topic = request.getParameter("topic");
		 if (topic == null) {
			PrintWriter writer = response.getWriter();
        	writer.println(( new servletResult("false") ).getJSONResult());
        	writer.close();
        	return;
		 }
		try{
			Context context = new InitialContext();
   		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
   		Connection conn = ds.getConnection();
   		
   		PreparedStatement pstmt = conn.prepareStatement(String.format(AppConstants.SEARCH_MESSAGE_BY_TOPIC, topic));
   				   		
   		ResultSet res = pstmt.executeQuery();
   		List<Messages> messages = new ArrayList<Messages>();
   		int i = 1;
   		while(res.next() && i <= 10) {
   			Messages resultMessage = new Messages(res.getInt(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getTimestamp(6), res.getInt(7));
   			messages.add(resultMessage);
   			i++;
   		}
   		PrintWriter writer = response.getWriter();
       	writer.println(convertToJSON.doConvert(messages));
       	writer.close();
   		conn.close();
   	
		} catch (SQLException | NamingException e) {
   		getServletContext().log("Error while closing connection", e);
   		response.sendError(500);//internal server error
		}
	}

}
