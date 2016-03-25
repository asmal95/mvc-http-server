package ru.javazen.server.http.transport;

import java.util.Map;

public class Response {

    private String protocolVersion;
    private int statusCode;
    private String reasonPhrase;
    private Map<String, String> headers;
    private String body;

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
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
        builder.append(getProtocolVersion());
        builder.append(" ");
        builder.append(getStatusCode());
        builder.append(" ");
        builder.append(getReasonPhrase());
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
