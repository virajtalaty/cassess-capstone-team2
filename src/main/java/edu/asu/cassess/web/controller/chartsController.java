package edu.asu.cassess.web.controller;

//import com.cassess.service.ApiService;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import edu.asu.cassess.persist.entity.github.CommitData;
import edu.asu.cassess.service.github.GatherGitHubData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@JsonAutoDetect
public class chartsController {

    @Autowired
    private GatherGitHubData gatherGitHubData;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/charts", produces = "application/json")
    public  Map<String, Object> homeResource(){
    //public List<CommitData> homeResource(){

        String jsonString = "";
        Map<String, Object> model = new HashMap<>();

        //model.put("placeholder", "This is placeholder text");

        //return model;

        List<CommitData> chartData = gatherGitHubData.getCommitList();

        ObjectMapper mapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        try {
          jsonString = mapper.writeValueAsString(chartData);
        }catch(Exception e){
            e.printStackTrace();
        }

        int counter = 0;

        List<List<Integer>> commitArray = new ArrayList<>();
        List<String> weeks = new ArrayList<>();
        JSONArray jsonarray = new JSONArray(jsonString);

        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonObject = jsonarray.getJSONObject(i);
            JSONObject commitDataPK = jsonObject.getJSONObject("commitDataPK");
            //String week = commitDataPK.getString("date");
            String username = commitDataPK.getString("username");
            //int commits = jsonObject.getInt("commits");
            //Integer commitsObj = new Integer(commits);

            ///Check for individual contributor (Change to contributor)
            if(username.equals("tjjohn1")){
                counter++;
                String week = commitDataPK.getString("date");
                int commits = jsonObject.getInt("commits");
                Integer commitsObj = new Integer(commits);

                List<Integer> dataPoints = new ArrayList<Integer>();
                dataPoints.add(new Integer(counter));
                dataPoints.add(commitsObj);
                commitArray.add(dataPoints);

                weeks.add(week);
            }
        }

        model.put("commitArray", commitArray);
        model.put("weeks", weeks);

        //ArrayList<Object> chartDataArray = createDataChartDataArray(gitHubData, "tjjohn1");

        return model;
    }

   /* private ArrayList<String> seperateObjects(String gitHubData, String stringPattern, char characterEnd) {

        ArrayList<String> returnedList = new ArrayList<>();

        Pattern ptrn= Pattern.compile(stringPattern);

        Matcher matcher = ptrn.matcher(gitHubData);

        int dataEnd = 0;

        String returnedListItem;

        while(matcher.find()){

            int dataStart = matcher.start();

            int goTwice = 0;

            for (int i = dataStart; i < gitHubData.length();  i++){
                if (gitHubData.charAt(i) == characterEnd) {
                    dataEnd = i+1;
                    goTwice += 1;
                    if(goTwice > 1) {
                        break;
                    }
                }
            }

            returnedListItem = gitHubData.substring(dataStart, dataEnd);
            returnedList.add(returnedListItem);
        }

        return returnedList;
    }

    private String parseForItems(String gitHubData, String stringPattern, char characterEnd) {

        String returnedData = "";

        int stringLength = stringPattern.length();

        Pattern ptrn= Pattern.compile(stringPattern);

        Matcher matcher = ptrn.matcher(gitHubData);

        while(matcher.find()){

            int dataStart= matcher.start() + stringLength;

            int dataEnd = 0;

            for (int i = dataStart; i < gitHubData.length();  i++){
                if (gitHubData.charAt(i) == characterEnd) {
                    dataEnd = i;
                    break;
                }
            }

            returnedData = gitHubData.substring(dataStart, dataEnd);
        }

        return returnedData;
    }

    private Object createDataChartObject(String listItem) {

        String stringPattern2 = "date=";

        String Day = parseForItems(listItem, stringPattern2, ',');

        String stringPattern3 = "commits=";

        String commit = parseForItems(listItem, stringPattern3, ',');

        JSONObject jsonObj = new JSONObject();

        jsonObj.put("label", Day);
        jsonObj.put("value", commit);

        ObjectMapper mapper = new ObjectMapper();

        Object ob = new Object();

        try {
            ob = mapper.readValue(jsonObj.toString(), Object.class);
            //System.out.println(ob.toString());
        }catch(Exception e){
            e.printStackTrace();
        }

        return ob;
    }

    private ArrayList<Object> createDataChartDataArray(String commitData, String userName) {

        ArrayList<Object> returnedDataChartArray = new ArrayList<>();

        ArrayList<String> listItems = seperateObjects(commitData, "CommitDataPK", '}');

        for (String item : listItems){
            if(item.contains("username='"+userName)) {
                returnedDataChartArray.add(createDataChartObject(item));
            }
        }

        return returnedDataChartArray;
    }*/
}