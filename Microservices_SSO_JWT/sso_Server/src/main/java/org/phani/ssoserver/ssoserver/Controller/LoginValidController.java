package org.phani.ssoserver.ssoserver.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.phani.ssoserver.ssoserver.Entity.ResponseBody;
import org.phani.ssoserver.ssoserver.Entity.UserDetail;
import org.phani.ssoserver.ssoserver.Exception.CustomException;
import org.phani.ssoserver.ssoserver.Service.LoginValidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginValidController {

    @Autowired
    private LoginValidateService loginValidateService;

    @PostMapping("/ValidateLogin")
    public ResponseBody ValidateLogin(@RequestBody UserDetail userDetail, HttpServletRequest request) throws CustomException {
        ResponseBody responseBody = new ResponseBody();
        UserDetail userDetails = new UserDetail();
        String tokenName = "";
        String SSOToken = request.getAttribute("SSOTOKEN").toString();
        if (!StringUtils.isEmpty(SSOToken)) {
            tokenName = loginValidateService.extractSSOToken(SSOToken);
        }
        if (SSOToken.equalsIgnoreCase("")) {
            userDetails = loginValidateService.findByEmail(userDetail.getEmail());
        }
        else if (!tokenName.equalsIgnoreCase("")) {
            userDetails = loginValidateService.findByEmail(tokenName);
        }
        Boolean isTokenValid = Boolean.parseBoolean(String.valueOf(request.getAttribute("validatedUserSSO")));
        if (request.getAttribute("TokenExp").toString().equalsIgnoreCase("true")) {
            responseBody.setSSOToken("");
        }
        else {
            responseBody.setSSOToken(request.getAttribute("SSOTOKEN").toString());
        }
        responseBody.setUserAndTokenValid(!StringUtils.isEmpty(userDetails) && isTokenValid);
        if (!StringUtils.isEmpty(userDetails)) {
            responseBody.setUsername(userDetails.getFirstName() + " " + userDetails.getLastName());
        }
        System.out.println("SSOToken is " +SSOToken);
        return responseBody;
    }
}
