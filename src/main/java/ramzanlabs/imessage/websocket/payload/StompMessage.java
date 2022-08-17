package ramzanlabs.imessage.websocket.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.GsonJsonParser;

import java.lang.reflect.Method;
import java.util.List;

public class StompMessage<T> {

    public StompMessage() {

    }

    @JsonProperty("mPayload")
    private String payload;

    @JsonProperty("mStompCommand")
    private String stompCommand;

    @JsonProperty("mStompHeaders")
    private List<StompHeader> stompHeaders;

    public String getStompCommand() {
        return this.stompCommand;
    }

    public T getPayload() throws JsonProcessingException, NoSuchMethodException {
        ObjectMapper mapper = new ObjectMapper();

        // figuring out the T type
        Method method = StompMessage.class.getDeclaredMethod("getPayload");
        Class<?> returnType = method.getReturnType();

        return (T) mapper.readValue(payload, returnType);

    }

    public List<StompHeader> getStompHeaders() {
        return this.stompHeaders;
    }

    public String getHeader(String key) {
        return stompHeaders.stream()
                .filter(stompHeader -> stompHeader.key().equals(key))
                .findFirst()
                .map(StompHeader::value)
                .orElse(null);
    }

    static class StompHeader {
        @JsonProperty("mKey")
        private String key;

        @JsonProperty("mValue")
        private String value;

        public String key() {
            return this.key;
        }

        public String value() {
            return this.value;
        }

        public void setKey(String key) {
            this.key = key;
        }
        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class StompCommand {

        public static final String CONNECT = "CONNECT";
        public static final String CONNECTED = "CONNECTED";
        public static final String SEND = "SEND";
        public static final String MESSAGE = "MESSAGE";
        public static final String SUBSCRIBE = "SUBSCRIBE";
        public static final String UNSUBSCRIBE = "UNSUBSCRIBE";

        public static final String UNKNOWN = "UNKNOWN";
    }
}
