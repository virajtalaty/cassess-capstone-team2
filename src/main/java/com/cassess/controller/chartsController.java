package com.cassess.controller;

import com.cassess.service.ApiService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class chartsController {

    @Autowired
    private ApiService apiService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/charts", produces = "application/json")
    public ArrayList<Object> homeResource(){

        String gitHubData = apiService.getGitHubCommitList();

        ArrayList<Object> chartDataArray = createDataChartDataArray(gitHubData, "tjjohn1");

        return chartDataArray;
    }

    private ArrayList<String> seperateObjects(String gitHubData, String stringPattern, char characterEnd) {

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
    }
}



