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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import general.AppConstants;
import general.servletResult;


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
	        	servletResult result = new servletResult("INVALID");
    			response.getWriter().println(result.getJSONResult());
	        }
	        PreparedStatement pstmt;
    		
    		pstmt = conn.prepareStatement(AppConstants.LOGIN_AUTHENTICATION);
    	
    		
    		pstmt.setString(1, username);
    		pstmt.setString(2, password);
    		ResultSet res = pstmt.executeQuery();
	        if(res.next() != false){
	        	String nickname = res.getString(3);
	            HttpSession session = request.getSession();
	            session.setAttribute("nickname", nickname);
	            session.setMaxInactiveInterval(AppConstants.SESSION_TTL);	       
	            servletResult result = new servletResult("SUCCESS");
    			response.getWriter().println(result.getJSONResult());
	        }else{
	        	servletResult result = new servletResult("FAIL");
    			response.getWriter().println(result.getJSONResult());
	            return;
	        }
    	} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
 
    }
 
}