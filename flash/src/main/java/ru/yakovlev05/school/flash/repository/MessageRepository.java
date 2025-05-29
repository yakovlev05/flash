package ru.yakovlev05.school.flash.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.yakovlev05.school.flash.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatId(Long chatId, Pageable pageable);

    @Query("""
            SELECT m FROM Message m
            WHERE m.id = :messageId
                AND m.sender.id = :userId
            """)
    Optional<Message> findByMessageIdAndUserId(Long messageId, Long userId);

    @Modifying
    @Query("""
            DELETE FROM Message m
            WHERE m.chat.id = :chatId
            """)
    void removeAllByChatId(Long chatId);
}
