package com.rakib.service;

import com.rakib.domain.Blog;
import com.rakib.service.dto.BlogDTO;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BlogService {
    Blog saveBlog(BlogDTO blogDTO) throws NotFoundException;

    Page<Blog> getBlog(Pageable pageable);

    Blog getBlogById(Long id);

    Blog updateBlog(Long id, BlogDTO blogDTO);

    String deleteBlog(Long id) throws NotFoundException;
}
