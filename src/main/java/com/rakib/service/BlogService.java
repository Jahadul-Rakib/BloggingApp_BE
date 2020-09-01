package com.rakib.service;

import com.rakib.domain.Blog;
import com.rakib.domain.enums.DataType;
import com.rakib.service.dto.BlogDTO;
import com.rakib.service.dto.BlogDetailsDTO;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface BlogService {
    Blog saveBlog(BlogDTO blogDTO) throws NotFoundException;

    Page<BlogDetailsDTO> getBlog(DataType action, Pageable pageable) throws Exception;

    BlogDetailsDTO getBlogById(Long id);

    Blog updateBlog(Long id, BlogDTO blogDTO) throws Exception;

    String deleteBlog(Long id) throws NotFoundException;

    Page<BlogDetailsDTO> getBlogByUserId(Long id, Pageable pageable);
}
