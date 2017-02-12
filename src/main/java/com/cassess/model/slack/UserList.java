package com.cassess.model.slack;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserList {

	private boolean ok;
	private List<UserObject> members;
	private String warning;
	private String error;
	private long cache_ts; //time stamp of retrieval
	
	public UserList() {
		
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
	 * @return the members
	 */
	public List<UserObject> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(List<UserObject> members) {
		this.members = members;
	}
}
