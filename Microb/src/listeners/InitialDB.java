package listeners;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import java.sql.PreparedStatement;

import general.AppConstants;

/**
 * Application Lifecycle Listener implementation class InitialDB
 *
 * Sets-up the Derby database's tables
 *
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 */
public class InitialDB implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public InitialDB() {    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  {	}

    /**
	 * Utility that checks whether a tables already exists.
	 *
	 * @param  SQLException e - exception created because of SQL error.
	 * @return boolean whether a tables already exists.
	 */
    private boolean tableAlreadyExists(SQLException e) {
        boolean exists;
        if(e.getSQLState().equals("X0Y32")) {
            exists = true;
        } else {
            exists = false;
        }
        return exists;
    }
    
    /**
	 * Utility that drop a table from the database.
	 *
	 * @param  Connection conn - connection to the DB.
	 * @param  ServletContext cntx - current server context
	 * @param  String table - the name of the table to be dropped;
	 */
    public void dropTable(Connection conn, ServletContext cntx, String table) {
    	
    	try {
			// Prepare the DROP statement
			PreparedStatement pstmt = conn.prepareStatement("DROP TABLE "+table);
			
			pstmt.executeUpdate();
    		
		}catch (SQLException e){
    		System.out.println("ERROR");
    		cntx.log("Error during database initialization",e);
			
		}

    }
    
    /*
	 * internal method for QA purpose.
	 *
	 * @param  Connection conn - connection to the DB.
	 * @param  ServletContext cntx - current server context
	 * @param  String table - the name of the table to be dropped;
	 */
    public void getColumns(Connection conn, ServletContext cntx, String table) {
    	
    	try {
			//Prepare the SQL statement
			PreparedStatement pstmt = conn.prepareStatement(AppConstants.GET_COLUMNS_BY_TABLE);
			pstmt.setString(1, table);
			pstmt.executeUpdate();
			
			conn.commit();
			pstmt.close();
		

    		
		}catch (SQLException e){
    		System.out.println("ERROR");
    		cntx.log("Error during database initialization",e);
			
		}

    }
    
    /**
	 * Utility that create a table on the database.
	 *
	 * @param  Connection conn - connection to the DB.
	 * @param  ServletContext cntx - current server context
	 * @param  String table - the name of the table to be created;
	 */
    public void createTable(Connection conn, ServletContext cntx, String stmt_str) {
       				
    		try{
    			//create requested table
    			Statement stmt = conn.createStatement();
    			stmt.executeUpdate(stmt_str);
    			
    			//commit update
    			conn.commit();
    			stmt.close();
    			
    		} catch (SQLException e){
    			//check if exception thrown since table was already created (so we created the database already 
    			//in the past
    			boolean created = tableAlreadyExists(e);
    			if (!created){
    				cntx.log("Error during database initialization",e);
    			}
    		}
    	
    }
    
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     * 
	 * Utility that initialize the database with the required tables.
	 *
	 * @param  ServletContextEvent event
	 *
     */
    public void contextInitialized(ServletContextEvent event)  { 
    	
    	ServletContext cntx = event.getServletContext();    	
    	try{   		
    		//obtain DB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
    		Connection conn = ds.getConnection();
    		//dropTable(conn, cntx, "Messages");
    		//dropTable(conn, cntx, "Users");
    		//dropTable(conn, cntx, "Following");
    		
    		// create required tables
	    	createTable(conn, cntx, AppConstants.CREATE_FOLLOWING_TABLE);	
	    	createTable(conn, cntx, AppConstants.CREATE_USER_TABLE);
	    	createTable(conn, cntx, AppConstants.CREATE_MESSAGES_TABLE);
	    	
	    	conn.commit();
	    	conn.close();
    	} catch (SQLException | NamingException e) {
    		//log error 
    		cntx.log("Error during database initialization",e);
    	}
    }
	
}
