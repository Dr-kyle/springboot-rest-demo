package com.kyle.demo.Util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author kz37
 */
public final class JsonUtil {
    private JsonUtil() {}

    public static final ObjectMapper objectMapperCustom = new ObjectMapper();
}
