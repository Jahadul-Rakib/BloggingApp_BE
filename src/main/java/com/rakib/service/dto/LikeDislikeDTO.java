package com.rakib.service.dto;


import lombok.Data;
import java.time.Instant;

@Data
public class LikeDislikeDTO {
    private long id;
    private String userName;
    private Long blogId;
    private boolean likeOrDislike;
    private Instant actionTime;
}
