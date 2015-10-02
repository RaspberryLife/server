package rbl.data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name = "rblUser")
public class User
{

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private int userId;

	@Column(name = "userName")
	private String userName;

	@Column(name = "userEmail")
	private String userEmail;

	@Column(name = "userPassword")
	private String userPassword;

	@Column(name = "userRole")
	private String userRole;

	//----------------------------------------------------------------------------------------------
	//                                      GETTER & SETTER
	//----------------------------------------------------------------------------------------------

	public String getUserRole()
	{
		return userRole;
	}

	public void setUserRole(String role)
	{
		this.userRole = role;
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int id)
	{
		this.userId = id;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String name)
	{
		this.userName = name;
	}

	public String getUserEmail()
	{
		return userEmail;
	}

	public void setUserEmail(String email)
	{
		this.userEmail = email;
	}

	public String getUserPassword()
	{
		return userPassword;
	}

	public void setUserPassword(String password)
	{
		this.userPassword = password;
	}

	@Override public String toString()
	{
		return "User{" +
				"userId=" + userId +
				", userName='" + userName + '\'' +
				", userEmail='" + userEmail + '\'' +
				", userPassword='" + userPassword + '\'' +
				", userRole='" + userRole + '\'' +
				'}';
	}
}
