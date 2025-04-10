package ru.yakovlev05.school.flash.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String title;

    @OneToMany(mappedBy = "chat")
    private List<ChatParticipant> participants = new ArrayList<>();

    public enum Type {
        PRIVATE, GROUP
    }
}
