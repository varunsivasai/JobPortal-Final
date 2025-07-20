package com.example.HomePage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/jobs")
public class JobListingController {

    @Autowired
    private JobPostRepository jobPostRepo;

    // Show all jobs or filter by search term
    @GetMapping("/landing")
    public String listJobs(@RequestParam(value = "search", required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("jobs", jobPostRepo.findByTitleContainingIgnoreCase(search));
        } else {
            model.addAttribute("jobs", jobPostRepo.findAll());
        }
        return "job_listings";  // Thymeleaf page
    }
}
