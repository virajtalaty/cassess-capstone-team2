package edu.asu.cassess.model.slack;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.asu.cassess.persist.entity.slack.SlackMessage;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import java.util.List;

@Entity
@Subselect("")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageList {

    private boolean ok;
    private List<SlackMessage> messages;
    private boolean has_more;
    private boolean is_limited;

    public MessageList() {

    }

    /**
     * @return the ok
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * @param ok the ok to set
     */
    public void setOk(boolean ok) {
        this.ok = ok;
    }

    /**
     * @return the messages
     */
    public List<SlackMessage> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(List<SlackMessage> messages) {
        this.messages = messages;
    }

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public boolean isIs_limited() {
        return is_limited;
    }

    public void setIs_limited(boolean is_limited) {
        this.is_limited = is_limited;
    }
}

