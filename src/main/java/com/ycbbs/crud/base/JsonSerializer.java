package com.ycbbs.crud.base;


import java.lang.reflect.Type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import cn.hutool.core.util.CharsetUtil;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.cn;

/**
 * @Title: JsonSerializer.java
 * @Package com.tisson.crawler.common.cache
 * @author Joker1995
 * @date 2019年1月7日
 * @version V1.0
 */
public class JsonSerializer {
    public static final String UTF_8 = "UTF-8";
    private static Gson gson = new Gson();
//    ObjectMapper fromJson = new ObjectMapper();

    /**
     * @param obj
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) {
        return gson.toJson(obj).getBytes(CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * @param data
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] data,Type type) {
        return gson.fromJson(new String(data), type);
    }
}

