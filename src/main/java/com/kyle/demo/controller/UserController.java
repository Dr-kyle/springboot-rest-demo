package com.kyle.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.kyle.demo.annotation.CurrentUser;
import com.kyle.demo.annotation.JwtIgnore;
import com.kyle.demo.entity.UserInfo;
import com.kyle.demo.result.Result;
import com.kyle.demo.result.ResultGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @CurrentUser
    @GetMapping("info")
    public Result<JSONObject> getTest(UserInfo userInfo) {
        System.out.println(JSONObject.toJSONString(userInfo));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","kyle zhao");
        jsonObject.put("avatar", "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png");
        jsonObject.put("role", "admin");
        return ResultGenerator.genSuccessResult(jsonObject);
    }

    @GetMapping("info1")
    // 不行，获取不到 userInfo
    public Result<JSONObject> getTest1(UserInfo userInfo) {
        System.out.println(JSONObject.toJSONString(userInfo));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","kyle zhao");
        jsonObject.put("avatar", "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png");
        jsonObject.put("role", "admin");
        return ResultGenerator.genSuccessResult(jsonObject);
    }

    @GetMapping("info2")
    public Result<JSONObject> getTest3(@CurrentUser UserInfo userInfo) {
        System.out.println(JSONObject.toJSONString(userInfo));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","kyle zhao");
        jsonObject.put("avatar", "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png");
        jsonObject.put("role", "admin");
        return ResultGenerator.genSuccessResult(jsonObject);
    }

    @GetMapping("logout")
    @JwtIgnore
    public Result<JSONObject> logout() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status","ok");
        return ResultGenerator.genSuccessResult(jsonObject);
    }
}
