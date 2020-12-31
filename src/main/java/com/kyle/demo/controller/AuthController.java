package com.kyle.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kyle.demo.annotation.JwtIgnore;
import com.kyle.demo.auth.AuthCustomRequest;
import com.kyle.demo.config.OauthConfig;
import com.xkcoding.http.config.HttpConfig;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.scope.*;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthScopeUtils;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;

/**
 * @author kz37
 */
@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OauthConfig oauthConfig;

    @JwtIgnore
    @GetMapping("/get")
    public String list() {
        System.out.println(oauthConfig.toString());
        return "ss";
    }

    @JwtIgnore
    @GetMapping("/config")
    public ObjectNode config() {
        // @Configuration 返回到reponse 的时候报错，因为类注入了beanfactory,json序列化失败
        // RestController 注解就是序列化为json格式的数据，所以实体类的属性不能有其他注入的beanfactory实例类的应用
        // @Component是一个元注解，意思是可以注解其他类注解，如@Controller @Service @Repository @Aspect。
        // 官方的原话是：带此注解的类看为组件，当使用基于注解的配置和类路径扫描的时候，
        // 这些类就会被实例化。其他类级别的注解也可以被认定为是一种特殊类型的组件，比如@Repository @Aspect。所以，@Component可以注解其他类注解。
        // https://www.cnblogs.com/xinruyi/p/11160933.html
        ObjectNode node = objectMapper.createObjectNode();
        node.put("clientId", oauthConfig.getClientId());
        node.put("clientSecret", oauthConfig.getClientSecret());
        node.put("redirectUri", oauthConfig.getRedirectUri());
        return node;
    }

    @JwtIgnore
    @RequestMapping("/render/{source}")
    @ResponseBody
    public String renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        return authorizeUrl;
        //response.sendRedirect(authorizeUrl);
    }

    /**
     * oauth平台中配置的授权回调地址，以本项目为例，在创建github授权应用时的回调地址应为：http://127.0.0.1:8443/oauth/callback/github
     */
    @JwtIgnore
    @RequestMapping("/callback/{source}")
    public AuthUser login(@PathVariable("source") String source, AuthCallback callback, HttpServletRequest request) {
        // log.info("进入callback：" + source + " callback params：" + JSONObject.toJSONString(callback));
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        AuthUser authUser = response.getData();
        try {
            String s = objectMapper.writeValueAsString(authUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return authUser;
        // log.info(JSONObject.toJSONString(response));

//        if (response.ok()) {
//            userService.save(response.getData());
//            return new ModelAndView("redirect:/users");
//        }
//        Map<String, Object> map = new HashMap<>(1);
//        map.put("errorMsg", response.getMsg());
//        return new ModelAndView("error", map);
    }


    /**
     * 根据具体的授权来源，获取授权请求工具类
     *
     * @param source
     * @return
     */
    private AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source.toLowerCase()) {
            case "dingtalk":
                authRequest = new AuthDingTalkRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "baidu":
                authRequest = new AuthBaiduRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .scopes(Arrays.asList(
                                AuthBaiduScope.BASIC.getScope(),
                                AuthBaiduScope.SUPER_MSG.getScope(),
                                AuthBaiduScope.NETDISK.getScope()
                        ))
//                        .clientId("")
//                        .clientSecret("")
//                        .redirectUri("http://localhost:9001/oauth/baidu/callback")
                        .build());
                break;
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .scopes(AuthScopeUtils.getScopes(AuthGithubScope.values()))
                        // 针对国外平台配置代理
//                        .httpConfig(HttpConfig.builder()
//                                .timeout(15000)
//                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
//                                .build())
                        .build());
                break;
            case "gitee":
                authRequest = new AuthGiteeRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .scopes(AuthScopeUtils.getScopes(AuthGiteeScope.values()))
                        .build());
                break;
            case "weibo":
                authRequest = new AuthWeiboRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .scopes(Arrays.asList(
                                AuthWeiboScope.EMAIL.getScope(),
                                AuthWeiboScope.FRIENDSHIPS_GROUPS_READ.getScope(),
                                AuthWeiboScope.STATUSES_TO_ME_READ.getScope()
                        ))
                        .build());
                break;
            case "coding":
                authRequest = new AuthCodingRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .codingGroupName("")
                        .scopes(Arrays.asList(
                                AuthCodingScope.USER.getScope(),
                                AuthCodingScope.USER_EMAIL.getScope(),
                                AuthCodingScope.USER_PHONE.getScope()
                        ))
                        .build());
                break;
            case "oschina":
                authRequest = new AuthOschinaRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "alipay":
                // 支付宝在创建回调地址时，不允许使用localhost或者127.0.0.1，所以这儿的回调地址使用的局域网内的ip
                authRequest = new AuthAlipayRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "qq":
                authRequest = new AuthQqRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "wechat_open":
                authRequest = new AuthWeChatOpenRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "taobao":
                authRequest = new AuthTaobaoRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "google":
                authRequest = new AuthGoogleRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .scopes(AuthScopeUtils.getScopes(AuthGoogleScope.USER_EMAIL, AuthGoogleScope.USER_PROFILE, AuthGoogleScope.USER_OPENID))
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                .timeout(15000)
                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                .build())
                        .build());
                break;
            case "facebook":
                authRequest = new AuthFacebookRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .scopes(AuthScopeUtils.getScopes(AuthFacebookScope.values()))
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                .timeout(15000)
                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                .build())
                        .build());
                break;
            case "douyin":
                authRequest = new AuthDouyinRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "linkedin":
                authRequest = new AuthLinkedinRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .scopes(null)
                        .build());
                break;
            case "microsoft":
                authRequest = new AuthMicrosoftRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .scopes(Arrays.asList(
                                AuthMicrosoftScope.USER_READ.getScope(),
                                AuthMicrosoftScope.USER_READWRITE.getScope(),
                                AuthMicrosoftScope.USER_READBASIC_ALL.getScope(),
                                AuthMicrosoftScope.USER_READ_ALL.getScope(),
                                AuthMicrosoftScope.USER_READWRITE_ALL.getScope(),
                                AuthMicrosoftScope.USER_INVITE_ALL.getScope(),
                                AuthMicrosoftScope.USER_EXPORT_ALL.getScope(),
                                AuthMicrosoftScope.USER_MANAGEIDENTITIES_ALL.getScope(),
                                AuthMicrosoftScope.FILES_READ.getScope()
                        ))
                        .build());
                break;
            case "mi":
                authRequest = new AuthMiRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "toutiao":
                authRequest = new AuthToutiaoRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "teambition":
                authRequest = new AuthTeambitionRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "pinterest":
                authRequest = new AuthPinterestRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                .timeout(15000)
                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                .build())
                        .build());
                break;
            case "renren":
                authRequest = new AuthRenrenRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "stack_overflow":
                authRequest = new AuthStackOverflowRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .stackOverflowKey("")
                        .build());
                break;
            case "huawei":
                authRequest = new AuthHuaweiRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .scopes(Arrays.asList(
                                AuthHuaweiScope.BASE_PROFILE.getScope(),
                                AuthHuaweiScope.MOBILE_NUMBER.getScope(),
                                AuthHuaweiScope.ACCOUNTLIST.getScope(),
                                AuthHuaweiScope.SCOPE_DRIVE_FILE.getScope(),
                                AuthHuaweiScope.SCOPE_DRIVE_APPDATA.getScope()
                        ))
                        .build());
                break;
            case "wechat_enterprise":
                authRequest = new AuthWeChatEnterpriseRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .agentId("1000003")
                        .build());
                break;
            case "kujiale":
                authRequest = new AuthKujialeRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "gitlab":
                authRequest = new AuthGitlabRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .scopes(AuthScopeUtils.getScopes(AuthGitlabScope.values()))
                        .build());
                break;
            case "meituan":
                authRequest = new AuthMeituanRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "eleme":
                authRequest = new AuthElemeRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "myauth":
                authRequest = new AuthCustomRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "twitter":
                authRequest = new AuthTwitterRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        // 针对国外平台配置代理
                        .httpConfig(HttpConfig.builder()
                                .timeout(15000)
                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10080)))
                                .build())
                        .build());
                break;
            case "wechat_mp":
                authRequest = new AuthWeChatMpRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            case "aliyun":
                authRequest = new AuthAliyunRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
                        .build());
                break;
            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }
}
