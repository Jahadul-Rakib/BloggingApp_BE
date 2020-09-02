package com.rakib.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;
@Data
@RedisHash
public class UserSecurityContext implements Serializable {
    @Id
    private String user;
    private String token;
}
