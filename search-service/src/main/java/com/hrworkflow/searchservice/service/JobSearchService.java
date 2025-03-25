package com.hrworkflow.searchservice.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrworkflow.searchservice.dto.JobDocument;
import com.hrworkflow.searchservice.dto.JobFilterRequestDTO;
import com.hrworkflow.searchservice.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class JobSearchService {

    private final JobRepository jobRepository;
    private final ElasticsearchClient elasticsearchClient;

    private static final String INDEX_NAME = "jobs";

    public List<JobDocument> searchJobs(JobFilterRequestDTO filterReq) throws IOException {

        BoolQuery.Builder boolQuery = QueryBuilders.bool();

        if (filterReq.getTitle() != null) {
            boolQuery.must(QueryBuilders.match(m -> m.field("title").query(filterReq.getTitle())));
        }

        if (filterReq.getDepartment() != null) {
            boolQuery.must(QueryBuilders.match(m -> m.field("department").query(filterReq.getDepartment())));
        }

        if (filterReq.getJobType() != null) {
            boolQuery.must(QueryBuilders.match(m -> m.field("jobType").query(filterReq.getJobType())));
        }

        if (filterReq.getMinSalary() != null || filterReq.getMaxSalary() != null) {
            boolQuery.filter(QueryBuilders.range(r -> r
                    .field("salary")
                    .gte(filterReq.getMinSalary() != null ? JsonData.of(filterReq.getMinSalary()) : null)
                    .lte(filterReq.getMaxSalary() != null ? JsonData.of(filterReq.getMaxSalary()) : null)
            ));
        }

        if (filterReq.getKeywords() != null && filterReq.getKeywords().length > 0) {
            BoolQuery.Builder keywordBoolQuery = QueryBuilders.bool();

            for (String keyword : filterReq.getKeywords()) {
                keywordBoolQuery.should(QueryBuilders.match(m -> m.field("description").query(keyword)));
            }

            boolQuery.must(keywordBoolQuery.build()._toQuery());
        }

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(INDEX_NAME)
                .query(boolQuery.build()._toQuery())
        );

        SearchResponse<JobDocument> response = elasticsearchClient.search(searchRequest, JobDocument.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    public Map.Entry<String, Double> getDepartmentWithHighestSalary() throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("jobs")
                .size(0) // Нам не нужны отдельные документы, только агрегация
                .aggregations("max_salary_per_department", a -> a
                        .terms(t -> t.field("department").size(10))
                        .aggregations("max_salary", a2 -> a2.max(m -> m.field("salary")))
                )
        );

        SearchResponse<Void> response = elasticsearchClient.search(searchRequest, Void.class);
        Aggregate depAgg = response.aggregations().get("max_salary_per_department");

        if (depAgg != null && depAgg.isSterms()) {
            return depAgg.sterms().buckets().array().stream()
                    .map(bucket -> {
                        String department = bucket.key().stringValue();
                        Double maxSalary = bucket.aggregations().get("max_salary").max().value();
                        return Map.entry(department, maxSalary);
                    })
                    .max(Map.Entry.comparingByValue())
                    .orElse(Map.entry("No Data", 0.0));
        }
        return Map.entry("No Data", 0.0);
    }
}
