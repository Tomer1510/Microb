package general;

import java.lang.reflect.Type;

import model.Users;

import com.google.gson.reflect.TypeToken;


/**
 * A simple place to hold global application constants
 */
public interface AppConstants {

	// HTTP
	public final int SESSION_TTL = 60*10; //10 Minutes
	
	// GSON
	public final Type USER_GSON_TYPE = new TypeToken<Users>() {}.getType();
	
	// DERBY
	public final String DB_NAME = "UserDB";
	public final String DB_DATASOURCE = "java:comp/env/jdbc/UserDatasource";
	public final String PROTOCOL = "jdbc:derby:"; 
	
	// SQL CREATE/INSERT
	public final String CREATE_USER_TABLE = "CREATE TABLE Users(Username varchar(10),"
			+ "Password varchar(8),"
			+ "Nickname varchar(20),"
			+ "Description varchar(100),"
			+ "ProfileImage varchar(100))";
	public final String INSERT_USER_STMT = "INSERT INTO Users VALUES(?,?,?,?,?)";
	
	// SQL SELECT
	public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMER";
	public final String SELECT_USER_BY_USERNAME_STMT = "SELECT * FROM Users "
			+ "WHERE Username=?";
	public final String SELECT_USER_BY_NICKNAME_STMT = "SELECT * FROM Users "
			+ "WHERE Nickname=?";
	
	// SQL COUNT
	public final String COUNT_USER_BY_USERNAME_STMT = "SELECT COUNT(*) as cnt FROM Users "
			+ "WHERE Username=?";
	public final String COUNT_USER_BY_NICKNAME_STMT = "SELECT COUNT(*) as cnt FROM Users "
			+ "WHERE Nickname=?";
	public final String LOGIN_AUTHENTICATION = "SELECT COUNT(*) as cnt FROM Users WHERE Username=? AND Password=?";
	
}