package com.smartcampus.api;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {

    public static final String BASE_URI = "http://localhost:8080/";

    public static void main(String[] args) {
        ResourceConfig config = new AppConfig();
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

        System.out.println("Smart Campus API running at " + BASE_URI);
        System.out.println("Press Ctrl+C to stop...");

        try {
            Thread.currentThread().join(); // keep server running
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
