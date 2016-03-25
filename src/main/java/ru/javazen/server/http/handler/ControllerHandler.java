package ru.javazen.server.http.handler;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import ru.javazen.server.http.action.result.View;
import ru.javazen.server.http.annotation.Action;
import ru.javazen.server.http.annotation.Controller;
import ru.javazen.server.http.annotation.RequestParam;
import ru.javazen.server.http.transport.Request;
import ru.javazen.server.http.transport.Response;
import ru.javazen.server.http.util.ClassUtil;
import ru.javazen.server.http.util.RequestParser;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerHandler extends HttpHandler {

    private String actionsPackage = "ru.javazen.server.http.controller";
    private List<Class> classes;
    public ControllerHandler() {
        try {
            classes = ClassUtil.getClasses(actionsPackage);
            classes = ClassUtil.filterByAnnotation(classes, Controller.class);
        } catch (ClassNotFoundException | IOException ex) {
            throw new RuntimeException("Cannot load controller classes", ex);
        }

    }
    @Override
    public Response handle(Request request) {

        String path = request.getPath();
        if(path.indexOf('?') > 0) {
           int i = path.indexOf('?');
            path = path.substring(0, i);
        }


        for (Class clazz : classes) {
            Controller controller = (Controller)clazz.getAnnotation(Controller.class);

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Action.class)) {
                    Action action = method.getAnnotation(Action.class);
                    String actionPath = normalizePath(controller.path() + "/" + action.path());
                    if (actionPath.equalsIgnoreCase(normalizePath(path)) &&
                            request.getMethod().equalsIgnoreCase(action.method())) {

                        Map<String, String> params;
                        if("GET".equalsIgnoreCase(request.getMethod())) {
                            params = RequestParser.getQueryParams(request);
                        } else {
                            params = RequestParser.getFormParams(request);
                        }

                        try {
                            List<Object> paramsForMethod = new ArrayList<>();
                            for (Parameter parameter : method.getParameters()) {
                                if(parameter.isAnnotationPresent(RequestParam.class)) {
                                    RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                                    if(params.containsKey(requestParam.value())) {
                                        paramsForMethod.add(params.get(requestParam.value()));
                                        continue;
                                    }
                                }
                                paramsForMethod.add(null);

                            }
                            Object result = method.invoke(clazz.newInstance(), paramsForMethod.toArray());

                            if (result instanceof View) {
                                View view = (View) result;
                                VelocityEngine velocityEngine = new VelocityEngine();
                                velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
                                velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
                                velocityEngine.init();

                                Template template = velocityEngine.getTemplate("/views/" + view.getPath());

                                VelocityContext velocityContext = new VelocityContext();
                                for(String key : view.getModel().keySet()) {
                                    velocityContext.put(key, view.getModel().get(key));
                                }
                                Writer writer = new StringWriter();
                                template.merge(velocityContext, writer);

                                String viewResult = writer.toString();
                                Response response = new Response();

                                response.setProtocolVersion("HTTP/1.0");
                                response.setStatusCode(200);
                                response.setReasonPhrase("OK");
                                Map<String, String> respHeaders = new HashMap<>();
                                respHeaders.put("Server", "JavaZen/0.1");
                                respHeaders.put("Content-Type", "text/html");
                                response.setHeaders(respHeaders);
                                response.setBody(viewResult);

                                return response;
                            }

                        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
        return null;
    }

    private String normalizePath(String path) {
        return ("/" + path + "/").replaceAll("\\/+", "/");
    }
}
