package existingRest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Members {

    private String id;
    private String real_name;

    public Members() {
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.real_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String real_name) {
        this.real_name = real_name;
    }

    @Override
    public String toString() {
        return "Members[{" +
                "id=" + id +
                ", real_name='" + real_name + '\'' +
                '}'+']';
    }
}
