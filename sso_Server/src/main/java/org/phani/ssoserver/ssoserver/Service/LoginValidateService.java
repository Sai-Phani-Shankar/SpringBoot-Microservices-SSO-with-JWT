package org.phani.ssoserver.ssoserver.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.phani.ssoserver.ssoserver.Entity.UserDetail;
import org.phani.ssoserver.ssoserver.Repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class LoginValidateService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private UserDetailRepository userDetailRepository;

    public Boolean saveUserDeviceData(UserDetail user){
        Boolean isSaved = false;
        UserDetail userDetail = userDetailRepository.save(user);
        if (!StringUtils.isEmpty(userDetail)) {
            isSaved = true;
        }
        return isSaved;
    }

    public UserDetail findByEmail(String Email){
        UserDetail user = userDetailRepository.findByEmail(Email);
        return user;
    }

    public String extractSSOToken(String token) {
        Claims claims = null;
        String userName = "";
        try {
            claims = Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            userName = claims.getSubject().toString();
        } catch (Exception e) {
            return "";
        }
        return userName;
    }
}
