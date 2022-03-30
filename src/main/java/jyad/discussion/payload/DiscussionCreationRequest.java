package jyad.discussion.payload;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiscussionCreationRequest {
    private String name;
    private List<Long> usersIds;
    private Long createdById;


}
