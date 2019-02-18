package de.ProPra.Lending;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class APIProcessor {
    // This Class interacts with the ProPay Application

    public static <T> T getAccountInformation(String name, final Class<T> type) {
        final Mono<T> mono = WebClient
                .create()
                .get()
                .uri(builder ->
                        builder.scheme("http")
                                .host("localhost")
                                .port("8888")
                                .pathSegment("account", name)
                                // for trailing slash
                                .path("/")
                                .build())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToMono(type);
        return mono.block();
    }
}
