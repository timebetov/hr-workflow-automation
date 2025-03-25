package com.hrworkflow.searchservice.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.hrworkflow.searchservice.dto.JobDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JobRepository {

    private static final String INDEX_NAME = "jobs";

    private final ElasticsearchClient elasticsearchClient;

    public void save(JobDocument job) throws IOException {

        IndexResponse response = elasticsearchClient.index(i -> i
                .index(INDEX_NAME)
                .id(job.getId())
                .document(job));
    }

    public Optional<JobDocument> findById(String id) throws IOException {

        GetResponse<JobDocument> response = elasticsearchClient.get(g -> g
                .index(INDEX_NAME)
                .id(id), JobDocument.class
        );

        return (response.found()) ? Optional.of(response.source()) : Optional.empty();
    }

    public void deleteById(String id) throws IOException {

        DeleteResponse response = elasticsearchClient.delete(d -> d
                .index(INDEX_NAME)
                .id(id)
        );
    }
}