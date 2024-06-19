package org.phani.ssoserver.ssoserver.Filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.SneakyThrows;
import org.phani.ssoserver.ssoserver.Entity.UserDetail;
import org.phani.ssoserver.ssoserver.Exception.CustomException;
import org.phani.ssoserver.ssoserver.Service.LoginValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
@Data
public class CustomFilter implements Filter {

    @Value("${jwt.secret}")
    private String secretKey = "Ptokenhanimy32characterultrasecureandultralongsecret";


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    LoginValidateService loginValidateService;


    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res =  (HttpServletResponse) servletResponse;
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String SSOToken = "";
            Date expiration = null;
            String username = "";
            Claims claims = null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (StringUtils.isEmpty(authentication)) {
                throw new CustomException("CUSTOM EXCEPTION - USER IS NOT PRESENT IN THE DB");
            }
            String tokenreq = request.getHeader("tkn");
       /* if (tokenreq.equalsIgnoreCase("")) {
            request.setAttribute("SSOTOKEN","");
        }
        else {*/
            request.setAttribute("SSOTOKEN", tokenreq);
            //}
            String passwordreq = request.getHeader("password");
            String usernamereq = request.getHeader("username");
            Boolean tknexp = Boolean.valueOf(request.getAttribute("TokenExp").toString());
            if (!StringUtils.isEmpty(tokenreq) && !tknexp && ((StringUtils.isEmpty(usernamereq))
                    && StringUtils.isEmpty(passwordreq))) { // token is not exxpired
                try {
                    claims = Jwts
                            .parser()
                            .setSigningKey(secretKey)
                            .build()
                            .parseClaimsJws(tokenreq)
                            .getBody();
                } catch (Exception e) {
                    request.setAttribute("validatedUserSSO", false);
                    request.setAttribute("SSOTOKEN", "");
                    request.setAttribute("TokenExp", "true");
                    filterChain.doFilter(servletRequest, servletResponse);
                }
                if (!StringUtils.isEmpty(claims)) {
                    String roles = claims.get("role").toString();
                    username = claims.getSubject();
                    expiration = claims.getExpiration();
                    String issuer = claims.getIssuer();
                    String validatedPassword = authentication.getCredentials().toString();
                    if (!StringUtils.isEmpty(claims)) {
                        if (roles.equalsIgnoreCase("SSOLogin")
                                && issuer.equalsIgnoreCase("SSOPhani")
                                && expiration.after(new Date())) {
                            //&& password.equalsIgnoreCase(validatedPassword)) {
                            request.setAttribute("validatedUserSSO", true);
                        } else if (expiration.before(new Date())) {
                            request.setAttribute("TokenExp", "true");
                        } else {
                            request.setAttribute("validatedUserSSO", false);
                            throw new BadCredentialsException("INVALID TOKEN");
                        }
                    }
                }
            }
            if ((StringUtils.isEmpty(usernamereq) && StringUtils.isEmpty(passwordreq)) && (tknexp
                    || StringUtils.isEmpty(tokenreq))) {
                request.setAttribute("validatedUserSSO", false);
                request.setAttribute("SSOTOKEN", "");
            } else if ((!StringUtils.isEmpty(tokenreq) && tknexp) || (!StringUtils.isEmpty(usernamereq)
                    && !StringUtils.isEmpty(passwordreq))) {
                try {
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            usernamereq,
                            passwordreq
                    ));
                } catch (BadCredentialsException e) {
                    try {
                        request.setAttribute("SSOTOKEN", "");
                        throw new Exception("INVALID CREDENTIALS", e);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                SSOToken = Jwts.builder()
                        .claim("role", "SSOLogin")
                        .setSubject(usernamereq)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 60000))
                        .signWith(SignatureAlgorithm.HS256, "Ptokenhanimy32characterultrasecureandultralongsecret")
                        .issuer("SSOPhani")
                        .compact();
                request.setAttribute("SSOTOKEN", SSOToken);
                request.setAttribute("validatedUserSSO", true);
                request.setAttribute("TokenExp", "false");
            }
            request.setAttribute("tokenusername", username);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (CustomException e) {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.getWriter().write(e.getMessage());
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
