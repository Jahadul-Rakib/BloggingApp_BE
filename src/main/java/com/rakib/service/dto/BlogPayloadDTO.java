package com.rakib.service.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class BlogPayloadDTO {
    private Long userId;
    private String blogTitle;
    private String blogBody;
    private Instant blogPostTime;
    private boolean activeOrNot;
}
