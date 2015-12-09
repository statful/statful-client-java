package com.mindera.telemetron.client;

/**
 * Exception to handle configuration errors.
 */
public class TelemetronConfigException extends TelemetronClientException {

    TelemetronConfigException(String msg) {
        super(msg);
    }

}
