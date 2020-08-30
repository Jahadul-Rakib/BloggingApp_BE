package com.rakib.service.implimentation;

import com.rakib.domain.UserRole;
import com.rakib.enums.Roles;
import com.rakib.service.dto.UserDTO;
import com.rakib.domain.repo.UserRoleRepo;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rakib.domain.UserInfo;
import com.rakib.domain.repo.UserInfoRepo;
import com.rakib.service.UserService;

import java.rmi.activation.ActivationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
public class UserServiceImpl implements UserService {


    private final UserInfoRepo userInfoRepo;
    private final UserRoleRepo userRoleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserInfoRepo userinfo, UserRoleRepo userRoleRepo, PasswordEncoder passwordEncoder) {
        this.userInfoRepo = userinfo;
        this.userRoleRepo = userRoleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserInfo saveUser(UserDTO user) throws DuplicateName {
        List<UserRole> roles = new ArrayList<>();
        user.getRoleId().forEach(value -> {
            UserRole role = userRoleRepo.getOne(value);
            if (role.equals(Roles.ADMIN)) {
                Collection<? extends GrantedAuthority> admin = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
                admin.forEach(grantedAuthority -> {
                    if (grantedAuthority.equals(Roles.ADMIN)) {
                        try {
                            throw new ActivationException("To make an admin, you should be also an admin.");
                        } catch (ActivationException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            roles.add(role);
        });

        Optional<UserInfo> byUserName = userInfoRepo.getUserInfoByUserName(user.getUserName());
        if (byUserName.isPresent()){
            throw new DuplicateName("User Email Already Exist.");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(user.getUserName());
        userInfo.setUserEmail(user.getUserEmail());
        userInfo.setUserPhone(user.getUserPhone());
        userInfo.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        userInfo.setActive(false);
        userInfo.setRole(roles);

        return userInfoRepo.save(userInfo);
    }

    @Override
    public UserInfo getUserByEmail(String email) {
        return userInfoRepo.getUserInfoByUserEmail(email);
    }

    @Override
    public List<UserInfo> getUsers() {
        return userInfoRepo.findAll();
    }

    @Override
    public UserInfo updateUser(long id, UserDTO userDTO) throws Exception {
        Optional<UserInfo> userInfo = userInfoRepo.findById(id);
        if (userInfo.isPresent()) {
            if (nonNull(userDTO.getUserName())) {
                userInfo.get().setUserName(userDTO.getUserName());
            }
            if (nonNull(userDTO.getUserEmail())) {
                userInfo.get().setUserEmail(userDTO.getUserEmail());
            }
            if (nonNull(userDTO.getUserPassword())) {
                userInfo.get().setUserPassword(userDTO.getUserPassword());
            }
            if (userDTO.isActive()) {
                userInfo.get().setActive(userDTO.isActive());
            }

            return userInfoRepo.save(userInfo.get());
        }
        throw new Exception("User Not Found.");
    }

}
