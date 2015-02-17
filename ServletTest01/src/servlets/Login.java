package servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import general.AppConstants;


@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
 
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
 
    	try{
    		
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
    		Connection conn = ds.getConnection();
	        String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        if (username == null || password == null)
	        {
	        	 PrintWriter out= response.getWriter();
		         out.println("INVALID");
	        }
	        PreparedStatement pstmt;
    		
    		pstmt = conn.prepareStatement(AppConstants.LOGIN_AUTHENTICATION);
    	
    		
    		pstmt.setString(1, username);
    		pstmt.setString(2, password);
    		ResultSet res = pstmt.executeQuery();
    		res.next();
	        if(res.getInt(1) == 1){
	            HttpSession session = request.getSession();
	            session.setAttribute("username", username);
	            session.setMaxInactiveInterval(AppConstants.SESSION_TTL);
	            PrintWriter out= response.getWriter();
	            out.println("SUCCESS");
	        }else{
	            PrintWriter out= response.getWriter();
	            out.println("FAIL");
	            return;
	        }
    	} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
 
    }
 
}