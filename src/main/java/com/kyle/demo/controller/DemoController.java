package com.kyle.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.kyle.demo.exception.ServiceException;
import com.kyle.demo.result.Result;
import com.kyle.demo.result.ResultGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kz37
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    @GetMapping("/get")
    public Result<JSONObject> getTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","kyle");
        jsonObject.put("age", 7);

        return ResultGenerator.genSuccessResult(jsonObject);
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

    @GetMapping("/error")
    public Result<JSONObject> getError1(@RequestParam Integer id) {
        if (null == id || id < 2) {
            throw new ServiceException("test error");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","kyle");
        jsonObject.put("age", 78);
        return ResultGenerator.genSuccessResult(jsonObject);
    }
}
