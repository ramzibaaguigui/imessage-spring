package jyad.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> getMessageById(Long id);
    Boolean deleteMessageById(Long id);
    @Query("select m from Message m where m.discussion = ?1 and m.sentAt < ?2 and m.isDeleted = ?3 order by m.sentAt")
    List<Message> findMessagesByDiscussionAndSentAtBeforeAndIsDeletedIsOrderBySentAtDesc(
            String discussionId, Date sentBefore, Boolean isDeleted
    );




}
