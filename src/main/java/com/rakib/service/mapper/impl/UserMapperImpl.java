package com.rakib.service.mapper.impl;

import com.rakib.domain.UserInfo;
import com.rakib.service.dto.response.UserResponseDTO;
import com.rakib.service.mapper.UserMapper;
import org.springframework.stereotype.Service;


@Service
public class UserMapperImpl implements UserMapper {
    @Override
    public UserResponseDTO toDTO(UserInfo info) {

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setUserId(info.getId());
        responseDTO.setUserName(info.getUsername());
        responseDTO.setUserEmail(info.getUserEmail());
        responseDTO.setUserPhone(info.getUserPhone());
        responseDTO.setActive(info.isActive());

        return responseDTO;
    }

}
