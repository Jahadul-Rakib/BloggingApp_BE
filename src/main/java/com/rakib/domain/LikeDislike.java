package com.rakib.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Data
@Entity
public class LikeDislike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private UserInfo userInfo;
    @ManyToOne
    private Blog blog;
    private boolean likeOrDislike;
    private Instant actionTime;
}
