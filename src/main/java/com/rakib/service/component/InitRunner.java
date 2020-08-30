package com.rakib.service.component;

import com.rakib.domain.UserInfo;
import com.rakib.domain.UserRole;
import com.rakib.enums.Roles;
import com.rakib.service.RoleService;
import com.rakib.service.UserService;
import com.rakib.service.dto.UserDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;


//@Component
public class InitRunner implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;

    public InitRunner(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {

        try {
            addUser();
        }catch (Exception e){
            e.getMessage();
        }


    }

    private void addUser( ) {
        UserRole role = addRoles();

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("Admin");
        userDTO.setUserEmail("rakib38@gmail.com");
        userDTO.setUserPassword("123456");
        userDTO.setUserPhone("01680023583");
        userDTO.setRoleId(Arrays.asList(role.getId()));
        userDTO.setActive(true);
        userService.saveUser(userDTO);
    }

    private UserRole addRoles() {
        UserRole roleAdmin = new UserRole();
        roleAdmin.setUserRole(Roles.ADMIN);
        UserRole role = roleService.saveRole(roleAdmin);
        return role;
    }
}
