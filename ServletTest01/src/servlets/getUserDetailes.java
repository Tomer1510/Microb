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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Users;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

/**
 * Servlet implementation class getUserDetailes
 */
public class getUserDetailes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getUserDetailes() {
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
    			servletResult result = new servletResult("Invalid request");
    			response.getWriter().println(result.getJSONResult());
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
    		
    		Users resultUser = new Users(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5));
    		
    		PrintWriter writer = response.getWriter();
        	writer.println(convertToJSON.doConvert(resultUser));
        	writer.close();
    		
    		
		} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
