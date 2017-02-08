package com.cassess.model.github;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GitHubProperties {
    private String accessToken;

    public GitHubProperties() {
        Properties properties = new Properties();

        InputStream input = null;
        try {
            input = new FileInputStream("src/main/java/com/cassess/model/github/github.properties");
            properties.load(input);
            accessToken = properties.getProperty("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

}
