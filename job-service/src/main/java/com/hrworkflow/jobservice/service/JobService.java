package com.hrworkflow.jobservice.service;

import com.hrworkflow.jobservice.dto.JobFilterRequestDTO;
import com.hrworkflow.jobservice.dto.ResourceNotFoundException;
import com.hrworkflow.jobservice.model.Job;
import com.hrworkflow.jobservice.model.JobStatus;
import com.hrworkflow.jobservice.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public Job createJob(Job job) {

        job.setId(UUID.randomUUID().toString());
        job.setPostedAt(LocalDate.now());
        job.setStatus(JobStatus.OPEN);
        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {

        var jobs = jobRepository.findAll();
        return StreamSupport.stream(jobs.spliterator(), false)
                .toList();
    }

    public Map<Boolean, List<Job>> getJobsByStatus() {
        return getAllJobs().stream()
                .collect(Collectors.partitioningBy(job -> job.getStatus() == JobStatus.OPEN));
    }

    public Job getJob(String jobId) {

        return jobRepository.findById(jobId).orElseThrow(
                () -> new ResourceNotFoundException("Job with id " + jobId + " not found")
        );
    }

    public void updateJobStatus(String jobId, JobStatus status) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id " + jobId + " not found"));

        job.setStatus(status);
        jobRepository.save(job);
    }

    public void deleteJob(String jobId) {

        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job not found with id: " + jobId);
        }
        jobRepository.deleteById(jobId);
    }

    // FILTERS
    public List<Job> filterJobs(JobFilterRequestDTO filter) {

        var jobs = jobRepository.findAll();
        return StreamSupport.stream(jobs.spliterator(), false)
                .filter(job -> filter.getTitle() == null || job.getTitle().toLowerCase().contains(filter.getTitle().toLowerCase()))
                .filter(job -> filter.getDepartment() == null || job.getDepartment().equalsIgnoreCase(filter.getDepartment().toLowerCase()))
                .filter(job -> filter.getStatus() == null || job.getStatus() == filter.getStatus())
                .filter(job -> filter.getMinSalary() == null || job.getSalary() >= filter.getMinSalary())
                .filter(job -> filter.getMaxSalary() == null || job.getSalary() <= filter.getMaxSalary())
                .filter(job -> filter.getJobType() == null || job.getJobType().equals(filter.getJobType()))
                .filter(job -> filter.getPostedAfter() == null || job.getPostedAt().isAfter(filter.getPostedAfter()))
                .filter(job -> filter.getPostedBefore() == null || job.getPostedAt().isAfter(filter.getPostedBefore()))
                .toList();
    }

    public Map<String, Long> compareStreamPerformance() {

        List<Job> jobs = getAllJobs();

        long startSequential = System.nanoTime();
        jobs.stream().filter(job -> job.getSalary() > 50000).toList();
        long durationSequential = System.nanoTime() - startSequential;

        long startParallel = System.nanoTime();
        jobs.parallelStream().filter(job -> job.getSalary() > 50000).toList();
        long durationParallel = System.nanoTime() - startParallel;

        Map<String, Long> performanceResult = new HashMap<>();
        performanceResult.put("sequential", durationSequential);
        performanceResult.put("parallel", durationParallel);

        return performanceResult;
    }

    public Map.Entry<String, Double> getDepartmentWithHighestSalary() {

        return getAllJobs().stream()
                .collect(Collectors.groupingBy(Job::getDepartment,
                        Collectors.mapping(Job::getSalary,
                                Collectors.maxBy(Double::compare))))
                .entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().orElse(0.0))) // Преобразуем Optional в Double
                .reduce((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? entry1 : entry2)
                .orElse(Map.entry("No Data", 0.0));
    }

    public Map<String, Map<JobStatus, List<Job>>> getJobsByDepartment() {

        return getAllJobs().stream()
                .collect(Collectors.groupingBy(Job::getDepartment, Collectors.groupingBy(Job::getStatus)));
    }

    public List<Job> searchJobs(String title, String department, Double minSalary, Double maxSalary) {

        Predicate<Job> titleFilter = job -> title == null || job.getTitle().toLowerCase().contains(title.toLowerCase());
        Predicate<Job> departmentFilter = job -> department == null || job.getDepartment().equalsIgnoreCase(department.toLowerCase());
        Predicate<Job> minSalaryFilter = job -> minSalary == null || job.getSalary() >= minSalary;
        Predicate<Job> maxSalaryFilter = job -> maxSalary == null || job.getSalary() <= maxSalary;

        return getAllJobs().stream()
                .filter(titleFilter
                        .and(departmentFilter)
                        .and(minSalaryFilter)
                        .and(maxSalaryFilter))
                .toList();
    }

}
