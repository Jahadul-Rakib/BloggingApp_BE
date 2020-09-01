package com.rakib.utilities;

import com.rakib.config.AppConstants;
import com.rakib.domain.UserInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class JWTUtilities {


    public String jwtTokenProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        UserInfo e = (UserInfo) principal;

        List<String> roleName = new ArrayList<>();
        e.getRole().forEach(role -> {
            roleName.add(String.valueOf(role.getName()));
        });

        if (!authentication.isAuthenticated()) {
            System.out.println("Not Authenticate !!!");
        }

        String Key = AppConstants.KEY;
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", roleName)
                .setIssuedAt(new java.util.Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                .signWith(Keys.hmacShaKeyFor(Key.getBytes()))
                .compact();

        return token;

    }

}
