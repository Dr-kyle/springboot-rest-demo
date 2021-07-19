package com.kyle.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.kyle.demo.annotation.CurrentUser;
import com.kyle.demo.entity.UserInfo;
import com.kyle.demo.result.Result;
import com.kyle.demo.result.ResultGenerator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @CurrentUser
    @RequestMapping("/test1/{id}")
    public Result<JSONObject> test1(UserInfo userInfo, @PathVariable String id) {
        System.out.println(JSONObject.toJSONString(userInfo) + id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","kyle zhao");
        jsonObject.put("avatar", "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png");
        jsonObject.put("role", "admin");
        return ResultGenerator.genSuccessResult(jsonObject);
    }

    @RequestMapping("/test2/{id}")
    public Result<JSONObject> test2(@CurrentUser UserInfo userInfo, @PathVariable String id) {
        System.out.println(JSONObject.toJSONString(userInfo) + id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","kyle zhao");
        jsonObject.put("avatar", "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png");
        jsonObject.put("role", "admin");
        return ResultGenerator.genSuccessResult(jsonObject);
    }
}
