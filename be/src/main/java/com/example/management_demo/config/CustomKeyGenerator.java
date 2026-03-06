package com.example.management_demo.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component("customKeyGenerator")
public class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String className = target.getClass().getSimpleName();
        String methodName = method.getName();
        String paramString = Arrays.stream(params)
                .map(p -> p == null ? "null" : p.toString())
                .collect(Collectors.joining("_"));

        return className + ":" + methodName + (paramString.isEmpty() ? "" : ":" + paramString);
    }
}
