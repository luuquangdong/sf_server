package com.it5240.sportfriendfinding.utils;

import java.util.Map;

public class RespHelper {

    public static final Map<String, Object> ok(){
        return Map.of("status", "ok");
    }
    public static final Map<String, Object> ok(String key, Object value){
        return Map.of(key, value);
    }
}
