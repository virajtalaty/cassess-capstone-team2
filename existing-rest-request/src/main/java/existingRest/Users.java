package existingRest;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Users {

    private String ok;
    private List<Members> members;

    public Users() {
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public List<Members> getMembers() {
        return members;
    }

    public void setValue(List<Members> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Users{" +
                "ok='" + ok + '\'' +
                ", members=" + members +
                '}';
    }
}
