package com.example.HomePage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobRepo;

    public List<JobPost> getAllJobs() {
        return jobRepo.findAll();
    }

    public JobPost getJobById(Long id) {
        return jobRepo.findById(id).orElse(null);
    }

    public JobPost save(JobPost job) {
        return jobRepo.save(job);
    }

    public void delete(Long id) {
        jobRepo.deleteById(id);
    }

    // Search jobs by title or company
    public List<JobPost> searchJobs(String search) {
        if (search == null || search.trim().isEmpty()) {
            return jobRepo.findAll();
        }
        return jobRepo.findByTitleContainingIgnoreCaseOrCompanyContainingIgnoreCase(search, search);
    }
}
