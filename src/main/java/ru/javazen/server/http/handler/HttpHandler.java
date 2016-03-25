package ru.javazen.server.http.handler;

import ru.javazen.server.http.transport.Request;
import ru.javazen.server.http.transport.Response;

public abstract class HttpHandler {

    public abstract Response handle(Request request);

}
