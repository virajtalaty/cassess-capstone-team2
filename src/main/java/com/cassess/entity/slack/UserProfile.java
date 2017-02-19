package com.cassess.entity.slack;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {

	private String avatar_hash;
	private String first_name;
	private String last_name;
	@Column(name="profile_real_name")
	private String real_name;
	private String email;
	private String skype;
	private String phone;
	private String image_24;
	private String image_32;
	private String image_48;
	private String image_72;
	private String image_192;
	
	public UserProfile(){
		
	}
	
	/**
	 * @return the avatar_hash
	 */
	public String getAvatar_hash() {
		return avatar_hash;
	}
	/**
	 * @param avatar_hash the avatar_hash to set
	 */
	public void setAvatar_hash(String avatar_hash) {
		this.avatar_hash = avatar_hash;
	}
	/**
	 * @return the first_name
	 */
	public String getFirst_name() {
		return first_name;
	}
	/**
	 * @param first_name the first_name to set
	 */
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return last_name;
	}
	/**
	 * @param last_name the last_name to set
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	/**
	 * @return the real_name
	 */
	public String getReal_name() {
		return real_name;
	}
	/**
	 * @param real_name the real_name to set
	 */
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the skype
	 */
	public String getSkype() {
		return skype;
	}
	/**
	 * @param skype the skype to set
	 */
	public void setSkype(String skype) {
		this.skype = skype;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the image_24
	 */
	public String getImage_24() {
		return image_24;
	}
	/**
	 * @param image_24 the image_24 to set
	 */
	public void setImage_24(String image_24) {
		this.image_24 = image_24;
	}
	/**
	 * @return the image_32
	 */
	public String getImage_32() {
		return image_32;
	}
	/**
	 * @param image_32 the image_32 to set
	 */
	public void setImage_32(String image_32) {
		this.image_32 = image_32;
	}
	/**
	 * @return the image_48
	 */
	public String getImage_48() {
		return image_48;
	}
	/**
	 * @param image_48 the image_48 to set
	 */
	public void setImage_48(String image_48) {
		this.image_48 = image_48;
	}
	/**
	 * @return the image_72
	 */
	public String getImage_72() {
		return image_72;
	}
	/**
	 * @param image_72 the image_72 to set
	 */
	public void setImage_72(String image_72) {
		this.image_72 = image_72;
	}
	/**
	 * @return the image_192
	 */
	public String getImage_192() {
		return image_192;
	}
	/**
	 * @param image_192 the image_192 to set
	 */
	public void setImage_192(String image_192) {
		this.image_192 = image_192;
	}
	
}
