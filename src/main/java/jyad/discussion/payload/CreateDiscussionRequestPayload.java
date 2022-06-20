package jyad.discussion.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateDiscussionRequestPayload {

    @JsonProperty("name")
    private String discussionName;

    @JsonProperty("users")
    private Set<String> discussionUsersUsernames;
}
