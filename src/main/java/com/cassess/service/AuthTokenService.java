package com.cassess.service;

import com.cassess.entity.AuthToken;
import com.cassess.service.DAO.AuthTokenServiceDaoImpl;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;

@Service
public class AuthTokenService implements IAuthTokenService{

    @EJB
    private AuthTokenServiceDaoImpl authTokenDao;

    public <T> Object create(AuthToken authToken){

        return authTokenDao.create(authToken);
    }

    public <T> Object update(AuthToken authToken){

        return authTokenDao.update(authToken);
    }

    public <T> Object find(String tool){

        return authTokenDao.find(tool);
    }

    public <T> Object delete(String tool){

        return authTokenDao.delete(tool);

    }
}
