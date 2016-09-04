package org.mitre.openid.connect.model;

import javax.persistence.*;

@Entity
@Table(name = "authorities",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"username", "authority"})
	}
)
@NamedQueries({
	@NamedQuery(name = Authority.QUERY_BY_NAME, query = "select a from Authority a where a.user.username = :username")
})
public class Authority {

	public static final String QUERY_BY_NAME = "Authority.getByName";

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "username", nullable = false)
	private User user;

	@Column(name = "authority", nullable = false, length = 45)
	private String authority;

	public Authority() {
	}

	public Authority(User user, String authority) {
		this.user = user;
		this.authority = authority;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Authority authority1 = (Authority) o;

		if (user != null ? !user.equals(authority1.user) : authority1.user != null) return false;
		return authority != null ? authority.equals(authority1.authority) : authority1.authority == null;

	}

	@Override
	public int hashCode() {
		int result = user != null ? user.hashCode() : 0;
		result = 131 * result + (authority != null ? authority.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Authority{" +
			"user=" + user +
			'}';
	}
}
