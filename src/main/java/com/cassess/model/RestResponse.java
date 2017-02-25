package com.cassess.model;

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
