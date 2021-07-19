package com.kyle.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyle.demo.Util.JwtUtil;
import com.kyle.demo.annotation.JwtIgnore;
import com.kyle.demo.auth.AuthCustomRequest;
import com.kyle.demo.config.Audience;
import com.kyle.demo.config.OauthConfig;
import com.kyle.demo.result.Result;
import com.kyle.demo.result.ResultGenerator;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;

@Controller
@RequestMapping("/api/thirdLogin")
public class ThirdController {

    @Autowired
    OauthConfig oauthConfig;

    @Autowired
    private Audience audience;

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping("/render/{source}")
    @JwtIgnore
    @ResponseBody
    public Result<String> renderAuth(@PathVariable("source") String source) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        return ResultGenerator.genSuccessResult(authorizeUrl);
    }

    @RequestMapping("/{source}/callback")
    @JwtIgnore
    public String login(@PathVariable("source") String source, AuthCallback callback, ModelMap modelMap) {
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        AuthUser authUser = response.getData();
        String token = JwtUtil.createToken("13636814471@163.com", "kyle", "admin", "kz37", audience.getSecret(), audience.getIssuer());
        modelMap.addAttribute("token", token);
        try {
            String s = objectMapper.writeValueAsString(authUser);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "/login";
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
                        .scopes(AuthScopeUtils.getScopes(AuthGithubScope.READ_USER))
                        // 针对国外平台配置代理
//                        .httpConfig(HttpConfig.builder()
//                                .timeout(15000)
//                                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("ls2.cngrok.com", 443)))
//                                .build())
                        .build());
                break;
            case "gitee":
                authRequest = new AuthGiteeRequest(AuthConfig.builder()
                        .clientId(oauthConfig.getClientId())
                        .clientSecret(oauthConfig.getClientSecret())
                        .redirectUri(oauthConfig.getRedirectUri())
//                        .scopes(AuthScopeUtils.getScopes(AuthGiteeScope.values()))
                        .scopes(AuthScopeUtils.getScopes(AuthGiteeScope.USER_INFO))
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
