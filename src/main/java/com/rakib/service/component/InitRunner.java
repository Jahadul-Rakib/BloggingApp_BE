package com.rakib.service.component;

import com.rakib.domain.UserInfo;
import com.rakib.domain.Role;
import com.rakib.domain.repo.UserInfoRepo;
import com.rakib.domain.repo.UserRoleRepo;
import com.rakib.domain.enums.Roles;
import com.rakib.service.RoleService;
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
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepo userRoleRepo;

    public InitRunner(UserInfoRepo userInfoRepo, RoleService roleService, PasswordEncoder passwordEncoder, UserRoleRepo userRoleRepo) {
        this.userInfoRepo = userInfoRepo;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepo = userRoleRepo;
    }

    @Override
    public void run(String... args) {
        addUser();
    }

    private void addUser() {
        Role role = addRoles();

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("Admin");
        userDTO.setUserEmail("rakib38@diit.info");
        userDTO.setUserPassword("123456");
        userDTO.setUserPhone("01680023583");
        userDTO.setRoleId(Collections.singletonList(role.getId()));
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

    private Role addRoles() {
        Role roleAdmin = new Role();
        roleAdmin.setName(Roles.ADMIN);
        Optional<Role> byRole = userRoleRepo.findByName(Roles.ADMIN);
        return byRole.orElseGet(() -> roleService.saveRole(roleAdmin));
    }
}
