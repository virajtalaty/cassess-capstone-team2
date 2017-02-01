package com.cassess.model.taiga;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class ConsumeTaigaAuthUser {
		
	private RestTemplate restTemplate;
	private String baseURL;
	private String username;
	private String password;
	private String type = "normal";
	private TaigaProperties props;
	
	@Autowired
	private TaigaAuthUserDaoImpl dao;
	
	public ConsumeTaigaAuthUser() {
		props = new TaigaProperties();
		restTemplate = new RestTemplate();
		baseURL = "https://api.taiga.io/api/v1/auth";
		username = props.getUsername();
		password = props.getPassword();
	}

	
	public TaigaAuthUser getUserInfo() {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();

        map.add("password", password);
        map.add("type", type);
        map.add("username", username);

        JSONObject jsonContent = new JSONObject(map);
        String content = jsonContent.toString();

        content = content.replaceAll("\\[", "").replaceAll("\\]","");

        //Console Output for testing purposes
        //System.out.println("Content " + content);

        HttpEntity<String> request = new HttpEntity<>(content, headers);

        //Console Output for testing purposes
        //System.out.println("Request " + request);

        ResponseEntity<TaigaAuthUser> response = restTemplate.postForEntity( baseURL, request , TaigaAuthUser.class );

        //Console Output for testing purposes
        //System.out.println("Fetching from " + baseURL);

		return dao.save(response.getBody());
	}

	public String getToken(){
	    TaigaAuthUser testUser = getUserInfo();
	    return testUser.getAuth_token();
    }

	
}

