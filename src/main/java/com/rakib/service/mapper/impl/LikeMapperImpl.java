package com.rakib.service.mapper.impl;

import com.rakib.domain.LikeDislike;
import com.rakib.service.dto.LikeDislikeDTO;
import com.rakib.service.mapper.LikeMapper;
import org.springframework.stereotype.Service;

@Service
public class LikeMapperImpl implements LikeMapper {
    @Override
    public LikeDislikeDTO toDTO(LikeDislike likeDislike) {
        LikeDislikeDTO dto = new LikeDislikeDTO();
        dto.setId(likeDislike.getId());
        dto.setActionTime(likeDislike.getActionTime());
        dto.setBlogId(likeDislike.getBlog().getId());
        dto.setUserName(likeDislike.getUserInfo().getUsername());
        dto.setLikeOrDislike(likeDislike.isLikeOrDislike());
        return dto;
    }
}
