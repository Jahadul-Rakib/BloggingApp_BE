package com.rakib.domain.repo;

import com.rakib.domain.Blog;
import com.rakib.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepo extends JpaRepository<Blog, Long> {
    Optional<List<Blog>> findAllByUserInfo(Optional<UserInfo> userInfo);
    Optional<List<Blog>> findAllByActive(boolean activeOrNot);
}
