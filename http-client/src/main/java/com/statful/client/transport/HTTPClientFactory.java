package com.statful.client.transport;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * Interface for factories to implement HTTP clients and components.
 */
public interface HTTPClientFactory {

    /**
     * Creates a new HTTP client to communicate with Statful.
     *
     * @return A closeable HTTP client
     * @throws GeneralSecurityException Thrown when SSL specific configurations fail
     */
    CloseableHttpClient createHttpClient() throws GeneralSecurityException;

    /**
     * Creates a new {@link HttpPut} object to be used to send PUT requests to the specified URI.
     *
     * @param uri The URI as a string
     * @param body The message body as a string to send to Staful
     * @return A newly created {@link HttpPut} object
     * @throws UnsupportedEncodingException Thrown when the encoding isn't supported
     */
    HttpPut createHttpPut(final String uri, final String body) throws UnsupportedEncodingException;
}
