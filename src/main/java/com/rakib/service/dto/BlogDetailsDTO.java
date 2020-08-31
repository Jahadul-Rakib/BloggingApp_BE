package com.rakib.service.dto;

import com.rakib.domain.Blog;
import com.rakib.domain.Comments;
import com.rakib.domain.enums.Action;
import lombok.Data;

import java.util.List;

@Data
public class BlogDetailsDTO {
    private Blog blog;
    private List<Comments> commentList;
    private Integer totalLike;
    private Integer totalDisLike;
    private Action currentUserLikeOrDislike;
}
