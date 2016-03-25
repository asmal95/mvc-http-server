package ru.javazen.server.http;

import ru.javazen.server.http.handler.HttpHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerListener {

    private ServerSocket serverSocket;
    private Thread serverThread;
    private List<RequestHandler> handlers;

    public ServerListener(int port, InetAddress address) throws IOException {
        serverSocket = new ServerSocket(port, 0, address);
        handlers = new ArrayList<>();
    }

    public void start(List<HttpHandler> httpHandlers) {
        serverThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        final RequestHandler handler = new RequestHandler(clientSocket, httpHandlers);
                        handlers.add(handler);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                handler.handle();
                                handlers.remove(handler);
                            }

                        }).start();
                    } catch (IOException ex) {
                        //LOG.error(ex);
                    }
                }
            }
        });
        //serverThread.setDaemon(true);
        serverThread.start();
    }

    public void stop() {
        serverThread.interrupt();
    }
}
