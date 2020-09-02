package com.rakib.service.implimentation;

import com.rakib.domain.UserSecurityContext;
import com.rakib.domain.repo.CustomContextRepo;
import com.rakib.service.SecurityService;
import javassist.NotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecurityServiceImpl implements SecurityService {
    private final CustomContextRepo repo;

    public SecurityServiceImpl(CustomContextRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<UserSecurityContext> listOfContext() {
        return repo.findAll();
    }

    @Override
    public UserSecurityContext save(String user, String token) {
        UserSecurityContext context = new UserSecurityContext();
        context.setUser(user);
        context.setToken(token);
        System.out.println(context);
        return repo.save(context);
    }

    @Override
    public Optional<UserSecurityContext> findByUserName(String userName) {
        return repo.findById(userName);
    }

    @Override
    public String deleteByUserName() throws NotFoundException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        Optional<UserSecurityContext> user = repo.findById(userName);
        if (user.isPresent()) {
            repo.deleteById(userName);
            return "Log Out Successfully.";
        } else {
            throw new NotFoundException("Token Not Found By User.");
        }
    }
}
