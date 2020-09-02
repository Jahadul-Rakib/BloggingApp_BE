package com.rakib.domain.repo;

import com.rakib.domain.UserSecurityContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomContextRepo extends JpaRepository<UserSecurityContext, String> {
}
