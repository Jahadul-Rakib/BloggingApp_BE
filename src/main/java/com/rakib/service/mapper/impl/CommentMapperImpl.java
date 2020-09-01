package com.rakib.service.mapper.impl;

import com.rakib.domain.Comments;
import com.rakib.service.dto.CommentDTO;
import com.rakib.service.mapper.CommentMapper;
import org.springframework.stereotype.Service;

@Service
public class CommentMapperImpl implements CommentMapper {
    @Override
    public CommentDTO toDTO(Comments comments) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comments.getId());
        dto.setCommentTime(comments.getCommentTime());
        dto.setBlogId(comments.getBlog().getId());
        dto.setUserName(comments.getUserInfo().getUsername());
        dto.setComment(comments.getComment());
        return dto;
    }
}
