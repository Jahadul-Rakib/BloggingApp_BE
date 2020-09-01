package com.rakib.service;

import com.rakib.service.dto.LikeDislikeDTO;
import javassist.NotFoundException;

public interface LikeDislikeService {
    LikeDislikeDTO saveLikeDislike(LikeDislikeDTO likeDislikeDTO) throws NotFoundException;
}
