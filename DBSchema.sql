/*
	Documentation for our SQL queries
	Sean Man 206184798
    Tomer Eiges 315818948
*/



/* CREATE_USER_TABLE 
   Create the user table */
CREATE TABLE Users(Username varchar(10),
					Password varchar(8),
					Nickname varchar(20),
					Description varchar(100),
					ProfileImage varchar(100));



/* CREATE_MESSAGES_TABLE
   Create the messages table for posts, set the ID column to be the primary key and auto incremented */
CREATE TABLE Messages(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)
					AuthorNickname varchar(10)
					Content varchar(200)
					//Mentions varchar(200)
					Topics varchar(200)
					Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
					RepublishCounter INTEGER DEFAULT 0
					PRIMARY KEY (ID));



/* CREATE_FOLLOWING_TABLE
   Create the following table (each row means some user is following another) */
CREATE TABLE Following(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)
					FollowerNickname varchar(10)
					FollowingNickname varchar(200)
					PRIMARY KEY (ID));



/* INSERT_USER_STMT
   Insert new user to the database */
INSERT INTO Users VALUES(?,?,?,?,?);
	


/* INSERT_MESSAGE_STMT
   Insert a new message */
INSERT INTO Messages(AuthorNickname, Content, Topics) VALUES(?,?,?);



/* INSERT_FOLLOWING
   Insert new data to the following table  */
INSERT INTO Following(FollowerNickname, FollowingNickname) VALUES(?,?);



/* DELETE_FOLLOWING
   Delete a row from the following table - means the user asked to unfollow */
DELETE FROM Following WHERE FollowerNickname=? AND FollowingNickname=?;



/* SELECT_USER_BY_USERNAME_STMT
   Find user by username */	
SELECT * FROM Users WHERE Username=?;



/* SELECT_USER_BY_NICKNAME_STMT
   Find user by nickname */
SELECT * FROM Users WHERE Nickname=?;



/* SELECT_MESSAGE_BY_ID_STMT 
   Retrieve message by message id */
SELECT * FROM Messages WHERE ID=?;



/* SELECT_MESSAGE_BY_NICKNAME_STMT 
   Get all messages written by user */
SELECT * FROM Messages WHERE AuthorNickname=? ORDER BY Timestamp DESC;



/* SELECT_ALL_MESSAGES_OF_NICKNAME_STMT 
   Get all messages not written by sepecified user, and order according to popularity formula */
SELECT * FROM Messages WHERE AuthorNickname!=? 
			ORDER BY LOG(2+(SELECT COUNT(*) FROM Following WHERE FollowingNickname = AuthorNickname))*LOG(2+RepublishCounter) DESC;



/* GET_COLUMNS_BY_TABLE 
   Return all fields of a table (for testing purposes) */
SELECT TABLENAME,COLUMNNAME, t.*  FROM sys.systables t, sys.syscolumns  WHERE TABLEID = REFERENCEID and tablename = ?;



/* GET_COLUMNS 
   Return all fields of all tables */
SELECT TABLENAME,COLUMNNAME, t.*  FROM sys.systables t, sys.syscolumns  WHERE TABLEID = REFERENCEID;



/* SELECT_MESSAGES_BY_FOLLOWING 
   Get all messages written by users the user is following, and order according to popularity formula */
SELECT Messages.* FROM Following RIGHT JOIN Messages ON Following.FollowingNickname=Messages.AuthorNickname WHERE FollowerNickname=? 
	ORDER BY LOG(2+(SELECT COUNT(*) FROM Following WHERE FollowingNickname = Messages.AuthorNickname))*LOG(2+Messages.RepublishCounter) DESC;



/* SEARCH_USERS_BY_NICKNAME 
   Search users by their nicknames */
SELECT * FROM Users WHERE Nickname LIKE '%%%s%%';




/* SEARCH_MESSAGE_BY_TOPIC 
   Find all messages that contain the specified topic */
SELECT * FROM Messages WHERE Topics LIKE '%%"%s"%%' ORDER BY Timestamp DESC;




/* SELECT_TOP_FOLLOWING_BY_NICKNAME 
   Select a user's top followees according to popularity formula */
SELECT FollowingNickname FROM Following WHERE FollowerNickname = ? 
	ORDER BY LOG(2+(SELECT COUNT(*) FROM Following WHERE FollowerNickname = ?)) DESC;




/* SELECT_TOP_FOLLOWER_BY_NICKNAME 
   Select a user's top followers according to popularity formula */
SELECT FollowerNickname FROM Following WHERE FollowingNickname = ? 
	ORDER BY LOG(2+(SELECT COUNT(*) FROM Following WHERE FollowingNickname = ?)) DESC;




/* COUNT_USER_BY_USERNAME_STMT
   Check for occurrence of a username (used to check if a username is already taken) */   
SELECT COUNT(*) as cnt FROM Users WHERE Username=?;




/* COUNT_USER_BY_NICKNAME_STMT 
   Check for occurrence of a nickname (used to check if a nickname is already taken) */ 
SELECT COUNT(*) as cnt FROM Users WHERE Nickname=?;




/* LOGIN_AUTHENTICATION 
   Select a row by combination of username and password (used for login authentication) */
SELECT * FROM Users WHERE Username=? AND Password=?;




/* COUNT_FOLLOWERS_BY_NICKNAME 
   Count how many followers a user has */
SELECT COUNT(*) as cnt FROM Following WHERE FollowingUsername=?;




/* SELECT_FOLLOWING
   Count how many users the user is following */
SELECT COUNT(*) as cnt FROM Following WHERE FollowerNickname=? AND FollowingNickname=?;





/* INCREAMENT_REPUBLISHED_MESSAGE 
   Increament the republished counter of a message (this is called when a user republishes a message) */
UPDATE Messages SET RepublishCounter=RepublishCounter+1 WHERE ID=?;

