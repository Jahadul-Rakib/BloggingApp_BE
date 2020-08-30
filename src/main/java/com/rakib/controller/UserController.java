package com.rakib.controller;

import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.rakib.service.dto.RequestData;
import com.rakib.service.dto.UserDTO;
import com.rakib.service.RoleService;
import com.rakib.utilities.JWTUtilities;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("api/v1/")
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
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserDTO user) {
        UserInfo saveUser = userService.saveUser(user);
        return ResponseEntity.ok().body(ImmutableMap.of("data", saveUser));
    }

    @PostMapping("addrole")
    public ResponseEntity<?> saveRole(@RequestBody UserRole userRole) {
        UserRole saveRole = roleService.saveRole(userRole);
        return ResponseEntity.ok().body(ImmutableMap.of("data", saveRole));
    }
    @GetMapping("user")
    public ResponseEntity<?> getUser() {
        List<UserInfo> userInfo = userService.getUsers();
        return ResponseEntity.ok().body(ImmutableMap.of("data", userInfo));
    }

    @GetMapping("user/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        UserInfo userInfo = userService.getUserByEmail(email);
        return ResponseEntity.ok().body(ImmutableMap.of("data", userInfo));
    }

    @PostMapping("login")
    public ResponseEntity<?> getLogin(@RequestBody RequestData requestData) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (requestData.getUsername(), requestData.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtilities.jwtTokenProvider();
        return ResponseEntity.ok().header("Authorization", "Bearer " + token).body(ImmutableMap.of("data", "Bearer " + token));
    }
}
