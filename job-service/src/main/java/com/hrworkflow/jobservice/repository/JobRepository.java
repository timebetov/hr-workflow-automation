package com.hrworkflow.jobservice.repository;

import com.hrworkflow.jobservice.model.Job;
import com.hrworkflow.jobservice.model.JobStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends ElasticsearchRepository<Job, String> {
}
