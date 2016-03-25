package ru.javazen.server.http.transport;

import java.util.Map;

public class Request {

    private String method;
    private String path;
    private String protocolVersion;
    private Map<String, String> headers;
    private String body;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getMethod());
        builder.append(" ");
        builder.append(getPath());
        builder.append(" ");
        builder.append(getProtocolVersion());
        builder.append("\n");
        for (String header : headers.keySet()) {
            builder.append(header);
            builder.append(": ");
            builder.append(headers.get(header));
            builder.append("\n");
        }
        builder.append("\r\n");
        if (getBody() != null) {
            builder.append(getBody());
        }
        return builder.toString();
    }

    //TODO Builder class
}
