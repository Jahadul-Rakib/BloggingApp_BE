package com.rakib.service;

import com.rakib.domain.LikeDislike;
import com.rakib.service.dto.LikeDislikeDTO;
import javassist.NotFoundException;

public interface LikeDislikeService {
    LikeDislike saveLikeDislike(LikeDislikeDTO likeDislikeDTO) throws NotFoundException;
}
