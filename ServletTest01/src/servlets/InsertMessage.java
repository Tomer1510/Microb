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

@WebServlet("/InsertMessage")

public class InsertMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 HttpSession session = request.getSession();
		 String username = (String)session.getAttribute("username");
		 String content = request.getParameter("content");
		 if (username == null || content == null) {
			PrintWriter writer = response.getWriter();
         	writer.println(( new servletResult("false") ).getJSONResult());
         	writer.close();
         	return;
		 }
		try{
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
    		Connection conn = ds.getConnection();
    		
 
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_MESSAGE_STMT, Statement.RETURN_GENERATED_KEYS);
    		
    		pstmt.setString(1, username);
    		pstmt.setString(2, content);
    		pstmt.executeUpdate();
    		ResultSet lastID = (pstmt.getGeneratedKeys());
    		if (lastID != null && lastID.next()) {
    			servletResult result = new servletResult("true", String.valueOf(lastID.getInt(1)));
        		PrintWriter writer = response.getWriter();
            	writer.println(result.getJSONResult());
            	writer.close();
    		}
    		conn.commit();
    		
    		conn.close();
    		
		} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
