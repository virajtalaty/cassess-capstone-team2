package com.dbtest.user.dao;

import com.dbtest.user.model.User;

public interface UserDAO 
{
	public void insert(User user);
	public User findByUserId(String string);
}




