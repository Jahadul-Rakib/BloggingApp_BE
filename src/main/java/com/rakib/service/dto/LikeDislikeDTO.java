package com.rakib.service.dto;

import com.rakib.domain.Blog;
import com.rakib.domain.UserInfo;
import lombok.Data;

import javax.persistence.ManyToOne;
import java.time.Instant;

@Data
public class LikeDislikeDTO {
    private long id;
    private Long userId;
    private Long blogId;
    private boolean likeOrDislike;
    private Instant actionTime;
}
