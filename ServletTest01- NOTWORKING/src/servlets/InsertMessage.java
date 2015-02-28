package servlets;

import general.AppConstants;
import general.convertToJSON;
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

import model.Users;
import model.Mentions;

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 HttpSession session = request.getSession();
		 String nickname = (String)session.getAttribute("nickname");
		 String content = request.getParameter("content");
		 String mentionsStr = request.getParameter("mentions");
		
		 if (nickname == null || content == null || mentionsStr == null) {
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
    	 	List<Mentions> mentions = gson.fromJson(request.getParameter("mentions"), new TypeToken<List<Mentions>>(){}.getType());
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_MESSAGE_STMT, Statement.RETURN_GENERATED_KEYS);
    		
    		pstmt.setString(1, nickname);
    		pstmt.setString(2, content);
    		pstmt.setString(3, new Gson().toJson(mentions));
    		
    		
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
    		conn.commit();
    		
    		conn.close();
    		
		} catch (SQLException | NamingException e) {
    		getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
    	}
	}

}
