package ru.javazen.server.http.controller;


import ru.javazen.server.http.action.result.View;

import java.util.Collections;
import java.util.Map;


public abstract class AbstractController {
    protected View view(String path, Map<String, Object> model) {
        return new View(path, model);
    }

    protected View view(String path) {
        return new View(path, Collections.EMPTY_MAP);
    }
}
