package com.kyle.demo.auth;

import me.zhyd.oauth.config.AuthSource;

/**
 * @author kz37
 */
public enum AuthCustomSource implements AuthSource {
    /**
     * 自定义oauth平台
     */
    MYAUTH {
        @Override
        public String authorize() {
            return "https://nas.newegg.org/oauth2/v1/authorize";
        }

        @Override
        public String accessToken() {
            return "https://nas.newegg.org/oauth2/v1/token";
        }

        @Override
        public String userInfo() {
            return "https://nas.newegg.org/oauth2/v1/user-info";
        }
    }
}
