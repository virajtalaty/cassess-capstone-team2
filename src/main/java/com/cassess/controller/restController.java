package com.cassess.controller;

import com.cassess.entity.AuthToken;
import com.cassess.service.IAuthTokenService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest")
public class restController {


    @Autowired
    private IAuthTokenService authTokenService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/token", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody
    <T> Object updateAuthToken(@RequestBody AuthToken authToken){

        if(authToken == null){
            return null;
        }else{

            return authTokenService.update(authToken);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/token", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody
    <T> Object newAuthToken(@RequestBody AuthToken authToken){

        if(authToken == null){
            return null;
        }else{
            return authTokenService.create(authToken);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/token/{tool}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody
    <T> Object getAuthToken(@PathVariable("tool") String tool){

        if(tool == null){
            return null;
        }else{
            System.out.println("Tool: " + tool);
            return authTokenService.find(tool);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/token/{tool}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody
    <T> Object deleteAuthToken(@PathVariable("tool") String tool){

        if(tool == null){
            return null;
        }else{
            System.out.println("Tool: " + tool);
            return authTokenService.delete(tool);
        }
    }

}

