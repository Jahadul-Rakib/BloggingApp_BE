package com.rakib.controller.api;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.rakib.service.SecurityService;
import com.rakib.service.dto.RequestData;
import com.rakib.service.dto.UserDTO;
import com.rakib.service.RoleService;
import com.rakib.service.dto.response.UserResponseDTO;
import com.rakib.utilities.JWTUtilities;
import javassist.NotFoundException;
import lombok.NonNull;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.rakib.domain.Role;
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
    private final SecurityService securityService;

    public UserController(AuthenticationManager authenticationManager,
                          UserService userService,
                          RoleService roleService,
                          JWTUtilities jwtUtilities,
                          SecurityService securityService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.jwtUtilities = jwtUtilities;
        this.securityService = securityService;
    }

    @PostMapping("user")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserDTO user) throws DuplicateName {
        UserResponseDTO saveUser = userService.saveUser(user);
        return ResponseEntity.ok().body(ImmutableMap.of("data", saveUser));
    }

    @PostMapping("role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> saveRole(@RequestBody Role userRole) throws Exception {
        Role saveRole = roleService.saveRole(userRole);
        return ResponseEntity.ok().body(ImmutableMap.of("data", saveRole));
    }

    @GetMapping("role")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getRole() {
        List<Role> getRole = roleService.getRole();
        return ResponseEntity.ok().body(ImmutableMap.of("data", getRole));
    }

    @GetMapping("user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getUser(Pageable pageable) {
        Page<UserResponseDTO> userInfo = userService.getUsers(pageable);
        return ResponseEntity.ok().body(ImmutableMap.of("data", userInfo));
    }

    @GetMapping("user/{email}")
    @PreAuthorize("hasAnyAuthority('ADMIN','BLOGGER')")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        UserResponseDTO userInfo = userService.getUserByEmail(email);
        return ResponseEntity.ok().body(ImmutableMap.of("data", userInfo));
    }

    @PutMapping("user/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','BLOGGER')")
    public ResponseEntity<?> updateUser(@NonNull @PathVariable long id,
                                        @RequestBody(required = false) UserDTO userDTO) throws Exception {
        UserResponseDTO userInfo = userService.updateUser(id, userDTO);
        return ResponseEntity.ok().body(ImmutableMap.of("data", userInfo));
    }

    @DeleteMapping("user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable long id) throws NotFoundException {
        String userInfo = userService.deleteUser(id);
        return ResponseEntity.ok().body(ImmutableMap.of("data", userInfo));
    }

    @PostMapping("logout")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> logOutUser() throws NotFoundException {
        String logOut = securityService.deleteByUserName();
        return ResponseEntity.ok().body(ImmutableMap.of("data", logOut));
    }

    @PostMapping("login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> getLogin(@RequestBody RequestData requestData) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (requestData.getUsername(), requestData.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtilities.jwtTokenProvider();
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails)authentication1.getPrincipal()).getUsername();
        securityService.save(username, token);
        return ResponseEntity.ok().header("Authorization",
                "Bearer " + token).body(ImmutableMap.of("data", "Bearer " + token, "userType", authentication1.getAuthorities()));
    }
}
