package com.rakib.service;

import com.rakib.domain.Comments;
import com.rakib.service.dto.CommentDTO;
import javassist.NotFoundException;

public interface CommentService {
    CommentDTO saveComment(CommentDTO commentDTO) throws NotFoundException;

    CommentDTO updateComment(Long id, CommentDTO commentDTO) throws NotFoundException;
}
