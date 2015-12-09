package com.mindera.telemetron.client;

/**
 * General TelemetronClient exception.
 */
public class TelemetronClientException extends RuntimeException {

    TelemetronClientException(String msg) {
        super(msg);
    }

    TelemetronClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
