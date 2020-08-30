package com.rakib.controller.api;

import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.rakib.service.dto.RequestData;
import com.rakib.service.dto.UserDTO;
import com.rakib.service.RoleService;
import com.rakib.utilities.JWTUtilities;
import lombok.NonNull;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.rakib.domain.UserInfo;
import com.rakib.domain.UserRole;
import com.rakib.service.UserService;

import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final JWTUtilities jwtUtilities;

    public UserController(AuthenticationManager authenticationManager, UserService userService, RoleService roleService, JWTUtilities jwtUtilities) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.jwtUtilities = jwtUtilities;

    }

    @PostMapping("adduser")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserDTO user) throws DuplicateName {
        UserInfo saveUser = userService.saveUser(user);
        return ResponseEntity.ok().body(ImmutableMap.of("data", saveUser));
    }

    @PostMapping("addrole")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveRole(@RequestBody UserRole userRole) {
        UserRole saveRole = roleService.saveRole(userRole);
        return ResponseEntity.ok().body(ImmutableMap.of("data", saveRole));
    }
    @GetMapping("user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUser() {
        List<UserInfo> userInfo = userService.getUsers();
        return ResponseEntity.ok().body(ImmutableMap.of("data", userInfo));
    }

    @GetMapping("user/{email}")
    @PreAuthorize("hasAnyRole('ADMIN','BLOGGER')")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        UserInfo userInfo = userService.getUserByEmail(email);
        return ResponseEntity.ok().body(ImmutableMap.of("data", userInfo));
    }
    @PutMapping("user/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','BLOGGER')")
    public ResponseEntity<?> updateUser(@NonNull @PathVariable long id,
                                        @RequestBody(required = false) UserDTO userDTO) throws Exception {
        UserInfo userInfo = userService.updateUser(id, userDTO);
        return ResponseEntity.ok().body(ImmutableMap.of("data", userInfo));
    }


    @PostMapping("login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getLogin(@RequestBody RequestData requestData) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (requestData.getUsername(), requestData.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtilities.jwtTokenProvider();
        return ResponseEntity.ok().header("Authorization",
                "Bearer " + token).body(ImmutableMap.of("data", "Bearer " + token));
    }
}