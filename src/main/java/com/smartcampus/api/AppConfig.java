package com.smartcampus.api;

import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        packages("com.smartcampus.api.resources");
    }
}