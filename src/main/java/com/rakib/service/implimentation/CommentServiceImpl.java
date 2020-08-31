package com.rakib.service.implimentation;

import com.rakib.domain.Blog;
import com.rakib.domain.Comments;
import com.rakib.domain.UserInfo;
import com.rakib.domain.repo.BlogRepo;
import com.rakib.domain.repo.CommentsRepo;
import com.rakib.domain.repo.UserInfoRepo;
import com.rakib.service.CommentService;
import com.rakib.service.dto.CommentDTO;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentsRepo commentsRepo;
    private final UserInfoRepo userInfoRepo;
    private final BlogRepo blogRepo;

    public CommentServiceImpl(CommentsRepo commentsRepo, UserInfoRepo userInfoRepo, BlogRepo blogRepo) {
        this.commentsRepo = commentsRepo;
        this.userInfoRepo = userInfoRepo;
        this.blogRepo = blogRepo;
    }

    @Override
    public Comments saveComment(CommentDTO commentDTO) throws NotFoundException {
        Optional<UserInfo> user = userInfoRepo.findById(commentDTO.getUserId());
        if (!user.isPresent()) {
            throw new NotFoundException("User Not Found.");
        }
        Optional<Blog> blog = blogRepo.findById(commentDTO.getBlogId());
        if (!blog.isPresent()) {
            throw new NotFoundException("Blog Not Found.");
        }
        Comments comments = new Comments();
        comments.setComment(commentDTO.getComment());
        comments.setCommentTime(Instant.now());
        comments.setUserInfo(user.get());
        comments.setBlog(blog.get());

        if (new Long(commentDTO.getId()) != null){
            comments.setId(commentDTO.getId());
        }
        return commentsRepo.save(comments);
    }

    @Override
    public Comments updateComment(Long id, CommentDTO commentDTO) throws NotFoundException{
        Optional<Comments> comment = commentsRepo.findById(id);
        if (comment.isPresent()){
            return saveComment(commentDTO);
        };
        throw new NotFoundException("Comment Not Found");
    }
}
