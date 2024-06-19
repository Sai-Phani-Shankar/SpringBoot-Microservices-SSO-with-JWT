package org.phani.ssojwt.sso.Filter;

import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String endPoint = request.getRequestURI();
        String os = "";
        String browser = "";
        String deviceType = "";

        if (endPoint.equalsIgnoreCase("/home")){
            String browserDetails = request.getHeader("User-Agent");
            UserAgent userAgent = UserAgent.parseUserAgentString(browserDetails);
            deviceType = userAgent.getOperatingSystem().getDeviceType().getName();
            if (browserDetails.toLowerCase().indexOf("windows") >= 0 )
            {
                os = "Windows";
            } else if(browserDetails.toLowerCase().indexOf("mac") >= 0)
            {
                os = "Mac";
            } else if(browserDetails.toLowerCase().indexOf("x11") >= 0)
            {
                os = "Unix";
            } else if(browserDetails.toLowerCase().indexOf("android") >= 0)
            {
                os = "Android";
            } else if(browserDetails.toLowerCase().indexOf("iphone") >= 0)
            {
                os = "IPhone";
            }else{
                os = "UnKnown, More-Info: "+browserDetails;
            }
            //===============Browser===========================
            if (browserDetails.toLowerCase().contains("msie"))
            {
                String substring=browserDetails.substring(browserDetails.indexOf("MSIE")).split(";")[0];
                browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
            } else if (browserDetails.toLowerCase().contains("safari") && browserDetails.toLowerCase().contains("version"))
            {
                browser=(browserDetails.substring(browserDetails.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(browserDetails.substring(browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
            } else if ( browserDetails.toLowerCase().contains("opr") || browserDetails.toLowerCase().contains("opera"))
            {
                if(browserDetails.toLowerCase().contains("opera"))
                    browser=(browserDetails.substring(browserDetails.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(browserDetails.substring(browserDetails.indexOf("Version")).split(" ")[0]).split("/")[1];
                else if(browserDetails.toLowerCase().contains("opr"))
                    browser=((browserDetails.substring(browserDetails.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
            } else if (browserDetails.toLowerCase().contains("chrome"))
            {
                browser=(browserDetails.substring(browserDetails.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
            } else if ((browserDetails.toLowerCase().indexOf("mozilla/7.0") > -1) || (browserDetails.toLowerCase().indexOf("netscape6") != -1)  || (browserDetails.toLowerCase().indexOf("mozilla/4.7") != -1) || (browserDetails.toLowerCase().indexOf("mozilla/4.78") != -1) || (browserDetails.toLowerCase().indexOf("mozilla/4.08") != -1) || (browserDetails.toLowerCase().indexOf("mozilla/3") != -1) )
            {
                //browser=(browserDetails.substring(browserDetails.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
                browser = "Netscape-?";

            } else if (browserDetails.toLowerCase().contains("firefox"))
            {
                browser=(browserDetails.substring(browserDetails.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
            } else if(browserDetails.toLowerCase().contains("rv"))
            {
                browser="IE-" + browserDetails.toLowerCase().substring(browserDetails.toLowerCase().indexOf("rv") + 3, browserDetails.toLowerCase().indexOf(")"));
            } else
            {
                browser = "UnKnown, More-Info: "+browserDetails;
            }

            request.setAttribute("browser",browser);
            request.setAttribute("os",os);
            request.setAttribute("device",deviceType);
        }


        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
