package general;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;

import model.User;

/**
 * A simple place to hold global application constants
 */
public interface AppConstants {

	public final String CUSTOMERS = "customers";
	public final String CUSTOMERS_FILE = CUSTOMERS + ".json";
	public final String NAME = "name";
	public final Type CUSTOMER_COLLECTION = new TypeToken<Collection<User>>() {}.getType();
	//derby constants
	public final String DB_NAME = "UserDB";
	public final String DB_DATASOURCE = "java:comp/env/jdbc/UserDatasource";
	public final String PROTOCOL = "jdbc:derby:"; 
	//sql statements
	public final String CREATE_USER_TABLE = "CREATE TABLE Users(Username varchar(10),"
			+ "Password varchar(8),"
			+ "Nickname varchar(20),"
			+ "Description varchar(100),"
			+ "ProfileImage varchar(100))";
	public final String INSERT_USER_STMT = "INSERT INTO Users VALUES(?,?,?,?,?)";
	public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMER";
	public final String SELECT_USER_BY_USERNAME_STMT = "SELECT COUNT(*) as cnt FROM Users "
			+ "WHERE Username=?";
	public final String SELECT_USER_BY_NICKNAME_STMT = "SELECT COUNT(*) as cnt FROM Users "
			+ "WHERE Nickname=?";
	public final String SELECT_USER_BY_STMT = "SELECT COUNT(*) as cnt FROM Users "
			+ "WHERE ?=?";
	public final String LOGIN_AUTHENTICATION = "SELECT COUNT(*) as cnt FROM Users WHERE Username=? AND Password=?";
}