/**
 * 
 */
package model;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author      Sean Man 206184798
 * @author		Tomer Eiges 315818948
 * 
 * represent user
 */
public class Users {

	private String Username, NickName, Description;
	private URL ProfileImage; // URL for profile image
	private int ID; // ID number in the DB

	public Users(String username, String nickName, String description, String imageURL) {
		this.Username = username;
		this.NickName = nickName;
		this.Description = description;
		try {
			this.ProfileImage = new URL(imageURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public String getUsername() {
		return Username;
	}

	public int getID() {
		return ID;
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
