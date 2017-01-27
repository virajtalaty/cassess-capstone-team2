package slack;

import org.springframework.web.client.RestTemplate;

public class ConsumeChannels {
	
	private RestTemplate restTemplate;
	private String baseURL;
	private String token;
	
	public ConsumeChannels() {
		restTemplate = new RestTemplate();
		baseURL = "https://slack.com/api/";
		token = "?token=xoxp-83385818629-84247524707-130058660960-971fd01ecefcef905d5cea1e5e9143ef";
	}
}
