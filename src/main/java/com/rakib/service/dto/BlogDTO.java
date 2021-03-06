package com.rakib.service.dto;

import com.rakib.service.dto.response.UserResponseDTO;
import lombok.Data;

import javax.persistence.Lob;
import java.time.Instant;

@Data
public class BlogDTO {
    private long id;
    private UserResponseDTO user;
    private String blogTitle;
    private String blogBody;
    private Instant blogPostTime;
    private boolean activeOrNot;
}
