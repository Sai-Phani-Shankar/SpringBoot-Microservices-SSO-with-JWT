package org.phani.ssoserver.ssoserver.Filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import org.phani.ssoserver.ssoserver.Entity.UserDetail;
import org.phani.ssoserver.ssoserver.Repository.UserDetailRepository;
import org.phani.ssoserver.ssoserver.Service.LoginValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.http.HttpRequest;

@Component
@Data
public class userDetailsCustomFilter implements UserDetailsService {

    @Autowired
    private UserDetailRepository userDetailRepository;


    @Override
    public UserDetails loadUserByUsername(String Email) throws UsernameNotFoundException {
        UserDetail user = userDetailRepository.findByEmail(Email);
        SecurityContextHolder securityContextHolder = new SecurityContextHolder();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user,
                user.getPassword(), null);
        securityContextHolder.getContext().setAuthentication(authentication);
        return User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
