package ru.javazen.server.http;

import ru.javazen.server.http.util.ServerConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        ServerConfig serverConfig = ServerConfig.getInstance();

        ServerListener server = new ServerListener(serverConfig.getPort(), InetAddress.getByName("localhost"));
        server.start(serverConfig.getHttpHandlers());

        //TimeUnit.MICROSECONDS.sleep(10000);
    }
}
