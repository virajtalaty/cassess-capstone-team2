package edu.asu.cassess.service.slack;

import edu.asu.cassess.model.slack.MessageList;

public interface IChannelHistoryService {
    MessageList getPublicMessages(String channel, String token, long unixOldest, long unixCurrent, String course, String team);

    MessageList getPrivateMessages(String channel, String token, long unixOldest, long unixCurrent, String course, String team);

    void getMessageTotals(String channelID, String course, String team);

    void updateMessageTotals(String course);
}
