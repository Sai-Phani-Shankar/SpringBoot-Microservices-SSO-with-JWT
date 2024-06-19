package org.phani.ssoserver.ssoserver.Conf;

import org.phani.ssoserver.ssoserver.Filter.CustomFilter;
import org.phani.ssoserver.ssoserver.Filter.CustomUserDetailsFilter;
import org.phani.ssoserver.ssoserver.Filter.userDetailsCustomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;


@EnableWebSecurity
@Configuration
public class SpringSecurityConf {

    @Autowired
    private CustomUserDetailsFilter customUserDetailsFilter;

    @Autowired
    private CustomFilter customFilter ;

    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/ValidateLogin").permitAll())
                .addFilterBefore(customUserDetailsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(customFilter, BasicAuthenticationFilter.class);

        return http.build();

    }
}
