package com.cassess.model.slack;

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

			input = new FileInputStream("src/main/java/com/cassess/model/slack/slack.properties");
			
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

