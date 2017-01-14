package existingRest;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String args[]) {
		SpringApplication.run(Application.class);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception, JsonParseException, IOException {
		return args -> { String result = restTemplate.getForObject("https://slack.com/api/users.list?token=xoxp-83385818629-83396174567-123161160469-710d4bbb79ee564ee258650e455f9a7f&pretty=1", String.class);
	 
	    		ObjectMapper mapper = new ObjectMapper();
	    		JsonNode actualObj = mapper.readTree(result);
	    	
	    		System.out.print(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(actualObj));
		};
	}
}
