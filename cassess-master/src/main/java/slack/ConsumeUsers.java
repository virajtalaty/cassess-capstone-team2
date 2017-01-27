package slack;

import org.springframework.web.client.RestTemplate;

public class ConsumeUsers {
		
	private RestTemplate restTemplate;
	private String baseURL;
	private String token;
	private SlackProperties props;
	
	public ConsumeUsers() {
		props = new SlackProperties();
		restTemplate = new RestTemplate();
		baseURL = "https://slack.com/api/";
		token = props.getToken();
	}
	
	public UserList getUserList() {
		String ulURL = baseURL + "users.list" + token;
		UserList ul = restTemplate.getForObject(ulURL, UserList.class);
		return ul;
	}
	
	public UserObject getUserInfo(String userID) {
		String uidURL = baseURL + "users.info" + token + "&user=" + userID;
		UserObject retUser = restTemplate.getForObject(uidURL, UserObject.class);
		return retUser;
	}
	
}

