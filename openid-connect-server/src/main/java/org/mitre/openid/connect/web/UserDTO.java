package org.mitre.openid.connect.web;

import com.sun.istack.internal.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.mitre.openid.connect.model.PasswordMatches;

@PasswordMatches
public class UserDTO {
	@NotNull
	@NotEmpty
	private String userName;

	@NotNull
	@NotEmpty
	private String password;
	private String matchingPassword;

	@NotNull
	@NotEmpty
	private String email;
	@NotNull
	@NotEmpty
	private String phone;

	public UserDTO() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserDTO userDto = (UserDTO) o;

		if (userName != null ? !userName.equals(userDto.userName) : userDto.userName != null) return false;
		if (password != null ? !password.equals(userDto.password) : userDto.password != null) return false;
		if (matchingPassword != null ? !matchingPassword.equals(userDto.matchingPassword) : userDto.matchingPassword != null)
			return false;
		if (email != null ? !email.equals(userDto.email) : userDto.email != null) return false;
		return phone != null ? phone.equals(userDto.phone) : userDto.phone == null;

	}

	@Override
	public int hashCode() {
		int result = userName != null ? userName.hashCode() : 0;
		result = 131 * result + (password != null ? password.hashCode() : 0);
		result = 131 * result + (matchingPassword != null ? matchingPassword.hashCode() : 0);
		result = 131 * result + (email != null ? email.hashCode() : 0);
		result = 131 * result + (phone != null ? phone.hashCode() : 0);
		return result;
	}
}
