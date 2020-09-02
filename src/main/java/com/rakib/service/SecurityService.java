package com.rakib.service;

import com.rakib.domain.UserSecurityContext;
import javassist.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface SecurityService {
    List<UserSecurityContext> listOfContext();
    UserSecurityContext save(String name, String context);
    Optional<UserSecurityContext> findByUserName(String name);
    String deleteByUserName() throws NotFoundException;
}
