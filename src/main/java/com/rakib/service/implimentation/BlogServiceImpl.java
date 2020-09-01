package com.rakib.service.implimentation;

import com.rakib.domain.Blog;
import com.rakib.domain.Comments;
import com.rakib.domain.LikeDislike;
import com.rakib.domain.UserInfo;
import com.rakib.domain.enums.Action;
import com.rakib.domain.enums.DataType;
import com.rakib.domain.enums.Roles;
import com.rakib.domain.repo.BlogRepo;
import com.rakib.domain.repo.CommentsRepo;
import com.rakib.domain.repo.LikeDislikeRepo;
import com.rakib.domain.repo.UserInfoRepo;
import com.rakib.service.BlogService;
import com.rakib.service.dto.BlogDTO;
import com.rakib.service.dto.BlogDetailsDTO;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.nonNull;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepo blogRepo;
    private final UserInfoRepo userInfoRepo;
    private final CommentsRepo commentsRepo;
    private final LikeDislikeRepo likeDislikeRepo;

    public BlogServiceImpl(BlogRepo blogRepo, UserInfoRepo userInfoRepo, CommentsRepo commentsRepo, LikeDislikeRepo likeDislikeRepo) {
        this.blogRepo = blogRepo;
        this.userInfoRepo = userInfoRepo;
        this.commentsRepo = commentsRepo;
        this.likeDislikeRepo = likeDislikeRepo;
    }

    @Override
    public Blog saveBlog(BlogDTO blogDTO) throws NotFoundException {
        Optional<UserInfo> user = userInfoRepo.findById(blogDTO.getUserId());
        if (!user.isPresent()) {
            throw new NotFoundException("User Not Exist.");
        }
        Blog blog = new Blog();
        blog.setActive(blogDTO.isActiveOrNot());
        blog.setBlogTitle(blogDTO.getBlogTitle());
        blog.setBlogBody(blogDTO.getBlogBody());
        blog.setBlogPostTime(Instant.now());
        blog.setUserInfo(user.get());

        return blogRepo.save(blog);
    }

    @Override
    public Page<BlogDetailsDTO> getBlog(DataType action, Pageable pageable) {
        AtomicInteger totalLike = new AtomicInteger();
        List<BlogDetailsDTO> blogDetailsDTOS = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean authorized = authorities.contains(new SimpleGrantedAuthority("ADMIN"));
        Optional<List<Blog>> allBlog;
        if (authorized) {
            if (action.equals(DataType.INACTIVE)) {
                allBlog = blogRepo.findAllByActive(false);
            } else if (action.equals(DataType.ACTIVE)) {
                allBlog = blogRepo.findAllByActive(true);
            } else {
                allBlog = Optional.of(blogRepo.findAll());
            }

        } else {
            allBlog = blogRepo.findAllByActive(true);
        }
        for (Blog blog : allBlog.get()) {
            BlogDetailsDTO blogDetailsDTO = new BlogDetailsDTO();
            blogDetailsDTO.setBlog(blog);
            Optional<List<Comments>> commentsByBlog = commentsRepo.findByBlog(blog);
            commentsByBlog.ifPresent(blogDetailsDTO::setCommentList);


            Optional<List<LikeDislike>> likeOrDislikeByBlog = likeDislikeRepo.findByBlog(blog);
            if (likeOrDislikeByBlog.isPresent()) {
                likeOrDislikeByBlog.get().forEach(likeDislike -> {
                    if (likeDislike.isLikeOrDislike()) {
                        totalLike.getAndIncrement();
                    }
                });
                blogDetailsDTO.setTotalLike(totalLike.get());
                blogDetailsDTO.setTotalDisLike(likeOrDislikeByBlog.get().size() - totalLike.get());
            }

            String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            UserInfo userInfoByUserEmail = userInfoRepo.getUserInfoByUserEmail(userName);

            Optional<LikeDislike> byUserInfoAndBlog = likeDislikeRepo.findByUserInfoAndBlog(userInfoByUserEmail, blog);
            if (byUserInfoAndBlog.isPresent()) {
                if (byUserInfoAndBlog.get().isLikeOrDislike()) {
                    blogDetailsDTO.setCurrentUserLikeOrDislike(Action.LIKE);
                } else {
                    blogDetailsDTO.setCurrentUserLikeOrDislike(Action.DISLIKE);
                }
            } else {
                blogDetailsDTO.setCurrentUserLikeOrDislike(Action.NOACTION);
            }

            blogDetailsDTOS.add(blogDetailsDTO);
        }

        return new PageImpl<BlogDetailsDTO>(blogDetailsDTOS, pageable, blogDetailsDTOS.size());
    }

    @Override
    public BlogDetailsDTO getBlogById(Long id) {
        AtomicInteger totalLike = new AtomicInteger();
        Optional<Blog> blog = blogRepo.findById(id);
        BlogDetailsDTO blogDetailsDTO = new BlogDetailsDTO();
        if (blog.isPresent()) {
            blogDetailsDTO.setBlog(blog.get());
            Optional<List<Comments>> commentsByBlog = commentsRepo.findByBlog(blog.get());
            commentsByBlog.ifPresent(blogDetailsDTO::setCommentList);

            Optional<List<LikeDislike>> likeOrDislikeByBlog = likeDislikeRepo.findByBlog(blog.get());
            if (likeOrDislikeByBlog.isPresent()) {
                likeOrDislikeByBlog.get().forEach(likeDislike -> {
                    if (likeDislike.isLikeOrDislike()) {
                        totalLike.getAndIncrement();
                    }
                });
                blogDetailsDTO.setTotalLike(totalLike.get());
                blogDetailsDTO.setTotalDisLike(likeOrDislikeByBlog.get().size() - totalLike.get());
            }

            String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            UserInfo userInfoByUserEmail = userInfoRepo.getUserInfoByUserEmail(userName);

            Optional<LikeDislike> byUserInfoAndBlog = likeDislikeRepo.findByUserInfoAndBlog(userInfoByUserEmail, blog.get());
            if (byUserInfoAndBlog.isPresent()) {
                if (byUserInfoAndBlog.get().isLikeOrDislike()) {
                    blogDetailsDTO.setCurrentUserLikeOrDislike(Action.LIKE);
                } else {
                    blogDetailsDTO.setCurrentUserLikeOrDislike(Action.DISLIKE);
                }
            } else {
                blogDetailsDTO.setCurrentUserLikeOrDislike(Action.NOACTION);
            }
        }
        return blogDetailsDTO;
    }

    @Override
    public Blog updateBlog(Long id, BlogDTO blogDTO) throws Exception {
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
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean authorized = authorities.contains(new SimpleGrantedAuthority("ADMIN"));
            if (authorized) {
                blog.setActive(blogDTO.isActiveOrNot());
            } else {
                throw new Exception("Admin required to update activation Status.");
            }

        }
        return blogRepo.save(blog);
    }

    @Override
    public String deleteBlog(Long id) throws NotFoundException {
        Optional<Blog> blog = blogRepo.findById(id);
        if (blog.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean authorized = authorities.contains(new SimpleGrantedAuthority("ADMIN"));
            if (!authorized) {
                String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
                UserInfo userInfo = userInfoRepo.getUserInfoByUserEmail(principal);
                if (userInfo.equals(blog.get().getUserInfo())) {
                    blogRepo.findById(id);
                } else {
                    try {
                        throw new Exception("You Have not permission perform delete action in the post.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            blogRepo.deleteById(id);
        } else {
            throw new NotFoundException("Blog not found by " + id);
        }
        return "Deleted Successfully.";
    }

    @Override
    public Page<BlogDetailsDTO> getBlogByUserId(Long id, Pageable pageable) {
        AtomicInteger totalLike = new AtomicInteger();

        List<BlogDetailsDTO> blogDetailsDTOS = new ArrayList<>();

        Optional<UserInfo> userInfo = userInfoRepo.findById(id);
        if (userInfo.isPresent()) {
            Optional<List<Blog>> allBlog = blogRepo.findAllByUserInfo(userInfo);
            for (Blog blog : allBlog.get()) {
                BlogDetailsDTO blogDetailsDTO = new BlogDetailsDTO();
                blogDetailsDTO.setBlog(blog);
                Optional<List<Comments>> commentsByBlog = commentsRepo.findByBlog(blog);
                commentsByBlog.ifPresent(blogDetailsDTO::setCommentList);


                Optional<List<LikeDislike>> likeOrDislikeByBlog = likeDislikeRepo.findByBlog(blog);
                if (likeOrDislikeByBlog.isPresent()) {
                    likeOrDislikeByBlog.get().forEach(likeDislike -> {
                        if (likeDislike.isLikeOrDislike()) {
                            totalLike.getAndIncrement();
                        }
                    });
                    blogDetailsDTO.setTotalLike(totalLike.get());
                    blogDetailsDTO.setTotalDisLike(likeOrDislikeByBlog.get().size() - totalLike.get());
                }

                String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
                UserInfo userInfoByUserEmail = userInfoRepo.getUserInfoByUserEmail(userName);

                Optional<LikeDislike> byUserInfoAndBlog = likeDislikeRepo.findByUserInfoAndBlog(userInfoByUserEmail, blog);
                if (byUserInfoAndBlog.isPresent()) {
                    if (byUserInfoAndBlog.get().isLikeOrDislike()) {
                        blogDetailsDTO.setCurrentUserLikeOrDislike(Action.LIKE);
                    } else {
                        blogDetailsDTO.setCurrentUserLikeOrDislike(Action.DISLIKE);
                    }
                } else {
                    blogDetailsDTO.setCurrentUserLikeOrDislike(Action.NOACTION);
                }

                blogDetailsDTOS.add(blogDetailsDTO);
            }
        }
        return new PageImpl<BlogDetailsDTO>(blogDetailsDTOS, pageable, blogDetailsDTOS.size());
    }
}
