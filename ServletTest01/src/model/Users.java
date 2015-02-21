/**
 * 
 */
package model;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author sean
 *
 */
public class Users {

	private String Username, Password, NickName, Description;
	private URL ProfileImage;
	private int ID;
	static public int IDcounter = 0;

	public Users(String username, String pass, String nickName, String description, String imageURL) {
		// TODO Auto-generated constructor stub
		this.Username = username;
		this.Password = pass;
		this.NickName = nickName;
		this.Description = description;
		try {
			this.ProfileImage = new URL(imageURL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.ID = Users.IDcounter;
		
		Users.IDcounter++;
	}

	public String getUsername() {
		return Username;
	}

	public int getID() {
		return ID;
	}

	public static int getIDcounter() {
		return IDcounter;
	}
	
	public boolean isPassCorrect(String pass)
	{
		if(this.Password.compareTo(pass) == 0) return true;
		else return false;
	}

	public String getNickName() {
		return NickName;
	}

	public String getDescription() {
		return Description;
	}

	public URL getProfileImage() {
		return ProfileImage;
	}

}
