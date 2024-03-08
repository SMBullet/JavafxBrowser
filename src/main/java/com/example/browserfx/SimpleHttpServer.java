package com.example.browserfx;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

public class SimpleHttpServer {

    public static void main(String[] args) throws IOException {
        int port = 8081; // You can change this port if needed
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor

        System.out.println("Server is running on http://127.0.0.1:" + port);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            String uri = exchange.getRequestURI().toString();

            // Handle the case when accessing http://127.0.0.1/
            if (uri.equals("/") || uri.equals("/index.html")) {
                serveFile(exchange, "src/htdocs/index.html");
            } else {
                // Handle other requests
                serveFile(exchange, "src/htdocs" + uri);
            }
        }

        private void serveFile(HttpExchange exchange, String filePath) throws IOException {
            File file = new File(filePath);

            if (file.exists() && !file.isDirectory()) {
                byte[] fileContent = Files.readAllBytes(file.toPath());

                exchange.sendResponseHeaders(200, fileContent.length);
                OutputStream os = exchange.getResponseBody();
                os.write(fileContent);
                os.close();
            } else {
                String response = "File not found";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}


