package com.rakib.domain.repo;


import com.rakib.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rakib.domain.UserRole;

import java.util.Optional;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByUserRole(Roles roles);
}
