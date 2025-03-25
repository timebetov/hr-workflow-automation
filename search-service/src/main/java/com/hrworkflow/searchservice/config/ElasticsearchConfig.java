package com.hrworkflow.searchservice.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class ElasticsearchConfig {

    private static final String ES_HOST = "localhost";
    private static final int ES_PORT = 9200;
    private static final String ES_USERNAME = "jobelastic";
    private static final String ES_PASSWORD = "passwordJob";

    @Bean
    public RestClient restClient() {
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(ES_USERNAME, ES_PASSWORD));

        return RestClient.builder(new HttpHost(ES_HOST, ES_PORT))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                )
                .build();
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule()) // Поддержка LocalDateTime
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Используем ISO 8601

        JacksonJsonpMapper jsonpMapper = new JacksonJsonpMapper(objectMapper);

        return new ElasticsearchClient(new RestClientTransport(restClient, jsonpMapper));
    }
}

