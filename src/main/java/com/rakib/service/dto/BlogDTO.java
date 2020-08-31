package com.rakib.service.dto;

import lombok.Data;

import javax.persistence.Lob;
import java.time.Instant;

@Data
public class BlogDTO {
    private long id;
    private long userId;
    private String blogTitle;
    private String blogBody;
    private Instant blogPostTime;
    private boolean activeOrNot;
}
