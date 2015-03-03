package servlets;

import general.AppConstants;
import general.servletResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

/**
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 * 
 *  updating the DB when republish is done.
 */
public class Republish extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Republish() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * updating the DB when republish is done.
	 *
	 * @param  HttpServletRequest request - contain the parameter 'messageID' that represent the massage to update
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String messageID = request.getParameter("messageID"); // get 'messageID' parameter
		
		 if (messageID == null) { // sanity check
			PrintWriter writer = response.getWriter();
        	writer.println(( new servletResult("false") ).getJSONResult());
        	writer.close();
        	return;
		 }
		try{
			Context context = new InitialContext();
   		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
   		Connection conn = ds.getConnection();
   		
   		// performing updating
   		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INCREAMENT_REPUBLISHED_MESSAGE);
   		
   		pstmt.setString(1, messageID);
   		
   		pstmt.executeUpdate();
   		
   		conn.commit();
   		
   		conn.close();
   		
		} catch (SQLException | NamingException e) {
   		getServletContext().log("Error while closing connection", e);
   		response.sendError(500);//internal server error
   	}

		
	}

}
