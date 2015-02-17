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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import general.AppConstants;

/**
 * Servlet implementation class RegisterUser
 */
public class IsAvailableForRegistration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IsAvailableForRegistration() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try{
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
    		Connection conn = ds.getConnection();
    		
    		String field = request.getParameter("field");
    		if ( (!field.equals("Nickname") && !field.equals("Username")) || request.getParameter("value")==null )
    		{
    			response.getWriter().println("Invalid request");
    			System.out.println("Invalid request");
    			return;
    		}
    		
    		String value = request.getParameter("value");
    		PreparedStatement pstmt;
    		if (field.equals("Username")) {
    			pstmt = conn.prepareStatement(AppConstants.SELECT_USER_BY_USERNAME_STMT);
    		} else {
    			pstmt = conn.prepareStatement(AppConstants.SELECT_USER_BY_NICKNAME_STMT);
    		}
    		
    		pstmt.setString(1, value);
    		ResultSet res = pstmt.executeQuery();
    		res.next();
    		if(res.getInt(1) == 0) {
    			System.out.println("AVAILABLE");
    			response.getWriter().println("AVAILABLE");
    			pstmt.close();
    			conn.close();
    			return;
    			
    		} else {
    			System.out.println("TAKEN");
    			response.getWriter().println("TAKEN");
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
