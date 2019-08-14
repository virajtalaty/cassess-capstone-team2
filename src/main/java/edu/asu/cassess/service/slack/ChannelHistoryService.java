package edu.asu.cassess.service.slack;

import edu.asu.cassess.dao.slack.ISlackMessageDao;
import edu.asu.cassess.dao.slack.IUserObjectQueryDao;
import edu.asu.cassess.model.rest.CourseList;
import edu.asu.cassess.model.slack.MessageList;
import edu.asu.cassess.persist.entity.rest.*;
import edu.asu.cassess.persist.entity.slack.*;
import edu.asu.cassess.persist.repo.slack.SlackMessageRepo;
import edu.asu.cassess.persist.repo.slack.SlackMessageTotalsRepo;
import edu.asu.cassess.service.rest.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class ChannelHistoryService implements IChannelHistoryService {

    class MutableInt {
        int value = 1; // note that we start at 1 since we're counting
        public void increment () { ++value;      }
        public int  get ()       { return value; }
    }

    private RestTemplate restTemplate;

    private String channelHistoryURL;

    private String groupHistoryURL;

    private Map<String, MutableInt> countsMap = new HashMap<String, MutableInt>();

    @Autowired
    private IChannelService channelService;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IUserObjectQueryDao userObjectQueryDao;

    @Autowired
    private IStudentsService studentService;

    @Autowired
    private SlackMessageTotalsRepo slackMessageTotalsRepo;

    @Autowired
    private SlackMessageRepo slackMessageRepo;

    @Autowired
    private ISlackMessageDao slackMessageDao;

    public ChannelHistoryService() {
        restTemplate = new RestTemplate();

        //non-private slack channel history URL
        channelHistoryURL = "https://slack.com/api/channels.history";

        //private slack channel history URL
        groupHistoryURL = "https://slack.com/api/groups.history";
    }

    @Override
    public MessageList getPublicMessages(String channel, String token, long unixOldest, long unixCurrent, String course, String team) {

        long nextUnixCurrent = 0;

        //System.out.println("----------------------------**********************************************=========UnixCurrent: " + unixCurrent);

        //System.out.println("----------------------------**********************************************=========UnixOldest: " + unixOldest);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        //System.out.println("Page: " + page);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<MessageList> messageList = restTemplate.getForEntity(channelHistoryURL + "?token=" + token + "&channel=" + channel +
                "&oldest=" + unixOldest + "&latest=" + unixCurrent, MessageList.class, request);
        //System.out.println(messageList.getBody());

        SlackMessage[] slackMessages = messageList.getBody().getMessages();

        //System.out.println(messageList.getBody());

        int index = 0;
        for(SlackMessage slackMessage : slackMessages){
            //System.out.println("----------------------------**********************************************=========Ts: " + slackMessage.getTs());
            //System.out.println("----------------------------**********************************************=========User: " + slackMessage.getUser());
            if(slackMessage.getText().length() > 20) {
                //System.out.println("Message: "+slackMessage.getText());
                MutableInt count = countsMap.get(slackMessage.getUser());
                if (count == null) {
                    countsMap.put(slackMessage.getUser(), new MutableInt());
                } else {
                    count.increment();
                }
            }

            //slackMessageRepo.save(slackMessage);
            index++;
            if(index == (slackMessages.length -1)){
                nextUnixCurrent = (long) Math.floor(slackMessage.getTs());
                //System.out.println("----------------------------**********************************************=========NextUnixCurrent: " + nextUnixCurrent);
            }
        }

        //System.out.println("----------------------------**********************************************=========has_more: " + messageList.getBody().isHas_more());

        if (messageList.getBody().isHas_more()) {
            return getPublicMessages(channel, token, unixOldest, nextUnixCurrent, course, team);
        } else {
            return messageList.getBody();
        }
    }

    @Override
    public MessageList getPrivateMessages(String channel, String token, long unixOldest, long unixCurrent, String course, String team) {

        long nextUnixCurrent = 0;

        //System.out.println("----------------------------**********************************************=========UnixCurrent: " + unixCurrent);

        //System.out.println("----------------------------**********************************************=========UnixOldest: " + unixOldest);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        //System.out.println("Page: " + page);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<MessageList> messageList = restTemplate.getForEntity(groupHistoryURL + "?token=" + token + "&channel=" + channel +
                "&oldest=" + unixOldest + "&latest=" + unixCurrent, MessageList.class, request);

        System.out.println(messageList.getBody());

        SlackMessage[] slackMessages = messageList.getBody().getMessages();

        System.out.println(messageList.getBody());

        int index = 0;
        for(SlackMessage slackMessage : slackMessages){
            //System.out.println("----------------------------**********************************************=========Ts: " + slackMessage.getTs());
            //System.out.println("----------------------------**********************************************=========User: " + slackMessage.getUser());
            if(slackMessage.getText().length() > 0) {
                MutableInt count = countsMap.get(slackMessage.getUser());
                if (count == null) {
                    countsMap.put(slackMessage.getUser(), new MutableInt());
                } else {
                    count.increment();
                }
            }

            //slackMessageRepo.save(slackMessage);
            index++;
            if(index == (slackMessages.length -1)){
                nextUnixCurrent = (long) Math.floor(slackMessage.getTs());
                System.out.println("----------------------------**********************************************=========NextUnixCurrent: " + nextUnixCurrent);
            }
        }

        //System.out.println("----------------------------**********************************************=========has_more: " + messageList.getBody().isHas_more());

        if (messageList.getBody().isHas_more()) {
            return getPrivateMessages(channel, token, unixOldest, nextUnixCurrent, course,team);
        } else {
            return messageList.getBody();
        }
    }

    @Override
    public void getMessageTotals(String channelID, String course, String team, String date) {
        System.out.println("Update Start");
        List<Student> students = studentService.listReadByTeam(course, team);
        for (Student student : students) {
            System.out.println("Student: "+student.getFull_name());
            int messageCount = 0;
            List<UserObject> userObjects = userObjectQueryDao.getUsersByEmail(student.getEmail());
            if(userObjects != null) {
                for (UserObject userObject : userObjects) {
                    System.out.println(userObject.getId());
                    if (countsMap.get(userObject.getId()) != null) {
                        messageCount = countsMap.get(userObject.getId()).get();
                    }
                    //System.out.println("----------------------------**********************************************=========User: " + userObject.getId());
                    //System.out.println("----------------------------**********************************************=========Count: " + messageCount);
                    //int messageCount = slackMessageQueryDao.getMessageCount(userObject.getId());
                    if (student.getEnabled() != null) {
                        if (student.getEnabled() != false && messageCount>0 ) {
                            System.out.println("Saving");
                            slackMessageTotalsRepo.save(new SlackMessageTotals(new MessageTotalsID(userObject.getProfile().getEmail(), channelID, date), userObject.getProfile().getReal_name(), student.getTeam_name(), course, messageCount, student.getSlack_username()));
                        }
                    }
                }
            }
        }
    }


    @Override
    public void updateMessageTotals(String course) {
        System.out.println("Updating Messages");
        long unixOldest;
        long unixCurrent;
        long current = System.currentTimeMillis();
        if (courseService == null) courseService = new CourseService();
        Course tempCourse = (Course)courseService.read(course);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");


        //TODO : Why is this code even there
        /*
        try {
            current = new SimpleDateFormat("yyyy-mm-dd").parse(String.valueOf(new java.util.Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        //System.out.println("CurrentDate: " + current);
        //System.out.println("EndDate: " + tempCourse.getEnd_date());
            if (current < (tempCourse.getEnd_date().getTime())) {
                String token = tempCourse.getSlack_token();
                if (token != null) {
                    System.out.println("Token: " + token);
                    List<Team> teams = tempCourse.getTeams();
                    for (Team team : teams) {
                        List<Channel> channels = channelService.listReadByTeam(team.getTeam_name(), course);
                        for (Channel channel : channels) {
                            unixOldest = (long) slackMessageDao.getTimeOfLastMessage(channel.getId());
                            if (unixOldest == 0)
                                unixOldest = tempCourse.getStart_date().getTime();
                            unixCurrent = unixOldest += TimeUnit.DAYS.toMillis(1);
                            while (unixCurrent <= current) {
                                System.out.println("Channel: " + channel.getId());
                                System.out.println("From: " + df.format(unixOldest) + " To: " + df.format(unixCurrent));
                                if (channel.getId().startsWith("C")) {
                                    getPublicMessages(channel.getId(), token, unixOldest / 1000, unixCurrent / 1000, course, team.getTeam_name());
                                    getMessageTotals(channel.getId(), course, team.getTeam_name(),df.format(unixOldest));
                                    countsMap.clear();
                                }
                                if (channel.getId().startsWith("G")) {
                                    getPrivateMessages(channel.getId(), token, unixOldest / 1000, unixCurrent / 1000, course, team.getTeam_name());
                                    getMessageTotals(channel.getId(), course, team.getTeam_name(),df.format(unixOldest));
                                    countsMap.clear();
                                }
                                unixOldest = unixCurrent;
                                unixCurrent += TimeUnit.DAYS.toMillis(1);
                            }

                        }
                    }
                }
            }
    }
}
