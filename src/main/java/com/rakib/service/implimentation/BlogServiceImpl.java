package com.rakib.service.implimentation;

import com.rakib.domain.Blog;
import com.rakib.domain.Comments;
import com.rakib.domain.LikeDislike;
import com.rakib.domain.UserInfo;
import com.rakib.domain.enums.Action;
import com.rakib.domain.enums.DataType;
import com.rakib.domain.repo.BlogRepo;
import com.rakib.domain.repo.CommentsRepo;
import com.rakib.domain.repo.LikeDislikeRepo;
import com.rakib.domain.repo.UserInfoRepo;
import com.rakib.service.BlogService;
import com.rakib.service.dto.BlogDTO;
import com.rakib.service.dto.BlogPayloadDTO;
import com.rakib.service.dto.CommentDTO;
import com.rakib.service.dto.response.BlogDetailsDTO;
import com.rakib.service.mapper.BlogMapper;
import com.rakib.service.mapper.CommentMapper;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private CommentMapper commentMapper;

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
    public BlogDTO saveBlog(BlogPayloadDTO blogDTO) throws NotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo user = userInfoRepo.getUserInfoByUserEmail(username);
        if (isNull(user)) {
            throw new NotFoundException("User Not Exist.");
        }
        Blog blog = new Blog();
        blog.setActive(blogDTO.isActiveOrNot());
        blog.setBlogTitle(blogDTO.getBlogTitle());
        blog.setBlogBody(blogDTO.getBlogBody());
        blog.setBlogPostTime(Instant.now());
        blog.setUserInfo(user);

        return blogMapper.toDTO(blogRepo.save(blog));
    }

    @Override
    public Page<BlogDetailsDTO> getBlog(DataType action, Pageable pageable) throws Exception {
        AtomicInteger totalLike = new AtomicInteger();
        List<BlogDetailsDTO> blogDetailsDTOS = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean authorized = authorities.contains(new SimpleGrantedAuthority("ADMIN"));
        Optional<List<Blog>> allBlog;
        if (authorized) {
            if (action == null) {
                allBlog = Optional.of(blogRepo.findAll());
            } else if (action.equals(DataType.ACTIVE)) {
                allBlog = blogRepo.findAllByActive(true);
            } else if (action.equals(DataType.INACTIVE)) {
                allBlog = blogRepo.findAllByActive(false);
            } else {
                throw new Exception("Data Type Exception.");
            }
        } else {
            allBlog = blogRepo.findAllByActive(true);
        }
        if (allBlog.isPresent()) {
            for (Blog blog : allBlog.get()) {
                BlogDetailsDTO blogDetailsDTO = new BlogDetailsDTO();
                blogDetailsDTO.setBlog(blogMapper.toDTO(blog));
                Optional<List<Comments>> commentsByBlog = commentsRepo.findByBlog(blog);
                List<CommentDTO> commentDTOS = new ArrayList<>();
                commentsByBlog.ifPresent(commentsList -> commentsList.forEach(comments -> {
                    commentDTOS.add(commentMapper.toDTO(comments));
                }));
                blogDetailsDTO.setCommentList(commentDTOS);
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
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                UserInfo userInfoByUserEmail = userInfoRepo.getUserInfoByUserEmail(username);

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

    @Override
    public BlogDetailsDTO getBlogById(Long id) {
        AtomicInteger totalLike = new AtomicInteger();
        Optional<Blog> blog = blogRepo.findById(id);
        BlogDetailsDTO blogDetailsDTO = new BlogDetailsDTO();
        if (blog.isPresent()) {
            blogDetailsDTO.setBlog(blogMapper.toDTO(blog.get()));
            Optional<List<Comments>> commentsByBlog = commentsRepo.findByBlog(blog.get());
            List<CommentDTO> commentDTOS = new ArrayList<>();
            commentsByBlog.ifPresent(commentsList -> commentsList.forEach(comments -> {
                commentDTOS.add(commentMapper.toDTO(comments));
            }));
            blogDetailsDTO.setCommentList(commentDTOS);

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

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UserInfo userInfoByUserEmail = userInfoRepo.getUserInfoByUserEmail(username);

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
    public BlogDTO updateBlog(Long id, BlogPayloadDTO blogPayloadDTO) throws Exception {
        Blog blog = new Blog();
        blog.setId(id);
        Optional<UserInfo> userInfo = userInfoRepo.findById(blogPayloadDTO.getUserId());
        blog.setUserInfo(userInfo.get());
        if (nonNull(blogPayloadDTO.getBlogTitle())) {
            blog.setBlogTitle(blogPayloadDTO.getBlogTitle());
        }
        if (nonNull(blogPayloadDTO.getBlogBody())) {
            blog.setBlogBody(blogPayloadDTO.getBlogBody());
        }
        if (nonNull(blogPayloadDTO.getBlogPostTime())) {
            blog.setBlogPostTime(blogPayloadDTO.getBlogPostTime());
        }

        if (blogPayloadDTO.isActiveOrNot()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean authorized = authorities.contains(new SimpleGrantedAuthority("ADMIN"));
            if (authorized) {
                blog.setActive(blogPayloadDTO.isActiveOrNot());
            } else {
                throw new Exception("Admin required to update activation Status.");
            }

        }
        return blogMapper.toDTO(blogRepo.save(blog));
    }

    @Override
    public String deleteBlog(Long id) throws NotFoundException {
        Optional<Blog> blog = blogRepo.findById(id);
        if (blog.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            boolean authorized = authorities.contains(new SimpleGrantedAuthority("ADMIN"));
            if (!authorized) {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                UserInfo userInfo = userInfoRepo.getUserInfoByUserEmail(username);
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
                blogDetailsDTO.setBlog(blogMapper.toDTO(blog));
                Optional<List<Comments>> commentsByBlog = commentsRepo.findByBlog(blog);
                List<CommentDTO> commentDTOS = new ArrayList<>();
                commentsByBlog.ifPresent(commentsList -> commentsList.forEach(comments -> {
                    commentDTOS.add(commentMapper.toDTO(comments));
                }));
                blogDetailsDTO.setCommentList(commentDTOS);

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

                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                UserInfo userInfoByUserEmail = userInfoRepo.getUserInfoByUserEmail(username);

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
