package ru.yakovlev05.school.flash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yakovlev05.school.flash.entity.ChatParticipant;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    @Query("""
            SELECT cp FROM ChatParticipant cp
            WHERE cp.id.chat.id = :chatID AND cp.id.user.id = :userId
            """)
    boolean isChatParticipant(Long chatId, Long userId);
}
