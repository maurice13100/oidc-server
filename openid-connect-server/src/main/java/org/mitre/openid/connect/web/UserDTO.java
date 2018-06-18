package org.mitre.openid.connect.web;

import org.hibernate.validator.constraints.NotEmpty;
import org.mitre.openid.connect.model.PasswordMatches;

import javax.validation.constraints.NotNull;

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

	private String givenName;
	private String familyName;
	private String middleName;
	private String gender;
	private String birthdate;
	private String streetAddress;
	private String locality;
	private String region;
	private String postalCode;
	private String country;

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

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserDTO userDTO = (UserDTO) o;

		if (userName != null ? !userName.equals(userDTO.userName) : userDTO.userName != null) return false;
		if (password != null ? !password.equals(userDTO.password) : userDTO.password != null) return false;
		if (matchingPassword != null ? !matchingPassword.equals(userDTO.matchingPassword) : userDTO.matchingPassword != null) return false;
		if (email != null ? !email.equals(userDTO.email) : userDTO.email != null) return false;
		if (phone != null ? !phone.equals(userDTO.phone) : userDTO.phone != null) return false;
		if (givenName != null ? !givenName.equals(userDTO.givenName) : userDTO.givenName != null) return false;
		if (familyName != null ? !familyName.equals(userDTO.familyName) : userDTO.familyName != null) return false;
		if (middleName != null ? !middleName.equals(userDTO.middleName) : userDTO.middleName != null) return false;
		if (gender != null ? !gender.equals(userDTO.gender) : userDTO.gender != null) return false;
		if (birthdate != null ? !birthdate.equals(userDTO.birthdate) : userDTO.birthdate != null) return false;
		if (streetAddress != null ? !streetAddress.equals(userDTO.streetAddress) : userDTO.streetAddress != null) return false;
		if (locality != null ? !locality.equals(userDTO.locality) : userDTO.locality != null) return false;
		if (region != null ? !region.equals(userDTO.region) : userDTO.region != null) return false;
		if (postalCode != null ? !postalCode.equals(userDTO.postalCode) : userDTO.postalCode != null) return false;
		return country != null ? country.equals(userDTO.country) : userDTO.country == null;

	}

	@Override
	public int hashCode() {
		int result = userName != null ? userName.hashCode() : 0;
		result = 131 * result + (password != null ? password.hashCode() : 0);
		result = 131 * result + (matchingPassword != null ? matchingPassword.hashCode() : 0);
		result = 131 * result + (email != null ? email.hashCode() : 0);
		result = 131 * result + (phone != null ? phone.hashCode() : 0);
		result = 131 * result + (givenName != null ? givenName.hashCode() : 0);
		result = 131 * result + (familyName != null ? familyName.hashCode() : 0);
		result = 131 * result + (middleName != null ? middleName.hashCode() : 0);
		result = 131 * result + (gender != null ? gender.hashCode() : 0);
		result = 131 * result + (birthdate != null ? birthdate.hashCode() : 0);
		result = 131 * result + (streetAddress != null ? streetAddress.hashCode() : 0);
		result = 131 * result + (locality != null ? locality.hashCode() : 0);
		result = 131 * result + (region != null ? region.hashCode() : 0);
		result = 131 * result + (postalCode != null ? postalCode.hashCode() : 0);
		result = 131 * result + (country != null ? country.hashCode() : 0);
		return result;
	}
}
