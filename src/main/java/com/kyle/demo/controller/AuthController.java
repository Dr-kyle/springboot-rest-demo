package com.kyle.demo.controller;

import com.kyle.demo.config.AuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kz37
 */
@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    AuthConfig authConfig;

    @GetMapping("/get")
    public String list() {
        System.out.println(authConfig.toString());
        return "ss";
    }

    @GetMapping("/config")
    public AuthConfig config() {
        // @Configuration 返回到reponse 的时候报错，因为类注入了beanfactory,json序列化失败
        // RestController 注解就是序列化为json格式的数据，所以实体类的属性不能有其他注入的beanfactory实例类的应用
        // @Component是一个元注解，意思是可以注解其他类注解，如@Controller @Service @Repository @Aspect。
        // 官方的原话是：带此注解的类看为组件，当使用基于注解的配置和类路径扫描的时候，
        // 这些类就会被实例化。其他类级别的注解也可以被认定为是一种特殊类型的组件，比如@Repository @Aspect。所以，@Component可以注解其他类注解。
        // https://www.cnblogs.com/xinruyi/p/11160933.html
        return authConfig;
    }
}
