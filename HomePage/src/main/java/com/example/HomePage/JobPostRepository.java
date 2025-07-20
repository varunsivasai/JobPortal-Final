package com.example.HomePage;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    List<JobPost> findByTitleContainingIgnoreCaseOrCompanyContainingIgnoreCase(String title, String company);

    List<JobPost> findByTitleContainingIgnoreCase(String search);

}
