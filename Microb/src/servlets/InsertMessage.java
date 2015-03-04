package servlets;

import general.AppConstants;
import general.servletResult;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import model.Mentions;
import model.Topics;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;


/**
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 * 
 *  insert new message to the DB
 */

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
	 * 
	 * insert new message to the DB.
	 *
	 * @param  HttpServletRequest request - contain the parameters of the message [author, content, mentions and topics]
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 HttpSession session = request.getSession();
		 String nickname = (String)session.getAttribute("nickname");
		 String content = request.getParameter("content");
		 String topicsStr = request.getParameter("topics");
		 Integer republishID = request.getParameter("rebulish")!=null?Integer.parseInt((request.getParameter("republish"))):null;
		 if (nickname == null || content == null || topicsStr == null) {
			PrintWriter writer = response.getWriter();
         	writer.println(( new servletResult("false") ).getJSONResult());
         	writer.close();
         	return;
		 }
		try{
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
    		Connection conn = ds.getConnection();
    		Gson gson = new Gson();
    		Gson gson2 = new Gson();
    	 	//List<Mentions> mentions = gson.fromJson(request.getParameter("mentions"), new TypeToken<List<Mentions>>(){}.getType());
    	 	List<Topics> topics = gson2.fromJson(request.getParameter("topics"), new TypeToken<List<Topics>>(){}.getType());
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_MESSAGE_STMT, Statement.RETURN_GENERATED_KEYS);
    		
    		pstmt.setString(1, nickname);
    		pstmt.setString(2, content);
    		//pstmt.setString(3, new Gson().toJson(mentions));
    		pstmt.setString(3, new Gson().toJson(topics));

    		
    		pstmt.executeUpdate();
    		ResultSet lastID = (pstmt.getGeneratedKeys());
    		if (request.getHeader("referer") != null)
    			response.sendRedirect(request.getHeader("referer"));
    		if (lastID != null && lastID.next()) {
    			servletResult result = new servletResult("true", String.valueOf(lastID.getInt(1)));
        		PrintWriter writer = response.getWriter();
            	writer.println(result.getJSONResult());
            	writer.close();
    		}
    		
    		if (republishID != null && republishID > 0) {
	    		PreparedStatement pstmt2 = conn.prepareStatement(AppConstants.INCREAMENT_REPUBLISHED_MESSAGE);
	       		
	       		pstmt2.setInt(1, republishID);
	       		
	       		pstmt2.executeUpdate();
    		}
    		
    		
    		conn.commit();
    		
    		conn.close();
    		
		} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
