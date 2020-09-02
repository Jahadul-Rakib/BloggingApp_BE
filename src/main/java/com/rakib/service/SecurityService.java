package com.rakib.service;

import com.rakib.domain.UserSecurityContext;

import java.util.Optional;

public interface SecurityService {
    UserSecurityContext save(String name, String context);
    Optional<UserSecurityContext> findByToken(String token);
    String deleteByUserName() throws Exception;
}
