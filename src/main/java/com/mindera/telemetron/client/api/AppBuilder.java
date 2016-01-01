package com.mindera.telemetron.client.api;

class AppBuilder {
    private final String app;

    AppBuilder(String app) {
        this.app = app;
    }

    String getApp() {
        return app;
    }
}
