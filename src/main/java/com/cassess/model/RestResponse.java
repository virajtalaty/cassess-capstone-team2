package com.cassess.model;
import org.json.JSONObject;
import org.springframework.util.MultiValueMap;

/**
 * Created by Thomas on 2/23/2017.
 */
public class RestResponse {

    public String response;


    public RestResponse (String message){

        this.response = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String message) {

        this.response = message;
    }


}
