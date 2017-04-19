package edu.asu.cassess.dao.slack;

import edu.asu.cassess.dao.CAssessDAO;
import edu.asu.cassess.persist.entity.slack.SlackAuth;
import edu.asu.cassess.persist.entity.slack.UserInfo;
import edu.asu.cassess.persist.entity.slack.UserList;
import edu.asu.cassess.persist.entity.slack.UserObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class ConsumeUsers {

    private RestTemplate restTemplate;
    private String baseURL;
    private String token;
    private SlackAuth auth;

    @Autowired
    private CAssessDAO dao;

    public ConsumeUsers() {
        restTemplate = new RestTemplate();
        baseURL = "https://slack.com/api/";
        auth = new SlackAuth();
    }

    public UserList getUserList() {
        String ulURL = baseURL + "users.list" + token;
        UserList ul = restTemplate.getForObject(ulURL, UserList.class);
        return ul;
    }

    public UserObject getUserInfo(String userID) {
        auth = dao.find(SlackAuth.class, 1);
        token = auth.getToken();
        String uidURL = baseURL + "users.info" + token + "&user=" + userID;
        System.out.println("Fetching from " + uidURL);
        UserInfo retUser = restTemplate.getForObject(uidURL, UserInfo.class);
        System.out.println("Some info from data storage class " + retUser.getUser().getName());
        return dao.save(retUser.getUser());
    }

}

