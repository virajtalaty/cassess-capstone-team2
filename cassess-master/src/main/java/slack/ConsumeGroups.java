package slack;

import org.springframework.web.client.RestTemplate;

public class ConsumeGroups {
	
	private RestTemplate restTemplate;
	private String baseURL;
	private String token;
	
	public ConsumeGroups() {
		restTemplate = new RestTemplate();
		baseURL = "https://slack.com/api/";
		token = "";
	}
}
