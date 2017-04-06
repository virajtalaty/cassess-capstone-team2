package edu.asu.cassess.web.controller;

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

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
//@JsonAutoDetect
public class chartsController {

    @Autowired
    private GatherGitHubData gatherGitHubData;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/charts", produces = "application/json")
    public  Map<String, Object> chartsResource(){

        ///Creates an empty initial String
        String jsonString = "";

        ///Creates a hash map
        Map<String, Object> model = new HashMap<>();


        ///Gets the commit data as a whole
        List<CommitData> chartData = gatherGitHubData.getCommitList();

        //String jsonString = getJSONString(chartData);

        /// Creates chart data in the form [{<data1>},{<data2>}]
        ObjectMapper mapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        try {
          jsonString = mapper.writeValueAsString(chartData);
        }catch(Exception e){
            e.printStackTrace();
        }


        ///Creates a counter to check the number of objects for a user
        int counter = 0;

        /// Create returned collections that will be arrays in the json object
        List<List<Integer>> commitArray = new ArrayList<>();
        List<String> weeks = new ArrayList<>();

        /// Read in the chart Data as a JSON array
        JSONArray jsonarray = new JSONArray(jsonString);

        /// Check Each object in the JSON array for contributor name
        for (int i = 0; i < jsonarray.length(); i++) {
            /// Separates each  JSON object out of the JSON array.
            JSONObject jsonObject = jsonarray.getJSONObject(i);
            JSONObject commitDataPK = jsonObject.getJSONObject("commitDataPK");
            ///Gets the username or contributor from chartData
            String username = commitDataPK.getString("username");

            ///Makes sure username is equal to contributor
            if(username.equals("tjjohn1")){
                /// counts the number of objects for that contributor
                counter++;
                /// Gets the week they committed
                String week = commitDataPK.getString("date");
                /// Gets the number of commits they made
                int commits = jsonObject.getInt("commits");
                Integer commitsObj = new Integer(commits);

                ///Creates an array of [x,y] data points
                List<Integer> dataPoints = new ArrayList<Integer>();
                ///Adds the x
                dataPoints.add(new Integer(counter));
                ///Adds the y
                dataPoints.add(commitsObj);
                /// Adds the array of data points to an array
                commitArray.add(dataPoints);
                /// Adds a week to the array for weeks
                weeks.add(week);
            }
        }

        ///Returns the data in a hashmap
        model.put("weeks", weeks);
        model.put("values", commitArray);
        model.put("key", "GitHubData");

        return model;
    }

/*
    private <T> String getJSONString(List<T> passedInList) {

        ///Creates an empty initial String
        String jsonString = "";

        /// Creates chart data in the form [{<data1>},{<data2>}]
        ObjectMapper mapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        try {
            jsonString = mapper.writeValueAsString(passedInList);
        }catch(Exception e){
            e.printStackTrace();
        }

        return jsonString;
    }
*/
}
