package com.rakib.service;

import com.rakib.domain.enums.DataType;
import com.rakib.service.dto.BlogDTO;
import com.rakib.service.dto.BlogPayloadDTO;
import com.rakib.service.dto.response.BlogDetailsDTO;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface BlogService {
    BlogDTO saveBlog(BlogPayloadDTO blogDTO) throws NotFoundException;

    Page<BlogDetailsDTO> getBlog(DataType action, Pageable pageable) throws Exception;

    BlogDetailsDTO getBlogById(Long id);

    BlogDTO updateBlog(Long id, BlogPayloadDTO blogDTO) throws Exception;

    String deleteBlog(Long id) throws NotFoundException;

    Page<BlogDetailsDTO> getBlogByUserId(Long id, Pageable pageable);
}
