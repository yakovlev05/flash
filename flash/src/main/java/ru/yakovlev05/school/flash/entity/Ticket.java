package ru.yakovlev05.school.flash.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "Ticket")
@Data
public class Ticket {
    @Id
    private String ticket;
    private Long userId;

    @TimeToLive
    private Long timeout = 10L;
}
