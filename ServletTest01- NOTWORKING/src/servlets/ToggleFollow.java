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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Users;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

@WebServlet("/ToggleFollow")

public class ToggleFollow extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ToggleFollow() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 HttpSession session = request.getSession();
		 String sourceNickname = (String)session.getAttribute("nickname");
		 String targetNickname = request.getParameter("nickname");
		 if (sourceNickname == null || targetNickname == null ) {
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
    		
    		
    		String following = "";
    		if (res.next() != false && res.getInt(1) != 0) {
    			PreparedStatement pstmt2 = conn.prepareStatement(AppConstants.DELETE_FOLLOWING);
    			pstmt2.setString(1, sourceNickname);
    			pstmt2.setString(2, targetNickname);
    			pstmt2.executeUpdate();
    			pstmt2.close();
        		following = "false";
    		} else {
    			PreparedStatement pstmt2 = conn.prepareStatement(AppConstants.INSERT_FOLLOWING);
    			pstmt2.setString(1, sourceNickname);
    			pstmt2.setString(2, targetNickname);
    			pstmt2.executeUpdate();
    			pstmt2.close();
        		following = "true";
    		}
    		pstmt.close();
			servletResult result = new servletResult("true", following);
    		PrintWriter writer = response.getWriter();
        	writer.println(result.getJSONResult());
        	writer.close();
    		
    		conn.commit();
    		
    		conn.close();
    		
		} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
