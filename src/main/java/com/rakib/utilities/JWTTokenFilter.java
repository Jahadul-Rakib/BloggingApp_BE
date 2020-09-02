package com.rakib.utilities;

import com.rakib.config.AppConstants;
import com.rakib.domain.UserSecurityContext;
import com.rakib.service.SecurityService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Service
public class JWTTokenFilter extends OncePerRequestFilter {

    private final SecurityService securityService;
    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    private final String SECRET = AppConstants.KEY;

    public JWTTokenFilter(SecurityService context) {
        this.securityService = context;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            if (checkJWTToken(request, response)) {
                Claims claims = validateToken(request);
                authCheck(claims);
                if (claims.get("authorities") != null) {
                    setUpSpringAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void authCheck(Claims claims) throws Exception {
        String email= String.valueOf(claims.get("sub"));
        Optional<UserSecurityContext> context = securityService.findByUserName(email);
        if (!context.isPresent()){
            throw new Exception("You Must Log In Again.");
        }
    }

    private Claims validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    private void setUpSpringAuthentication(Claims claims) {
        @SuppressWarnings("unchecked")
        List<Object> authorities = Collections.singletonList(claims.get("authorities"));//[{id=1, name=ADMIN, authority=ADMIN}]
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        authorities.forEach(value -> {
            String replace = value.toString().replace("[", "");
            String replace1 = replace.replace("]", "");
            String[] split = replace1.split(",");
            for (String actualVaue: split){
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(actualVaue);
                simpleGrantedAuthorities.add(authority);
            }
        });
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken
                (claims.getSubject(), null, simpleGrantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    private boolean checkJWTToken(HttpServletRequest request, HttpServletResponse res) {
        String authenticationHeader = request.getHeader(HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
            return false;
        return true;
    }

}
