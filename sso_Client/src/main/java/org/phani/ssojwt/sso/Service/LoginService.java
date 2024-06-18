package org.phani.ssojwt.sso.Service;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.phani.ssojwt.sso.Entity.DeviceData;
import org.phani.ssojwt.sso.Entity.UserDetail;
import org.phani.ssojwt.sso.Repository.DeviceRepository;
import org.phani.ssojwt.sso.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    @Value("${jwt.secret}")
    private String secretkey;

    @Value("${jwt.expiration}")
    private long JWTexpiration;

    @Value("${crypto.salt}")
    private String salt;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeviceRepository deviceRepository;

    public String generateJWTToken(String userName) {
        return Jwts.builder()
                .claim("role", "SSOLogin")
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWTexpiration))
                .signWith(SignatureAlgorithm.HS256, secretkey)
                .issuer("SSOPhani")
                .compact();
    }

    public String getTokenCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        List<Cookie> SSOToken = new ArrayList<Cookie>();
        if(!StringUtils.isEmpty(cookies)) {
            SSOToken = Arrays.stream(cookies).toList()
                    .stream()
                    .filter(p -> p.getName().equalsIgnoreCase("SSOTOKEN"))
                    .findFirst()
                    .stream().collect(Collectors.toList());
        }
        return SSOToken.toString();
    }

    @Transactional
    public String saveUserData(UserDetail userDetail) {
        userRepository.save(userDetail);
        return "saved";
    }
}

