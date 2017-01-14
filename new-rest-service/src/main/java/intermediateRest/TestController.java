package intermediateRest;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/intermediateRest", method = RequestMethod.GET, produces = "application/json")
    public TestResource greeting(@RequestParam(value="name", defaultValue="No Name") String name) {
        return new TestResource(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
