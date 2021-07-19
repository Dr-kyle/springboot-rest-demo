package com.kyle.demo;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
//        Integer i1 = 127;
//        Integer i2 = 127;
//        System.out.println(i1 == i2);
//        Number a = new Double(2);
//        Number b = 2;
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


        // System.out.println(testFinally());

        // 动态代理
//        InvocationHandler handler = (proxy, method, args1) -> {
//            if (method.getName().equals("morning")) {
//                System.out.println("Good morning, " + args1[0]);
//            }
//            return "zhao";
//        };
//
//        Hello hello = (Hello) Proxy.newProxyInstance(Hello.class.getClassLoader(), new Class[]{Hello.class}, handler);
//        String res = hello.morning("Kyle");
//        System.out.println(res);
//
//        HashMap<String, String> map = new HashMap<>();
//        System.out.println(map.put("null", "2"));
//        List<String> a;
//        for(Map.Entry<String, String> entry : map.entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }
//
//        System.out.println(map.get(null));

//        TreeMap<String, String> map = new TreeMap<String, String>();
//        map.put("b", "b");
//        map.put("a", "a");
//        for (String s : map.keySet()) {
//            System.out.println(s);
//        }

//        LinkedList<String> ls = new LinkedList<>()
//        Deque<Integer> deque = new LinkedList<>();
//        deque.push(1);
//        PriorityQueue<String> queue = new PriorityQueue<>(5);
//        queue.add("4");
//        queue.offer("55");

//        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
//        ZonedDateTime zdt2 = zdt.withZoneSameInstant(ZoneId.of("Asia/Shanghai"));
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        System.out.println(dtf.format(zdt));
//        System.out.println(dtf.format(zdt2));
//        System.out.println(ZoneId.systemDefault());

//        List<String> s = Arrays.asList("1");
//        System.out.println("main start");
//        Thread t = new Thread(() -> {
//            try {
//                Thread.sleep(2000);
//                System.out.println("thread end");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        t.setDaemon(true);
//        t.start();
//        System.out.println("main end");
        HashMap<String, String> map = new HashMap<>(7);
        map.put("b", "b");
        map.put("a", "a");
        map.put("c", "b");
        map.put("d", "a");
        map.put("e", "b");
        map.put("f", "a");
        map.put("j", "b");
        map.put("h", "a");

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
interface Hello {
    String morning(String name);
}
