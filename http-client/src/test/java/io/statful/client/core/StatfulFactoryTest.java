package io.statful.client.core;

import io.statful.client.domain.api.StatfulClient;
import io.statful.client.test.HttpTest;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.RegexBody.regex;
import static org.mockserver.verify.VerificationTimes.once;

public class StatfulFactoryTest extends HttpTest {

    @Test
    public void shouldCreateUdpClient() throws Exception {
        // Given
        mockClientAndServer.when(
                request()
                        .withMethod("PUT").withPath("/tel/v1/metrics"),
                exactly(1))
                .respond(
                        response().withStatusCode(201));

        // TODO - find a way to remove this sleep since the client is async
        Thread.sleep(200);

        // When
        StatfulClient client = StatfulFactory.buildHTTPClient("test_prefix").with()
                .host("127.0.0.1")
                .secure(false)
                .port(mockServerPort)
                .token("a")
                .flushSize(1)
                .build();

        client.counter("test_counter").send();

        // TODO - find a way to remove this sleep since the client is async
        Thread.sleep(200);

        // Then
        mockClientAndServer.verify(
                request()
                        .withBody(regex("test_prefix.application.counter.test_counter 1 .+ count,sum,10\\n")),
                once()
        );
    }

    @Test
    public void shouldCreateUdpClientWithoutOptionalConfigurations() throws Exception {
        StatfulClient client = StatfulFactory.buildHTTPClient("test_prefix").build();

        assertNotNull(client);
    }

}