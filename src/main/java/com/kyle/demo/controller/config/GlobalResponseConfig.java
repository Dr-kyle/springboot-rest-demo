package com.kyle.demo.controller.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kyle.demo.Util.JsonUtil;
import com.kyle.demo.result.Result;
import com.kyle.demo.result.ResultGenerator;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * @author kz37
 */
@RestControllerAdvice
public class GlobalResponseConfig implements ResponseBodyAdvice<Object> {

    /**
     * 判定哪些请求要执行beforeBodyWrite，返回true执行，返回false不执行
     * */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //获取当前处理请求的controller的方法
        //String methodName = methodParameter.getMethod().getName();
        // 拦/不拦截处理返回值的方法，如登录
        //String method = "login";
        //这里可以加入很多判定，如果在白名单的List里面，是否拦截
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (null == o) {
            if (mediaType == MediaType.APPLICATION_JSON) {
                return ResultGenerator.genSuccessResult();
            } else {
                return null;
            }
        } else {
            if (o instanceof Resource) {
                return o;
            } else if (o instanceof String) {
                Result<Object> result = ResultGenerator.genSuccessResult(o);
                try {
                    return JsonUtil.objectMapper.writeValueAsString(result);
                } catch (JsonProcessingException e) {
                    return o;
                }
            } else {
                if (o instanceof Result) {
                    return o;
                } else {
                    return ResultGenerator.genSuccessResult(o);
                }
            }
        }
    }
}
