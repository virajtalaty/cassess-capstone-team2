package edu.asu.cassess.service.slack;

import edu.asu.cassess.model.slack.MessageList;

/**
 * Created by Thomas on 4/22/2017.
 */
public interface IChannelHistoryService {
    MessageList getMessages(String channel, String token, long unixOldest, long unixCurrent);

    void getMessageTotals(String channelID, String course, String team);

    void updateMessageTotals(String course);
}
