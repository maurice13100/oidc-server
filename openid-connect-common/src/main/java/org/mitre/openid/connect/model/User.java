package org.mitre.openid.connect.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
	@Id @Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "enabled")
	private boolean enabled;
	@OneToMany(mappedBy = "user")
	private Set<Authority> authorities;

	public User() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (enabled != user.enabled) return false;
		if (username != null ? !username.equals(user.username) : user.username != null) return false;
		if (password != null ? !password.equals(user.password) : user.password != null) return false;
		return authorities != null ? authorities.equals(user.authorities) : user.authorities == null;

	}

	@Override
	public int hashCode() {
		int result = username != null ? username.hashCode() : 0;
		result = 131 * result + (password != null ? password.hashCode() : 0);
		result = 131 * result + (enabled ? 1 : 0);
		result = 131 * result + (authorities != null ? authorities.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "User{" +
			"username='" + username + '\'' +
			'}';
	}
}
