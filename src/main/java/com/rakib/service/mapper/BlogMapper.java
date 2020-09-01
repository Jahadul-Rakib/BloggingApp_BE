package com.rakib.service.mapper;

import com.rakib.domain.Blog;
import com.rakib.service.dto.BlogDTO;
import com.rakib.service.dto.response.BlogDetailsDTO;

public interface BlogMapper {
    BlogDTO toDTO(Blog blog);
}
