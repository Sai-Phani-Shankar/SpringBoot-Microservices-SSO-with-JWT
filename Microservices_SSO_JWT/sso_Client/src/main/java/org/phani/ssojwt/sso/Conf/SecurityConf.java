package org.phani.ssojwt.sso.Conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.phani.ssojwt.sso.Filter.CustomFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConf {

    public String[] Url = {
            "/h2-console",
            "/login",
            "/home",
            "/saveUser",
            "/Logout"
};

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                //.authorizeHttpRequests(auth -> auth.requestMatchers("/login").authenticated())
                .authorizeHttpRequests(auth -> auth.requestMatchers(Url).permitAll());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filter (HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(new CustomFilter(), BasicAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
