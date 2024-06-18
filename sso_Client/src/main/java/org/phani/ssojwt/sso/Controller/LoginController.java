package org.phani.ssojwt.sso.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.phani.ssojwt.sso.Entity.DeviceData;
import org.phani.ssojwt.sso.Entity.ResponseBody;
import org.phani.ssojwt.sso.Entity.UserDetail;
import org.phani.ssojwt.sso.Repository.DeviceRepository;
import org.phani.ssojwt.sso.Repository.UserRepository;
import org.phani.ssojwt.sso.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Controller
@Getter
@Setter
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ResponseBody responseBody;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    Client client = ClientBuilder.newClient();
    WebTarget webTarget = client.target("http://127.0.0.1:9090/ValidateLogin");


    @GetMapping("/login")
    public String LoginPage(){
        return "login";
    }


    @GetMapping("/home")
    public String HomePage(HttpServletRequest req, Model model, @RequestParam("username") String userName,
                           @RequestParam("password") String password) throws Exception {
        String os = req.getAttribute("os").toString();
        String browser = req.getAttribute("browser").toString();
        String deviceType = req.getAttribute("device").toString();
        String SSOToken = responseBody.getSSOToken();
        //String tokenUname =
        UserDetail userDetail = UserDetail
                .builder()
                .Email(userName)
                .token(SSOToken)
                .os(os)
                .browser(browser)
                .deviceType(deviceType)
                .build();
        String json = objectMapper.writeValueAsString(userDetail);
        Response response = webTarget
                .request(MediaType.APPLICATION_JSON)
                .header("tkn", SSOToken)
                .header("username",userName)
                .header("password",password)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON));
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            String responseData = response.readEntity(String.class);
            ResponseBody responseBodyResponse = objectMapper.readValue(responseData, ResponseBody.class);
            if (responseBodyResponse.getUserAndTokenValid()) {
                responseBody.setSSOToken(responseBodyResponse.getSSOToken());
                response.close();
                return "Home";
            }
            else {
                req.setAttribute("BadCreds","isBad");
                req.setAttribute("message", "Bad Credentials or SSO Token expired. Please Login Again");
                response.close();
                return "Login";
            }
        }
            else {
                req.setAttribute("BadCreds","isBad");
                req.setAttribute("message", "Bad Credentials or SSO Token expired. Please Login Again");
                response.close();
                return "Login";
            }
    }

    @PostMapping("/saveUser")
    public ResponseEntity<String> saveData(@RequestBody UserDetail userDetail) {
        DeviceData deviceData = DeviceData
                .builder()
                .browser("OS")
                .deviceType("Computer")
                .os("Windows 11")
                        .build();
        UserDetail u = UserDetail
                .builder()
                .Email("abc@gmail.com")
                .FirstName("Phani")
                .LastName("ShankarRao")
                .password("$2a$10$ZPAJdBZD15tSJZPAqdMYSuhFJJVr8FVvrL5Y5E3Uzet5o5NkpDGxa")
                .deviceData(deviceData)
                .build();
        loginService.saveUserData(u);
        Optional<String> optional = Optional.of("Saved to DB Succesfully");
        return ResponseEntity.of(optional);
    }
}
