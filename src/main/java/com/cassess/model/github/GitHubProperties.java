package com.cassess.model.github;


import java.io.*;
import java.util.Properties;

public class GitHubProperties {
    private String accessToken;

    public GitHubProperties() {
    Properties properties = new Properties();

    BufferedReader reader = null;
        try {
        InputStream in = getClass().getResourceAsStream("/github.properties");
        reader = new BufferedReader(new InputStreamReader(in));
        properties.load(reader);
        accessToken = properties.getProperty("access_token");
    } catch (Exception e) {
        e.printStackTrace();
    }finally{
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

    public String getAccessToken() {
        return accessToken;
    }

}
