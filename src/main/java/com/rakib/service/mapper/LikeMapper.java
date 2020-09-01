package com.rakib.service.mapper;

import com.rakib.domain.LikeDislike;
import com.rakib.service.dto.LikeDislikeDTO;

public interface LikeMapper {
    LikeDislikeDTO toDTO(LikeDislike likeDislike);
}
