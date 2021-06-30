package com.kyle.demo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.Feature;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        //language=JSON
//        String s = "{\"name.a\": \"kyle\",\"age\": 2, \"text\": {\"name\": \"kyle2\"" +
//                "}}";
//        String s1 = new String("127");
//        String s2 = new String("127");
//        String s3 = "129";
//        String s4 = "129";
//        System.out.println(Integer.parseInt(s1) == Integer.parseInt(s2));
//        System.out.println(Integer.valueOf(s1) == Integer.valueOf(s2));
//        System.out.println(Integer.parseInt(s3) == Integer.parseInt(s4));
//        System.out.println(Integer.valueOf(s3) == Integer.valueOf(s4));
//        Integer i1 = 129;
//        Integer i2 = 129;
//        System.out.println(i1 == i2);
//        JSONObject jsonObject = new JSONObject();
//        JSONObject jsonObject1 = new JSONObject();
//        jsonObject1.put("name","kyle1");
//        jsonObject1.put("age", 2);
//
//        jsonObject.put("name","kyle");
//        jsonObject.put("age", 7);
//        jsonObject.put("text", jsonObject1);
//        System.out.println(JSONPath.contains(jsonObject, "$.text.age"));
//        System.out.println(JSONPath.contains(s, "$.text.name"));
//        System.out.println(JSONPath.contains(s, "$.name.a"));
        // System.out.println(Arrays.copyOfRange("kyle".toCharArray(), 1, 2));


        System.out.println(testFinally());

    }

    private static int testFinally() {
        try {
            return Integer.parseInt("1");
        } catch (Exception e) {
            e.printStackTrace();
            return 4;
        } finally {
            System.out.println("2");
            return 3;
        }
    }
}
