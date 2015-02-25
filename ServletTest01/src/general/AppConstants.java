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
	
	public final String CREATE_MESSAGES_TABLE = "CREATE TABLE Messages(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "AuthorNickname varchar(10),"
			+ "Content varchar(200),"
			+ "Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
			+ "CONSTRAINT primary_key PRIMARY KEY (ID))";
	public final String INSERT_MESSAGE_STMT = "INSERT INTO Messages(AuthorNickname, Content) VALUES(?,?)";
	public final String GET_COLUMNS = "select TABLENAME,COLUMNNAME, t.*  FROM sys.systables t, sys.syscolumns  WHERE TABLEID = REFERENCEID ";
	
	// SQL SELECT
	public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMER";
	public final String SELECT_USER_BY_USERNAME_STMT = "SELECT * FROM Users "
			+ "WHERE Username=?";
	public final String SELECT_USER_BY_NICKNAME_STMT = "SELECT * FROM Users "
			+ "WHERE Nickname=?";

	public final String SELECT_MESSAGE_BY_ID_STMT = "SELECT * FROM Messages WHERE ID=?";
	public final String SELECT_MESSAGE_BY_USERNAME_STMT = "SELECT * FROM Messages WHERE AuthorNickname=? ORDER BY Timestamp DESC";

	// SQL COUNT
	public final String COUNT_USER_BY_USERNAME_STMT = "SELECT COUNT(*) as cnt FROM Users "
			+ "WHERE Username=?";
	public final String COUNT_USER_BY_NICKNAME_STMT = "SELECT COUNT(*) as cnt FROM Users "
			+ "WHERE Nickname=?";
	public final String LOGIN_AUTHENTICATION = "SELECT COUNT(*) as cnt FROM Users WHERE Username=? AND Password=?";
	
}