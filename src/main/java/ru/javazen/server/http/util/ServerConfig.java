package ru.javazen.server.http.util;

import ru.javazen.server.http.handler.ControllerHandler;
import ru.javazen.server.http.handler.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ServerConfig {

    private static ServerConfig instance = new ServerConfig();

    private static final String PORT_PROPERTY = "server.port";
    private static final String CONTROLLER_PACKAGE_PROPERTY = "server.controller_package";

    private Properties properties;

    private List<HttpHandler> handlers;

    public static ServerConfig getInstance() {
        return instance;
    }

    private ServerConfig() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load server properties", e);
        }

        handlers = new ArrayList<>();
        handlers.add(new ControllerHandler());
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty(PORT_PROPERTY));
    }

    public String getControllerPackage() {
        return properties.getProperty(CONTROLLER_PACKAGE_PROPERTY);
    }

    public List<HttpHandler> getHttpHandlers() {
        return handlers;
    }
}
