package com.kyle.demo.exception;

import com.kyle.demo.result.Result;
import com.kyle.demo.result.ResultGenerator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kz37
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ServiceException.class)
    public <T> Result<T> globalExceptionHandler(HttpServletRequest request, ServiceException e) {
        return ResultGenerator.genFailResult(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public <T> Result<T> globalExceptionHandler(HttpServletRequest request, Exception e) {
        return ResultGenerator.genFailResult(e.getMessage());
    }

}
