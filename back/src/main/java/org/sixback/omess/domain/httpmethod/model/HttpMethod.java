package org.sixback.omess.domain.httpmethod.model;

import java.util.ArrayList;
import java.util.List;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    HEAD("HEAD"),
    TRACE("TRACE");

    private final String methodName;
    private static final List<String> METHOD_NAMES = new ArrayList<>();

    static{
        for (HttpMethod httpMethod : HttpMethod.values()) {
            METHOD_NAMES.add(httpMethod.methodName);
        }
    }

    HttpMethod(String methodName){
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public static List<String> getMethodNames(){
        return METHOD_NAMES;
    }
}
