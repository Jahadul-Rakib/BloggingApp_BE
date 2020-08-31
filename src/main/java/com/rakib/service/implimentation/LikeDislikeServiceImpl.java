package com.rakib.service.implimentation;

import com.rakib.domain.Blog;
import com.rakib.domain.LikeDislike;
import com.rakib.domain.UserInfo;
import com.rakib.domain.repo.BlogRepo;
import com.rakib.domain.repo.LikeDislikeRepo;
import com.rakib.domain.repo.UserInfoRepo;
import com.rakib.service.LikeDislikeService;
import com.rakib.service.dto.LikeDislikeDTO;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class LikeDislikeServiceImpl implements LikeDislikeService {
    private final UserInfoRepo userInfoRepo;
    private final BlogRepo blogRepo;
    private final LikeDislikeRepo likeDislikeRepo;

    public LikeDislikeServiceImpl(UserInfoRepo userInfoRepo, BlogRepo blogRepo, LikeDislikeRepo likeDislikeRepo) {
        this.userInfoRepo = userInfoRepo;
        this.blogRepo = blogRepo;
        this.likeDislikeRepo = likeDislikeRepo;
    }


    @Override
    public LikeDislike saveLikeDislike(LikeDislikeDTO likeDislikeDTO) throws NotFoundException {
        LikeDislike likeDislike = new LikeDislike();

        Optional<UserInfo> user = userInfoRepo.findById(likeDislikeDTO.getUserId());
        if (!user.isPresent()) {
            throw new NotFoundException("User Not Found.");
        }
        Optional<Blog> blog = blogRepo.findById(likeDislikeDTO.getBlogId());
        if (!blog.isPresent()) {
            throw new NotFoundException("Blog Not Found.");
        }
        Optional<LikeDislike> byUserInfoAndBlog = likeDislikeRepo.findByUserInfoAndBlog(user.get(), blog.get());

        byUserInfoAndBlog.ifPresent(dislike -> likeDislike.setId(dislike.getId()));

        likeDislike.setLikeOrDislike(likeDislikeDTO.isLikeOrDislike());
        likeDislike.setActionTime(Instant.now());
        likeDislike.setUserInfo(user.get());
        likeDislike.setBlog(blog.get());
        return likeDislikeRepo.save(likeDislike);
    }
}
