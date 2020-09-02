package com.rakib.domain.repo;

import com.rakib.domain.UserSecurityContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomContextRepo extends JpaRepository<UserSecurityContext, String> {
    Optional<UserSecurityContext> findByUser(String user);
    Optional<UserSecurityContext> findByToken(String token);
}
