package com.cassess.service;

import com.cassess.entity.AuthToken;
import org.json.JSONObject;


public interface IAuthTokenService {

    <T> Object create(AuthToken authToken);

    <T> Object update(AuthToken authToken);

    <T> Object find(String tool);

    <T> Object delete(String tool);
}
