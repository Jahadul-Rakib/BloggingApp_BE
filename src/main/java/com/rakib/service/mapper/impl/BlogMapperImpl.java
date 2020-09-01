package com.rakib.service.mapper.impl;

import com.rakib.domain.Blog;
import com.rakib.service.dto.BlogDTO;
import com.rakib.service.mapper.BlogMapper;
import com.rakib.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class BlogMapperImpl implements BlogMapper {
    private final UserMapper userMapper;

    public BlogMapperImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public BlogDTO toDTO(Blog blog) {

        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setId(blog.getId());
        blogDTO.setActiveOrNot(blog.isActive());
        blogDTO.setUser(userMapper.toDTO(blog.getUserInfo()));
        blogDTO.setBlogTitle(blog.getBlogTitle());
        blogDTO.setBlogBody(blog.getBlogBody());
        blogDTO.setBlogPostTime(blog.getBlogPostTime());
        return blogDTO;
    }
}
