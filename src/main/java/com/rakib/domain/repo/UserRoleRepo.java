package com.rakib.domain.repo;


import com.rakib.domain.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rakib.domain.Role;

import java.util.Optional;

@Repository
public interface UserRoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Roles roles);
}
