package com.rakib.service.component;

import com.rakib.domain.UserInfo;
import com.rakib.domain.Role;
import com.rakib.domain.repo.UserInfoRepo;
import com.rakib.domain.repo.UserRoleRepo;
import com.rakib.domain.enums.Roles;
import com.rakib.service.dto.UserDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Component
public class InitRunner implements CommandLineRunner {
    private final UserInfoRepo userInfoRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepo userRoleRepo;

    public InitRunner(UserInfoRepo userInfoRepo, PasswordEncoder passwordEncoder, UserRoleRepo userRoleRepo) {
        this.userInfoRepo = userInfoRepo;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepo = userRoleRepo;
    }

    @Override
    public void run(String... args) {
        addUser();
    }

    private void addUser() {
        Role roleAdmin = addRoles(1L,Roles.ADMIN);
        Role roleBlogger = addRoles(2L,Roles.BLOGGER);

        UserDTO adminDTO = new UserDTO();
        adminDTO.setUserName("Admin");
        adminDTO.setUserEmail("rakib38@diit.info");
        adminDTO.setUserPassword("123456");
        adminDTO.setUserPhone("01680023583");
        adminDTO.setRoleId(Collections.singletonList(roleAdmin.getId()));
        adminDTO.setActive(true);
        saveUser(adminDTO);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("Blogger");
        userDTO.setUserEmail("rakibdiu2015@gmail.com");
        userDTO.setUserPassword("123456");
        userDTO.setUserPhone("01680023583");
        userDTO.setRoleId(Collections.singletonList(roleBlogger.getId()));
        userDTO.setActive(true);
        saveUser(userDTO);
    }

    private void saveUser(UserDTO user) {
        Optional<UserInfo> userChecking = userInfoRepo.getUserInfoByUserName(user.getUserName());
        if (!userChecking.isPresent()) {
            List<Role> roles = new ArrayList<>();
            user.getRoleId().forEach(value -> {
                Role role = userRoleRepo.getOne(value);
                roles.add(role);
            });

            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(user.getUserName());
            userInfo.setUserEmail(user.getUserEmail());
            userInfo.setUserPhone(user.getUserPhone());
            userInfo.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
            userInfo.setActive(user.isActive());
            userInfo.setRole(roles);


            userInfoRepo.save(userInfo);

        }
    }

    private Role addRoles(Long id, Roles roles) {
        Role role = new Role();
        role.setName(roles);
        role.setId(id);
        Optional<Role> byRole = userRoleRepo.findByName(roles);
        return byRole.orElseGet(() -> userRoleRepo.save(role));
    }
}
