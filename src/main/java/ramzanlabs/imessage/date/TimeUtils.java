package ramzanlabs.imessage.date;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;


@Component
public class TimeUtils {

    public Date now() {
        return Date.from(Instant.now());
    }

}
