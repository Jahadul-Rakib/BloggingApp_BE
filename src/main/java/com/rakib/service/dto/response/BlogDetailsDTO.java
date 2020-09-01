package com.rakib.service.dto.response;


import com.rakib.domain.enums.Action;
import com.rakib.service.dto.BlogDTO;
import com.rakib.service.dto.CommentDTO;
import lombok.Data;

import java.util.List;

@Data
public class BlogDetailsDTO {
    private BlogDTO blog;
    private List<CommentDTO> commentList;
    private Integer totalLike;
    private Integer totalDisLike;
    private Action currentUserLikeOrDislike;
}
