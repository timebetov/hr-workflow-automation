package com.github.timebetov.service;

import com.github.timebetov.dto.Interview;
import com.github.timebetov.repository.InterviewRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepositoryInterface interviewRepository;

    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    public Interview getInterviewById(int id) {
        return interviewRepository.findById(id);
    }

    public List<Interview> getInterviewsByStatus(Interview.InterviewStatus status) {
        return interviewRepository.findByStatus(status);
    }

    public boolean addInterview(Interview interview) {

        Objects.requireNonNull(interview, "Interview cannot be null");
        return interviewRepository.add(interview);
    }

    public Interview setStatus(int id, Interview.InterviewStatus status) {
        return interviewRepository.setStatus(id, status);
    }

    public boolean removeInterview(int id) {
        return interviewRepository.remove(id);
    }


}
