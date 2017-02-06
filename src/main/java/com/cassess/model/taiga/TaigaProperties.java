package com.cassess.model.taiga;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TaigaProperties {
	Properties taigaprop;
	InputStream input;
	String username;
	String password;


	public TaigaProperties() {
		taigaprop = new Properties();
		
		try {

			input = new FileInputStream("src/main/java/com/cassess/model/taiga/taiga.properties");
			
			taigaprop.load(input);
			
			username = taigaprop.getProperty("username");
			password = taigaprop.getProperty("password");

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
	
	public String getUsername() {
		return username;
	}
    public String getPassword() {
        return password;
    }
	


  }

