package com.kyle.demo.config.resolver;

import com.alibaba.fastjson.JSONObject;
import com.kyle.demo.annotation.CurrentUser;
import com.kyle.demo.constant.Constants;
import com.kyle.demo.entity.UserInfo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.lang.reflect.Method;

/**
 * @author kz37
 */
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {

        final Method method = methodParameter.getMethod();
        final Class<?> clazz = methodParameter.getMethod().getDeclaringClass();
        boolean hasParameterAnnotation = methodParameter.hasParameterAnnotation(CurrentUser.class);
        // isAnnotationPresent Returns true if an annotation for the specified type is present on this element
        boolean isHasLoginAuth = clazz.isAnnotationPresent(CurrentUser.class) || method.isAnnotationPresent(CurrentUser.class) || hasParameterAnnotation;
        // isAssignableFrom
        boolean isHasLoginUserParameter = methodParameter.getParameterType().isAssignableFrom(UserInfo.class);

        return isHasLoginAuth && isHasLoginUserParameter;


//        boolean result = methodParameter.getParameterType().isAssignableFrom(UserInfo.class)
//                && methodParameter.hasParameterAnnotation(CurrentUser.class);
//        return result;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        UserInfo userInfo = (UserInfo) nativeWebRequest.getAttribute(Constants.CURRENT_USER, RequestAttributes.SCOPE_REQUEST);
        if (userInfo != null) {
            return userInfo;
        }
        throw new MissingServletRequestPartException(Constants.CURRENT_USER);
    }
}
