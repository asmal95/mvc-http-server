package ru.javazen.server.http;


import ru.javazen.server.http.handler.HttpHandler;
import ru.javazen.server.http.transport.Request;
import ru.javazen.server.http.transport.Response;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class RequestHandler {

    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String SPACE = " ";
    private List<HttpHandler> handlers;

    private Socket socket;

    public RequestHandler(Socket socket, List<HttpHandler> handlers) {
        this.socket = socket;
        this.handlers = handlers;
    }

    public void handle() {

        try {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            Request request = new Request();
            Map<String, String> headers = new HashMap<>();

            boolean isFirstLine = true;

            while ((line = input.readLine()) != null && !line.isEmpty()) {
                if (isFirstLine) {
                    StringTokenizer tokenizer = new StringTokenizer(line, SPACE);

                    request.setMethod(tokenizer.nextToken());
                    request.setPath(tokenizer.nextToken());
                    request.setProtocolVersion(tokenizer.nextToken());

                    isFirstLine = false;
                    continue;
                }

                int separator = line.indexOf(':');

                headers.put(line.substring(0, separator), line.substring(separator+2/* separator + space*/, line.length()));
            }

            int contentLength = 0;
            if (headers.containsKey(CONTENT_LENGTH) && headers.get(CONTENT_LENGTH).matches("\\d+")) {
                contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH));
            }
            request.setHeaders(headers);

            if (contentLength > 0) {
                char[] charArray = new char[contentLength];
                input.read(charArray, 0, contentLength);
                request.setBody(new String(charArray));
            }

            System.out.println(request);

            Response response = null;

            /*response.setProtocolVersion("HTTP/1.0");
            response.setStatusCode(200);
            response.setReasonPhrase("OK");
            Map<String, String> respHeaders = new HashMap<>();
            respHeaders.put("Server", "JavaZen/0.1");
            respHeaders.put("Content-Type", "text/html");
            response.setHeaders(respHeaders);
            response.setBody("<TITLE>Example</TITLE><form method='post'><input type='text' name='hi' />Hello<button>sd</button></form>");
*/
            for(HttpHandler handler : handlers) {
                response = handler.handle(request);
            }

            if (response == null) {
                response = new Response();
                response.setProtocolVersion("HTTP/1.1");
                response.setStatusCode(404);
                response.setReasonPhrase("Not Found");

                Map<String, String> respHeaders = new HashMap<>();
                respHeaders.put("Server", "JavaZen/0.1");
                respHeaders.put("Content-Type", "text/html");
                response.setHeaders(respHeaders);

                /*response.setBody("Not Found");*/
                response.setBody("<TITLE>Example</TITLE><form method='post'><input type='text' name='hi' />Hello<button>sd</button></form>");
            }

            output.write(response.toString());

            output.close();
            input.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
