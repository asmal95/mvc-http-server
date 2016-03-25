package ru.javazen.server.http.controller;

import ru.javazen.server.http.action.result.ActionResult;
import ru.javazen.server.http.annotation.Action;
import ru.javazen.server.http.annotation.Controller;
import ru.javazen.server.http.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller(path = "/")
public class IndexController extends AbstractController {

    @Action(path = "/", method = "GET")
    public ActionResult indexGet(@RequestParam("name") String name) {

        Map<String, Object> model = new HashMap<>();

        model.put("name", name);
        return view("index.vm", model);
    }

    @Action(path = "/", method = "POST")
    public ActionResult indexPost(@RequestParam("name") String name) {

        Map<String, Object> model = new HashMap<>();

        model.put("name", name);
        return view("index.vm", model);
    }
}
