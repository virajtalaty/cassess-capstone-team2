package edu.asu.cassess.service.github.agReplacement;

import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

class Commit {
    String timestamp;
    String message;
    String html_url;
    String branch;
    int additions;
    int deletions;
    int total;
    String sha;

    @Override
    public String toString() {
        return "{" +
                "\"timestamp\":\"" + timestamp + '\"' +
                ",\"message\":\"" + message + "\"" +
                ",\"branch\":\"" + branch + "\"" +
                ",\"html_url\":\"" + html_url + '\"' +
                ",\"additions\":" + additions +
                ",\"deletions\":" + deletions +
                ",\"total\":" + total +
                '}';
    }
    public Commit(JSONObject obj, String branch){
        DateTimeFormatter formatter1 = DateTimeFormatter.ISO_DATE_TIME;
        DateTimeFormatter formatter2 = DateTimeFormatter.RFC_1123_DATE_TIME;
        this. sha = obj.getString("sha");
        this.message = obj.getJSONObject("commit").getString("message").replaceAll("\r\n|\r|\n","\\\\n");
        message = message.replaceAll("\\t","\\\\t");
        Pattern quote = Pattern.compile("\"");
        message = message.replaceAll(quote.pattern(),"\\\\\"");
        this.html_url = obj.getString("html_url");
        JSONObject stats = obj.getJSONObject("stats");
        this.additions = stats.getInt("additions");
        this.deletions = stats.getInt("deletions");
        this.total = stats.getInt("total");
        this.timestamp = obj.getJSONObject("commit").getJSONObject("author").getString("date");
        timestamp =formatter2.format(formatter1.parse(timestamp));
        this.branch = branch;
    }

    public Commit(String timestamp, String message, String html_url, String branch, int additions, int deletions, int total, String sha) {
        this.timestamp = timestamp;
        this.message = message;
        this.html_url = html_url;
        this.branch = branch;
        this.additions = additions;
        this.deletions = deletions;
        this.total = total;
        this.sha = sha;
    }
}
