package com.rakib.service.mapper;

import com.rakib.domain.UserInfo;
import com.rakib.service.dto.response.UserResponseDTO;

public interface UserMapper {
     UserResponseDTO toDTO(UserInfo info);
}
