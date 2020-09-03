package com.rakib.service.implimentation;

import com.rakib.domain.Blog;
import com.rakib.domain.Comments;
import com.rakib.domain.UserInfo;
import com.rakib.domain.repo.BlogRepo;
import com.rakib.domain.repo.CommentsRepo;
import com.rakib.domain.repo.UserInfoRepo;
import com.rakib.service.CommentService;
import com.rakib.service.dto.CommentDTO;
import com.rakib.service.mapper.CommentMapper;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;

    private final CommentsRepo commentsRepo;
    private final UserInfoRepo userInfoRepo;
    private final BlogRepo blogRepo;

    public CommentServiceImpl(CommentsRepo commentsRepo, UserInfoRepo userInfoRepo, BlogRepo blogRepo) {
        this.commentsRepo = commentsRepo;
        this.userInfoRepo = userInfoRepo;
        this.blogRepo = blogRepo;
    }

    @Override
    public CommentDTO saveComment(CommentDTO commentDTO) throws Exception {
        Optional<UserInfo> user = userInfoRepo.getUserInfoByUserName(commentDTO.getUserName());
        if (user.isPresent()) {
            Optional<Blog> blog = blogRepo.findById(commentDTO.getBlogId());
            if (blog.isPresent()) {
                Comments comments = new Comments();
                comments.setComment(commentDTO.getComment());
                comments.setCommentTime(Instant.now());
                comments.setUserInfo(user.get());
                comments.setBlog(blog.get());
                comments.setId(commentDTO.getId());

                return commentMapper.toDTO(commentsRepo.save(comments));

            }
            throw new Exception("Blog Not Found.");
        }
        throw new Exception("User Not Found");
    }

    @Override
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) throws Exception {
        Optional<Comments> comment = commentsRepo.findById(id);
        if (comment.isPresent()) {
            commentDTO.setId(id);
            return saveComment(commentDTO);
        }
        throw new NotFoundException("Comment Not Found");
    }
}
