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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Messages;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

@WebServlet("/GetMessage")

public class GetMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 String field = request.getParameter("field");
		 String value = request.getParameter("value");
		 if (field == null || value == null) {
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
    				break;
    			case "id":  pstmt = conn.prepareStatement(AppConstants.SELECT_MESSAGE_BY_ID_STMT); pstmt.setInt(1, Integer.parseInt(value));
    				break;
    				
    			default: return;

    		}
    		
    		ResultSet res = pstmt.executeQuery();
    		List<Messages> messages = new ArrayList<Messages>(); 
    		while(res.next()) {
    			Messages resultMessage = new Messages(res.getInt(1), res.getString(2), res.getString(3), res.getString(4), res.getTimestamp(5), res.getInt(6));
    			messages.add(resultMessage);
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
