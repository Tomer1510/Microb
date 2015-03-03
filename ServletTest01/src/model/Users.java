/**
 * 
 */
package model;

/**
 * @author sean
 *
 */
public class Users {

	private String Username, NickName, Description;
	private String ProfileImage;
	private int ID;

	public Users(String username, String nickName, String description, String imageURL) {
		// TODO Auto-generated constructor stub
		this.Username = username;
		//this.Password = pass;
		this.NickName = nickName;
		this.Description = description;
		if(!imageURL.equals("")) {
			this.ProfileImage = imageURL;
		} else {
			this.ProfileImage = "img/defaultImage.png";
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

	public String getProfileImage() {
		return ProfileImage;
	}

}