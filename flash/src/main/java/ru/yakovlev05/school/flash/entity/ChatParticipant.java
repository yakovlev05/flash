package ru.yakovlev05.school.flash.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "chat_participants")
public class ChatParticipant {

    @EmbeddedId
    private CompositeId id;

    @ManyToOne
    @JoinColumn(name = "chat_id", insertable = false, updatable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    private LocalDateTime joinedAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMIN, PARTICIPANT
    }

    @EqualsAndHashCode
    @Embeddable
    public static class CompositeId {

        @Column(name = "chat_id")
        private Long chatId;

        @Column(name = "user_id")
        private Long userId;
    }
}
