package com.rakib.service.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class CommentDTO {
    private long id;
    private Long userId;
    private Long blogId;
    private String comment;
    private Instant commentTime;
}
