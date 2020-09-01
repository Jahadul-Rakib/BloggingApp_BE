package com.rakib.service.dto.response;

import lombok.Data;


@Data
public class UserResponseDTO {
    private Long userId;
    private String userName;
    private String userPhone;
    private String userEmail;
    private boolean active;
}
