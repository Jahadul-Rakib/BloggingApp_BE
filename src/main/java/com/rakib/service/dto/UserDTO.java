package com.rakib.service.dto;


import com.rakib.config.EmilRegex;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;


@Data
public class UserDTO {
    @NotNull
    private String userName;
    private String userPhone;
    @Email
    @Pattern(regexp = EmilRegex.EMAIL_REGEX, message = "Invalid email address")
    private String userEmail;
    @NotNull
    private String userPassword;
    @NotNull
    private List<Long> roleId;
    private boolean active;
}
