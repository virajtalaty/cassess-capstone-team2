package edu.asu.cassess.service.github.agReplacement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class QueryCaller {
    String token;
    String owner;
    String repo;
    int calls = 0;

    public QueryCaller(String token, String owner, String repo) {
        this.token = token;
        this.owner = owner;
        this.repo = repo;
    }

    public String getCommits(String user, String start, String end, String branch){
        return apiCall("/commits?access_token="+token+"&author="+user+
                "&since="+start+"T00:00:00Z&until="+end+"T23:59:59&sha="+branch);
    }
    public String getCommit(String sha){
        return apiCall("/commits/"+sha+"?access_token="+token);
    }

    public String apiCall(String querry){
        return apiCall(querry, false);
    }

    public String apiCall(String querry, boolean showHeaders){
        StringBuffer response1 = new StringBuffer();
        String jsonData = null;
        String inputLine;
        BufferedReader in;
        calls++;
        try {
            URL url = new URL("https://api.github.com/repos/"+owner+"/"+repo+querry);
            //System.out.println(url.toString());
            HttpURLConnection con;
            do {
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "User-Agent");
                String linkHeader = con.getHeaderField("Link");
                url = null;
                if(linkHeader!=null){
                    HashMap<String,String> links = new HashMap<>();
                    String[] split = linkHeader.split(",\\s");
                    for(String s:split) {
                        String[] l = s.split(";\\s");
                        links.put(l[1].substring(5,9),l[0].replaceAll("[<>]",""));
                    }
                    if(links.containsKey("next")) {
                        url = new URL(links.get("next"));
                    }
                }

                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    response1.append(inputLine);
                }

                //System.out.println(con.getHeaderField("Link").);
                if (showHeaders) {
                    Map headers = con.getHeaderFields();
                    Set keys = headers.keySet();
                    for (Object s : keys)
                        System.out.println(s + ": " + headers.get(s));
                }

                in.close();
            }while (url!=null);

            jsonData = response1.toString().replaceAll("]\\[",",");
        } catch (Exception e){e.printStackTrace();}
        return jsonData;
    }
}
