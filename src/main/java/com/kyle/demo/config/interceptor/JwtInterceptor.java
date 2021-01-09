package com.kyle.demo.config.interceptor;

import com.kyle.demo.Util.JwtUtil;
import com.kyle.demo.annotation.JwtIgnore;
import com.kyle.demo.config.Audience;
import com.kyle.demo.constant.Constants;
import com.kyle.demo.entity.UserInfo;
import com.kyle.demo.exception.OauthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kz37
 */
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private Audience audience;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            JwtIgnore jwtIgnore = handlerMethod.getMethodAnnotation(JwtIgnore.class);
            if (null != jwtIgnore) {
                return true;
            }
        }
        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            // response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        final String authHeader = request.getHeader(JwtUtil.AUTH_HEADER_KEY);
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith(JwtUtil.TOKEN_PREFIX)) {
            throw new OauthException("invalid token!");
        }

        final String token = authHeader.substring(7);
        JwtUtil.parseToken(token, audience.getSecret(), audience.getIssuer());
        UserInfo userInfo = new UserInfo();
        request.setAttribute(Constants.CURRENT_USER, userInfo);
        return true;
    }
}
