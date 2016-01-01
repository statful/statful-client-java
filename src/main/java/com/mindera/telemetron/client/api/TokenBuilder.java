package com.mindera.telemetron.client.api;

class TokenBuilder {
    private final String token;

    TokenBuilder(String token) {
        this.token = token;
    }

    String getToken() {
        return token;
    }
}
