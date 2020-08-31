package com.rakib.domain.repo;

import com.rakib.domain.Blog;
import com.rakib.domain.LikeDislike;
import com.rakib.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeDislikeRepo extends JpaRepository<LikeDislike, Long> {
    Optional<LikeDislike> findByUserInfoAndBlog(UserInfo userInfo, Blog blog);
    Optional<List<LikeDislike>> findByBlog(Blog blog);
}
