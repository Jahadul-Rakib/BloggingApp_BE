package com.rakib.domain.repo;

import com.rakib.domain.Blog;
import com.rakib.domain.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentsRepo extends JpaRepository<Comments, Long> {
    Optional<List<Comments>> findByBlog(Blog blog);
}
