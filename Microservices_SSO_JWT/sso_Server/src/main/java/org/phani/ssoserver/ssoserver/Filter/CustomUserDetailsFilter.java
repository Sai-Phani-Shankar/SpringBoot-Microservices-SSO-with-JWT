package org.phani.ssoserver.ssoserver.Filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomUserDetailsFilter extends OncePerRequestFilter {

    @Autowired
    userDetailsCustomFilter userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = servletRequest;
        String tokenreq = request.getHeader("tkn");
        String passwordreq = request.getHeader("password");
        String usernamereq = request.getHeader("username");
        Claims claims = null;
        if (!StringUtils.isEmpty(tokenreq) && ((StringUtils.isEmpty(usernamereq))
                && StringUtils.isEmpty(passwordreq))) {
            try {
                claims = Jwts
                        .parser()
                        .setSigningKey("Ptokenhanimy32characterultrasecureandultralongsecret")
                        .build()
                        .parseClaimsJws(tokenreq)
                        .getBody();
            }
            catch (Exception e)
            {
                //request.setAttribute("SSOTOKEN","");
                request.setAttribute("TokenExp","true");
                request.setAttribute("validatedUserSSO", false);
                filterChain.doFilter(servletRequest, servletResponse);
            }
            if (!StringUtils.isEmpty(claims)) {
                try {
                    userDetailsService.loadUserByUsername(claims.getSubject());
                }
                catch(UsernameNotFoundException e) {
                    request.setAttribute("TokenExp","false");
                    throw new BadCredentialsException("INVALID USER DATA");
                }
                request.setAttribute("TokenExp","false");
            }
            request.setAttribute("TokenExp","false");
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else if(StringUtils.isEmpty(tokenreq) && (!StringUtils.isEmpty(usernamereq))
                && !StringUtils.isEmpty(passwordreq)) {
            userDetailsService.loadUserByUsername(usernamereq);
            request.setAttribute("TokenExp","false");
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else if (StringUtils.isEmpty(tokenreq) && (StringUtils.isEmpty(usernamereq))
                && StringUtils.isEmpty(passwordreq)) {
            request.setAttribute("TokenExp","false");
            throw new BadCredentialsException("INVALID USER DATA");
        }
        else {
            try {
                userDetailsService.loadUserByUsername(usernamereq);
            }
            catch (UsernameNotFoundException e) {
                request.setAttribute("TokenExp","false");
                throw new BadCredentialsException("INVALID USER DATA");
            }
            request.setAttribute("TokenExp","false");
            filterChain.doFilter(servletRequest, servletResponse);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
