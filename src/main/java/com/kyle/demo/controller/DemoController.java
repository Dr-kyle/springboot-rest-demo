package com.kyle.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kyle.demo.exception.ServiceException;
import com.kyle.demo.result.Result;
import com.kyle.demo.result.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kz37
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/get")
    public Result<JSONObject> getTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","kyle");
        jsonObject.put("age", 7);

        return ResultGenerator.genSuccessResult(jsonObject);
    }

    @PostMapping("/team")
    public Result<Object> receiveTeams(@RequestBody String body) {
        System.out.println(body);
        return ResultGenerator.genSuccessResult();
    }
    @GetMapping("/error/{id}")
    public Result<JSONObject> getError(@PathVariable Integer id) {
        if (null == id || id < 2) {
            throw new ServiceException("test error");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","kyle");
        jsonObject.put("age", 77);
        return ResultGenerator.genSuccessResult(jsonObject);
    }

    @GetMapping("/error/**")
    public Result<JSONObject> other(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String urlTail = new AntPathMatcher()
                .extractPathWithinPattern( "/error/**", request.getRequestURI());
        jsonObject.put("url", request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
        jsonObject.put("urlTail", urlTail);
        jsonObject.put("getRequestURI", request.getRequestURI());
        jsonObject.put("name","kyle *");
        jsonObject.put("age", 77);
        return ResultGenerator.genSuccessResult(jsonObject);
    }

    /**
     *  * 不配 / ， ** 会匹配 /
     * @param request
     * @return
     */
    @GetMapping("/test/*")
    public Result<JSONObject> test(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String urlTail = new AntPathMatcher()
                .extractPathWithinPattern( "/error/**", request.getRequestURI());
        jsonObject.put("url", request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
        jsonObject.put("urlTail", urlTail);
        jsonObject.put("getRequestURI", request.getRequestURI());
        jsonObject.put("name","kyle * test");
        jsonObject.put("age", 77);
        return ResultGenerator.genSuccessResult(jsonObject);
    }


    @GetMapping("/error")
    public Result<JSONObject> getError1() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","kyle");
        jsonObject.put("age", 78);
        return ResultGenerator.genSuccessResult(jsonObject);
    }

    @GetMapping("/config")
    public String ss() {
        return "name";
    }

    @GetMapping("/e")
    public String e() {
        throw new ServiceException("chu cuole");
    }

    @GetMapping("/config1")
    public JSONObject ss1() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","kyle");
        jsonObject.put("age", 78);
        return jsonObject;
    }

    @GetMapping("/json")
    public ObjectNode s() {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("a","b");
        System.out.println(node.getNodeType());
        System.out.println(node.get("a").getNodeType());
        return node;
    }
}
