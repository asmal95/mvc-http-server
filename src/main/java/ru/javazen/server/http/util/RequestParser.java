package ru.javazen.server.http.util;

import ru.javazen.server.http.transport.Request;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class RequestParser {

    public static Map<String, String> getFormParams(Request request) {
        Map<String, String> result = Collections.EMPTY_MAP;
        if ("application/x-www-form-urlencoded".equals(request.getHeaders().get("Content-Type")) && request.getBody() != null) {
            result = parse(request.getBody());
        }
        return result;
    }

    public static Map<String, String> getQueryParams(Request request) {
        /*String params = request.getPath().substring(request.getPath().indexOf("?"), request.getPath().length());
        return parse(params);*/
        Map<String, String> result = Collections.EMPTY_MAP;
        if (request.getPath().indexOf("?") > 0) {
            int i = request.getPath().indexOf("?");
            String params = request.getPath().substring(i + 1, request.getPath().length());
            result = parse(params);
        }
        return result;
    }

    private static Map<String, String> parse(String params) {
        Map<String, String> result = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(params, "&");
        while (tokenizer.hasMoreTokens()) {
            String couple = tokenizer.nextToken();
            String key = couple.substring(0, couple.indexOf("="));
            String value = couple.substring(couple.indexOf("=") + 1, couple.length());
            result.put(key, value);
        }
        return result;
    }
}
