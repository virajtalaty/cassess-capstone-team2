package com.dbtest.user.model;

import java.sql.Timestamp;

public class User 
{
	String Id;
	String name;
	String TeamId;
	
	
	public User(String Id, String name, String TeamId) {
		this.Id = Id;
		this.name = name;
		this.TeamId = TeamId;
	}
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return Id;
	}
	public void setCustId(String Id) {
		this.Id = Id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTeamId() {
		return TeamId;
	}
	public void setTeamId(String TeamId) {
		this.TeamId = TeamId;
	}

	@Override
	public String toString() {
		return "User [TeamId=" + TeamId + ", Id=" + Id + ", name=" + name
				+ "]";
	}
	
	
}
