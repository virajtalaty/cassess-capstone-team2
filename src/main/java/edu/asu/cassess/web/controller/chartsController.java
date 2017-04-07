package edu.asu.cassess.web.controller;

import edu.asu.cassess.persist.entity.github.CommitData;
import edu.asu.cassess.service.charts.ChartsService;
import edu.asu.cassess.service.github.GatherGitHubData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class chartsController {

    ///Keeps track of the timezone for Phoenix
    private final ZoneId zoneId = ZoneId.of("America/Phoenix");

    @Autowired
    private GatherGitHubData gatherGitHubData;

    @Autowired
    private ChartsService chartsService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/GitHubIndividualCommitChart", produces = "application/json")
    public  Map<String, Object> gitHubIndividualCommitChartsResource(){

        ///Creates a hash map
        Map<String, Object> model = new HashMap<>();

        ///Gets the commit data as a whole
        List<CommitData> chartData = gatherGitHubData.getCommitList();

        //Converts the List to JSON String
        String jsonString = chartsService.getJSONString(chartData);

        /// Create returned collections that will be arrays in the json object
        List<List<Long>> commitArray = chartsService.getGitHubChartDataPoints(jsonString, "tjjohn1");

        model.put("values", commitArray);
        model.put("key", "GitHubData");

        return model;
    }
/*
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/TaigaBarGraph", produces = "application/json")
    public  Map<String, Object> TaigaBarGraphChartsResource(){

        ///Creates a hash map
        Map<String, Object> model = new HashMap<>();

        ///Gets the commit data as a whole
        List<CommitData> chartData = gatherGitHubData.getCommitList();

        //Converts the List to JSON String
        String jsonString = chartsService.getJSONString(chartData);

        /// Create returned collections that will be arrays in the json object
        List<List<Long>> commitArray = chartsService.getGitHubChartDataPoints(jsonString, "tjjohn1");

        model.put("values", commitArray);
        model.put("key", "GitHubData");

        return model;
    }
*/
}
