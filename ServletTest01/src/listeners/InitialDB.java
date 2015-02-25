package listeners;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.sql.PreparedStatement;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import general.AppConstants;

/**
 * Application Lifecycle Listener implementation class InitialDB
 *
 */
public class InitialDB implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public InitialDB() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

  //utility that checks whether the customer tables already exists
    private boolean tableAlreadyExists(SQLException e) {
        boolean exists;
        if(e.getSQLState().equals("X0Y32")) {
            exists = true;
        } else {
            exists = false;
        }
        return exists;
    }
    
    public void getColumns(ServletContextEvent event, String table) {
    	ServletContext cntx = event.getServletContext();
    	
		try{
			//obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(AppConstants.GET_COLUMNS);
			pstmt.setString(1, table);
			ResultSet res = pstmt.executeQuery();
			
			while (res.next()) {
				System.out.println(res.getString(1) +"\t"+res.getString(2));
			}
			//commit update
			conn.close();

    		
		}catch (SQLException | NamingException e){
    		System.out.println("ERROR");
    		cntx.log("Error during database initialization",e);
			
		}
		//close connection

    }
    
    
    public void createTable(ServletContextEvent event, String stmt_str) {
    	ServletContext cntx = event.getServletContext();
    	
    	try{
    		
    		//obtain CustomerDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
    		Connection conn = ds.getConnection();
    		
    		try{
    			//create Users table
    			Statement stmt = conn.createStatement();
    			stmt.executeUpdate(stmt_str);
    			
    			//commit update
    			conn.commit();
    			stmt.close();
    			conn.close();
    		} catch (SQLException e){
    			//check if exception thrown since table was already created (so we created the database already 
    			//in the past
    			boolean created = tableAlreadyExists(e);
    			if (!created){
    				throw e;//re-throw the exception so it will be caught in the
    				//external try..catch and recorded as error in the log
    			}
    		}
    	} catch (SQLException | NamingException e) {
    		//log error 
    		cntx.log("Error during database initialization",e);
    	}
    }
    
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event)  { 
    	createTable(event, AppConstants.CREATE_USER_TABLE);
    	createTable(event, AppConstants.CREATE_MESSAGES_TABLE);

    }
	
}
