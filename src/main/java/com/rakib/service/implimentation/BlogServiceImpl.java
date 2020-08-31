package com.rakib.service.implimentation;

import com.rakib.domain.Blog;
import com.rakib.domain.UserInfo;
import com.rakib.domain.enums.Roles;
import com.rakib.domain.repo.BlogRepo;
import com.rakib.domain.repo.UserInfoRepo;
import com.rakib.service.BlogService;
import com.rakib.service.dto.BlogDTO;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepo blogRepo;
    private final UserInfoRepo userInfoRepo;

    public BlogServiceImpl(BlogRepo blogRepo, UserInfoRepo userInfoRepo) {
        this.blogRepo = blogRepo;
        this.userInfoRepo = userInfoRepo;
    }

    @Override
    public Blog saveBlog(BlogDTO blogDTO) throws NotFoundException {
        Optional<UserInfo> user = userInfoRepo.findById(blogDTO.getUserId());
        if (!user.isPresent()) {
            throw new NotFoundException("User Not Exist.");
        }
        Blog blog = new Blog();
        blog.setActiveOrNot(blogDTO.isActiveOrNot());
        blog.setBlogTitle(blogDTO.getBlogTitle());
        blog.setBlogBody(blogDTO.getBlogBody());
        blog.setBlogPostTime(Instant.now());
        blog.setUserInfo(user.get());

        return blogRepo.save(blog);
    }

    @Override
    public Page<Blog> getBlog(Pageable pageable) {
        return blogRepo.findAll(pageable);
    }

    @Override
    public Blog getBlogById(Long id) {
        Optional<Blog> blogById = blogRepo.findById(id);
        return blogById.get();
    }

    @Override
    public Blog updateBlog(Long id, BlogDTO blogDTO) {
        Blog blog = new Blog();
        blog.setId(id);
        Optional<UserInfo> userInfo = userInfoRepo.findById(blogDTO.getUserId());
        blog.setUserInfo(userInfo.get());
        if (nonNull(blogDTO.getBlogTitle())) {
            blog.setBlogTitle(blogDTO.getBlogTitle());
        }
        if (nonNull(blogDTO.getBlogBody())) {
            blog.setBlogBody(blogDTO.getBlogBody());
        }
        if (nonNull(blogDTO.getBlogPostTime())) {
            blog.setBlogPostTime(blogDTO.getBlogPostTime());
        }
        if (blogDTO.isActiveOrNot()) {
            if (blogDTO.isActiveOrNot()) {
                Collection<? extends GrantedAuthority> admin = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
                admin.forEach(grantedAuthority -> {
                    if (!grantedAuthority.equals(Roles.ADMIN)) {
                        try {
                            throw new Exception("Admin required to update activation Status.");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            blog.setActiveOrNot(blogDTO.isActiveOrNot());
        }
        return blogRepo.save(blog);
    }

    @Override
    public String deleteBlog(Long id) throws NotFoundException {
        Optional<Blog> blog = blogRepo.findById(id);
        if (blog.isPresent()) {
            Collection<? extends GrantedAuthority> admin = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            admin.forEach(grantedAuthority -> {
                if (!grantedAuthority.equals(Roles.ADMIN)) {
                    String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
                    UserInfo userInfo = userInfoRepo.getUserInfoByUserEmail(principal);
                    if (userInfo.equals(blog.get().getUserInfo())){
                        try {
                            throw new Exception("You Have not permission perform delete action in the post.");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            blogRepo.deleteById(id);
        } else {
            throw new NotFoundException("Blog not found by " + id);
        }
        return "Deleted Successfully.";
    }
}
