package com.cassess.model.slack;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "slack_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserObject {
	
	@Transient
	private boolean ok;
	private String warning;
	private String error;
	private long cache_ts; //time stamp of retrieval
	@Id
	private String id;
	private String team_id;
	private String name;
	private boolean deleted;
	private String status;
	private String color;
	private String real_name;
	private String tz;
	private String tz_label;
	private long tz_offset;
	@Embedded
	private UserProfile profile;
	private boolean is_admin;
	private boolean is_owner;
	private boolean has_2fa;
	
	public UserObject(){
		
	}
	
	/**
	 * @return the ok
	 */
	public boolean isOk() {
		return ok;
	}

	/**
	 * @param ok the ok to set
	 */
	public void setOk(boolean ok) {
		this.ok = ok;
	}

	/**
	 * @return the warning
	 */
	public String getWarning() {
		return warning;
	}

	/**
	 * @param warning the warning to set
	 */
	public void setWarning(String warning) {
		this.warning = warning;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @return the cache_ts
	 */
	public long getCache_ts() {
		return cache_ts;
	}

	/**
	 * @param cache_ts the cache_ts to set
	 */
	public void setCache_ts(long cache_ts) {
		this.cache_ts = cache_ts;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the team_id
	 */
	public String getTeam_id() {
		return team_id;
	}
	/**
	 * @param team_id the team_id to set
	 */
	public void setTeam_id(String team_id) {
		this.team_id = team_id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}
	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
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
	 * @return the tz
	 */
	public String getTz() {
		return tz;
	}
	/**
	 * @param tz the tz to set
	 */
	public void setTz(String tz) {
		this.tz = tz;
	}
	/**
	 * @return the tz_label
	 */
	public String getTz_label() {
		return tz_label;
	}
	/**
	 * @param tz_label the tz_label to set
	 */
	public void setTz_label(String tz_label) {
		this.tz_label = tz_label;
	}
	/**
	 * @return the tz_offset
	 */
	public long getTz_offset() {
		return tz_offset;
	}
	/**
	 * @param tz_offset the tz_offset to set
	 */
	public void setTz_offset(long tz_offset) {
		this.tz_offset = tz_offset;
	}
	/**
	 * @return the profile
	 */
	public UserProfile getProfile() {
		return profile;
	}
	/**
	 * @param profile the profile to set
	 */
	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}
	/**
	 * @return the is_admin
	 */
	public boolean isIs_admin() {
		return is_admin;
	}
	/**
	 * @param is_admin the is_admin to set
	 */
	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}
	/**
	 * @return the is_owner
	 */
	public boolean isIs_owner() {
		return is_owner;
	}
	/**
	 * @param is_owner the is_owner to set
	 */
	public void setIs_owner(boolean is_owner) {
		this.is_owner = is_owner;
	}
	/**
	 * @return the has_2fa
	 */
	public boolean isHas_2fa() {
		return has_2fa;
	}
	/**
	 * @param has_2fa the has_2fa to set
	 */
	public void setHas_2fa(boolean has_2fa) {
		this.has_2fa = has_2fa;
	}

}
