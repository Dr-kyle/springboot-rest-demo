package com.kyle.demo.auth;

import com.alibaba.fastjson.JSONObject;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;

/**
 * @author kz37
 */
public class AuthCustomRequest extends AuthDefaultRequest {
    public AuthCustomRequest(AuthConfig config) {
        super(config, AuthCustomSource.MYAUTH);
    }

    public AuthCustomRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthCustomSource.MYAUTH, authStateCache);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String response = this.doPostAuthorizationCode(authCallback.getCode());
        JSONObject res = JSONObject.parseObject(response);
        this.checkResponse(res.containsKey("error"), (String)res.get("error_description"));
        return AuthToken.builder()
                .accessToken(res.getString("access_token"))
                .scope(res.getString("scope"))
                .tokenType(res.getString("token_type"))
                .expireIn(res.getIntValue("expires_in"))
                .refreshToken(res.getString("refresh_token"))
                .build();
    }

    @Override
    protected AuthUser getUserInfo(AuthToken authToken) {
        String response = this.doGetUserInfo(authToken);
        JSONObject object = JSONObject.parseObject(response);
        this.checkResponse(object.containsKey("error"), object.getString("error_description"));
        return AuthUser.builder()
                .uuid(object.getString("sub"))
                .username(object.getString("name"))
                .avatar(object.getString("picture"))
                .nickname(object.getString("nickname"))
                .email(object.getString("email"))
                .remark(object.toString())
                .token(authToken)
                .rawUserInfo(object)
                .source(this.source.toString())
                .build();
    }

    private void checkResponse(boolean error, String errorDescription) {
        if (error) {
            throw new AuthException(errorDescription);
        }
    }
}
