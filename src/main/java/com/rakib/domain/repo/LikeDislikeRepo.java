package com.rakib.domain.repo;

import com.rakib.domain.LikeDislike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeDislikeRepo extends JpaRepository<LikeDislike, Long> {
}
