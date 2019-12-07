package com.kyle.demo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.Feature;

public class Test {
    public static void main(String[] args) {
        //language=JSON
        String s = "{\"name.a\": \"kyle\",\"age\": 2, \"text\": {\"name\": \"kyle2\"" +
                "}}";
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name","kyle1");
        jsonObject1.put("age", 2);

        jsonObject.put("name","kyle");
        jsonObject.put("age", 7);
        jsonObject.put("text", jsonObject1);
        System.out.println(JSONPath.contains(jsonObject, "$.text.age"));
        System.out.println(JSONPath.contains(s, "$.text.name"));
        System.out.println(JSONPath.contains(s, "$.name.a"));

    }
}
