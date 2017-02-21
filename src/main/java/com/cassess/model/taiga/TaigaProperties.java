package com.cassess.model.taiga;

import java.io.*;
import java.util.Properties;

public class TaigaProperties {
	Properties taigaprop;
	BufferedReader reader;
	String username;
	String password;


	public TaigaProperties() {
		taigaprop = new Properties();
		
		try {

			InputStream in = getClass().getResourceAsStream("/taiga.properties");
			reader = new BufferedReader(new InputStreamReader(in));
			
			taigaprop.load(reader);
			
			username = taigaprop.getProperty("username");
			password = taigaprop.getProperty("password");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
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

