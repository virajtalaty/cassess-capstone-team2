package edu.asu.cassess.service.taiga;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import edu.asu.cassess.dao.CAssessDAO;
import edu.asu.cassess.dao.taiga.IAuthQueryDao;
import edu.asu.cassess.persist.entity.taiga.AuthUser;

import org.springframework.http.MediaType;

@Service
@Transactional
public class AuthUserService {

	private RestTemplate restTemplate;
	private String authURL;
	private String username;
	private String password;
	private String type;
	private TaigaProperties props;
	private String projectListURL;


	@Autowired
	private CAssessDAO authStoreDao;

	@Autowired
	private IAuthQueryDao authQueryDao;

	public AuthUserService() {
		props = new TaigaProperties();
		restTemplate = new RestTemplate();
		authURL = "https://api.taiga.io/api/v1/auth";
		projectListURL = "https://api.taiga.io/api/v1/projects";
		username = props.getUsername();
		password = props.getPassword();
		type = "normal";
	}


	public AuthUser getUserInfo() {

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
		System.out.println("Content " + content);

		HttpEntity<String> request = new HttpEntity<String>(content, headers);

		//Console Output for testing purposes
		System.out.println("Request " + request);

		ResponseEntity<AuthUser> response = restTemplate.postForEntity(authURL, request , AuthUser.class );

		//Console Output for testing purposes
		System.out.println("Fetching from " + authURL);
		
		Long idCheck = response.getBody().getId();

        	authQueryDao.removeDuplicateUser(idCheck);

		return authStoreDao.save(response.getBody());
	}

	public String getToken(String email){
		return authQueryDao.getUser(email).getAuth_token();
	}

	public Long getID(String email){return authQueryDao.getUser(email).getId();}

}

