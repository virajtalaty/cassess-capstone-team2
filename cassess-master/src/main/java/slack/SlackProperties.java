package slack;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SlackProperties {
	Properties slackprop;
	InputStream input;
	String token;

	
	public SlackProperties() {
		slackprop = new Properties();
		
		try {

			input = new FileInputStream("slack.properties");
			
			slackprop.load(input);
			
			token = slackprop.getProperty("token");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public String getToken() {
		return token;
	}
	


  }

