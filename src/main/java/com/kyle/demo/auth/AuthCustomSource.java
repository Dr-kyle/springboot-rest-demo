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
            return "";
        }

        @Override
        public String accessToken() {
            return "";
        }

        @Override
        public String userInfo() {
            return "";
        }
    }
}
