package com.hrworkflow.searchservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchInit {

    private final ElasticsearchClient elasticsearchClient;
    private static final String INDEX_NAME = "jobs";

    @PostConstruct
    public void init() throws IOException {

        try {
            if (!isIndexExists()) {
                createIndex();
            }
        } catch (ElasticsearchException e) {
            log.error("Error while initializing Elasticsearch: {}", e.getMessage());
        }
    }

    private boolean isIndexExists() {
        try {
            GetIndexResponse response = elasticsearchClient.indices().get(i -> i.index(INDEX_NAME));
            return response.result().containsKey(INDEX_NAME);
        } catch (ElasticsearchException e) {
            if (e.response().error().type().equals("index_not_found_exception")) {
                log.warn("Index '{}' does not exist.", INDEX_NAME);
                return false;
            }
            log.error("Error checking index '{}': {}", INDEX_NAME, e.getMessage());
            throw e; // Пробрасываем дальше
        } catch (IOException e) {
            log.error("I/O Error checking index '{}': {}", INDEX_NAME, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private void createIndex() {
        try {
            CreateIndexResponse response = elasticsearchClient.indices().create(c -> c
                    .index(INDEX_NAME)
                    .mappings(m -> m
                            .properties("title", p -> p.text(t -> t))
                            .properties("description", p -> p.text(t -> t))
                            .properties("department", p -> p.keyword(k -> k))
                            .properties("jobType", p -> p.keyword(k -> k))
                            .properties("status", p -> p.keyword(k -> k))
                            .properties("deadline", p -> p.date(d -> d))
                            .properties("salary", p -> p.double_(d -> d))
                    )
            );

            if (response.acknowledged()) {
                log.info("Index '{}' created successfully.", INDEX_NAME);
            } else {
                log.error("Failed to create index '{}'. Elasticsearch did not acknowledge.", INDEX_NAME);
            }
        } catch (IOException e) {
            log.error("Error while creating index '{}': {}", INDEX_NAME, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
