package com.dbtest.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dbtest.user.dao.UserDAO;
import com.dbtest.user.model.User;

public class App 
{
    public static void main( String[] args )
    {
    	ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
    	 
        UserDAO userDAO = (UserDAO) context.getBean("userDAO");
        
        User user1 = new User();
                  
        if(userDAO.findByUserId("USLACKBOT") != null){
        	user1 = userDAO.findByUserId("USLACKBOT");
        }else{
        	User insertUser = new User("USLACKBOT", "slackbot","T2FBBQ2JH");
        	userDAO.insert(insertUser);
        	user1 = userDAO.findByUserId("USLACKBOT");
        }
        System.out.println(user1);
        
    }
}
