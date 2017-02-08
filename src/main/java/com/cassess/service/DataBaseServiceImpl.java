package com.cassess.service;

import com.cassess.model.taiga.AuthUserQueryDao;
import com.cassess.model.taiga.ConsumeAuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class DataBaseServiceImpl implements DataBaseService {

    @Autowired
    private AuthUserQueryDao authUserQueryDao;

    private void setConsumeAuthUser() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        ConsumeAuthUser consumeAuthUser = context.getBean("consumeAuthUser", ConsumeAuthUser.class);
        consumeAuthUser.getUserInfo();
        context.close();
    }

    @Override
    public String getTaigaToken(){
        setConsumeAuthUser();
        String token = authUserQueryDao.getUser("TaigaTestUser@gmail.com").getAuth_token();
        return token;
    }

    @Override
    public Long getTaigaID(){
        Long Id = authUserQueryDao.getUser("TaigaTestUser@gmail.com").getId();
        return Id;
    }

}
