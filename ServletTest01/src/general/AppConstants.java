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
			+ "Mentions varchar(200),"
			+ "Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
			+ "PRIMARY KEY (ID))";
	public final String INSERT_MESSAGE_STMT = "INSERT INTO Messages(AuthorNickname, Content, Mentions) VALUES(?,?,?)";
	public final String INSERT_FOLLOWING = "INSERT INTO Following(FollowerNickname, FollowingNickname) VALUES(?,?)";
	public final String DELETE_FOLLOWING = "DELETE FROM Following WHERE FollowerNickname=? AND FollowingNickname=?";

	public final String CREATE_FOLLOWING_TABLE = "CREATE TABLE Following(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "FollowerNickname varchar(10),"
			+ "FollowingNickname varchar(200),"
			+ "PRIMARY KEY (ID))";
	
	
	/*
	 * "CREATE TABLE Following(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "FollowerUsername varchar(10),"
			+ "FollowingNickname varchar(10),"
			+ "CONSTRAINT primary_key PRIMARY KEY (ID))";
	 */
	// SQL SELECT
	public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMER";
	public final String SELECT_USER_BY_USERNAME_STMT = "SELECT * FROM Users "
			+ "WHERE Username=?";
	public final String SELECT_USER_BY_NICKNAME_STMT = "SELECT * FROM Users "
			+ "WHERE Nickname=?";

	public final String SELECT_MESSAGE_BY_ID_STMT = "SELECT * FROM Messages WHERE ID=?";
	public final String SELECT_MESSAGE_BY_NICKNAME_STMT = "SELECT * FROM Messages WHERE AuthorNickname=? ORDER BY Timestamp DESC";
	public final String GET_COLUMNS_BY_TABLE = "select TABLENAME,COLUMNNAME, t.*  FROM sys.systables t, sys.syscolumns  WHERE TABLEID = REFERENCEID and tablename = ?";
	public final String GET_COLUMNS = "select TABLENAME,COLUMNNAME, t.*  FROM sys.systables t, sys.syscolumns  WHERE TABLEID = REFERENCEID";

	/*  */
	
	// SQL COUNT
	public final String COUNT_USER_BY_USERNAME_STMT = "SELECT COUNT(*) as cnt FROM Users "
			+ "WHERE Username=?";
	public final String COUNT_USER_BY_NICKNAME_STMT = "SELECT COUNT(*) as cnt FROM Users "
			+ "WHERE Nickname=?";
	public final String LOGIN_AUTHENTICATION = "SELECT * FROM Users WHERE Username=? AND Password=?";
	public final String COUNT_FOLLOWERS_BY_NICKNAME = "SELECT COUNT(*) as cnt FROM Following "
			+ "WHERE FollowingUsername=?";
	public final String SELECT_FOLLOWING = "SELECT COUNT(*) as cnt FROM Following WHERE FollowerNickname=? AND FollowingNickname=?";

	public final String SELECT_MESSAGES_BY_FOLLOWING = "SELECT Messages.* FROM Following RIGHT JOIN Messages ON Following.FollowingNickname=Messages.AuthorNickname WHERE FollowerNickname=? ORDER BY Messages.ID DESC";
	public final String SEARCH_USERS_BY_NICKNAME = "SELECT * FROM Users WHERE Nickname LIKE '%%%s%%'";
	
}