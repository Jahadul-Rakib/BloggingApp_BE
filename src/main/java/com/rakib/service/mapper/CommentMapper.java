package com.rakib.service.mapper;

import com.rakib.domain.Comments;
import com.rakib.service.dto.CommentDTO;

public interface CommentMapper {
    CommentDTO toDTO(Comments comments);
}
