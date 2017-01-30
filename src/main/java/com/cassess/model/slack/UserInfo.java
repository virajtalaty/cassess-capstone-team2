package com.cassess.model.slack;

public class UserInfo {
	private boolean ok;
	private UserObject user;
	private String warning;
	private String error;
	private long cache_ts; //time stamp of retrieval
	
	public UserInfo() {
		
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
	 * @return the user
	 */
	public UserObject getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(UserObject user) {
		this.user = user;
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
}
