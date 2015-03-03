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
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import general.AppConstants;
import general.servletResult;

/**
 * Servlet implementation class RegisterUser
 */
public class RegisterUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterUser() {
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
    		
    		String username = request.getParameter("Username");
    		String password = request.getParameter("Password");
    		String nickname = request.getParameter("Nickname");
    		String description = request.getParameter("Description");
    		String profileImage = request.getParameter("ProfileImage");
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.COUNT_USER_BY_USERNAME_STMT);
    		
    		pstmt.setString(1, username);
    		
    		ResultSet res = pstmt.executeQuery();
    		res.next();
    		if(res.getInt(1) != 0)
    		{
    			servletResult result = new servletResult("User already exist");
    			response.getWriter().println(result.getJSONResult());
    			conn.close();

    			return;
    		}
    		
    		pstmt = conn.prepareStatement(AppConstants.INSERT_USER_STMT);
    		
    		pstmt.setString(1, username);
    		pstmt.setString(2, password);
    		pstmt.setString(3, nickname);
    		pstmt.setString(4, description);
    		pstmt.setString(5, profileImage);
    		
    		pstmt.executeUpdate();
    		
    		conn.commit();
    		pstmt.close();
			conn.close();
			HttpSession session = request.getSession();
	        session.setAttribute("nickname", nickname);
	        session.setMaxInactiveInterval(AppConstants.SESSION_TTL);	
    		servletResult result = new servletResult("SUCCESS");
			response.getWriter().println(result.getJSONResult());
    		
    		
		} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
