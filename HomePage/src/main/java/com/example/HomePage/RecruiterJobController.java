package com.example.HomePage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/recruiter/jobs")
public class RecruiterJobController {

    @Autowired
    private JobPostService jobService;

    @Autowired
    private JobSeekerProfileService jobSeekerService;  // FIX: Added service to fetch job seekers

    // List all jobs (Recruiter can manage here)
    @GetMapping
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobService.getAllJobs());
        return "job_manage";  // Page to view/edit/delete jobs
    }

    // Show job creation form
    @GetMapping("/create")
    public String createJobForm(Model model) {
        model.addAttribute("job", new JobPost());
        return "job_create";
    }

    // Save new job
    @PostMapping("/save")
    public String saveJob(@ModelAttribute JobPost job) {
        jobService.save(job);
        return "redirect:/recruiter/jobs";
    }

    // Show job edit form
    @GetMapping("/edit/{id}")
    public String editJobForm(@PathVariable Long id, Model model) {
        JobPost job = jobService.getJobById(id);
        model.addAttribute("job", job);
        return "job_create";  // Reuse form for edit
    }

    // Update job
    @PostMapping("/update/{id}")
    public String updateJob(@PathVariable Long id, @ModelAttribute JobPost job) {
        job.setId(id);
        jobService.save(job);
        return "redirect:/recruiter/jobs";
    }

    // Delete job
    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobService.delete(id);
        return "redirect:/recruiter/jobs";
    }

    // View all job seekers (for recruiter)
    @GetMapping("/jobseekers")
    public String viewJobSeekers(Model model) {
        List<JobSeekerProfile> seekers = jobSeekerService.getAllProfiles();
        model.addAttribute("seekers", seekers);
        return "recruiter_jobseekers";  // Show job seekers list
    }
}
