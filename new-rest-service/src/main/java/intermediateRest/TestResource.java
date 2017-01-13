package intermediateRest;

public class TestResource {

    private final long id;
    private final String content;

    public TestResource(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
