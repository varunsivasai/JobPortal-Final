package com.example.HomePage;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobApplicationController {

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private JobApplicationRepository applicationRepo;

    @Autowired
    private JobSeekerProfileService profileService;

    // Apply for a job
    @PostMapping("/apply/{jobId}")
    public String applyForJob(@PathVariable Long jobId, HttpSession session) {
        String username = (String) session.getAttribute("username");

        // If not logged in, save redirect URL and go to login
        if (username == null) {
            session.setAttribute("redirectAfterLogin", "/jobs/apply/" + jobId);
            return "redirect:/login";
        }

        RegisterJobSeeker user = profileService.getByUserName(username);
        JobSeekerProfile seeker = (user != null) ? profileService.getByUser(user) : null;
        JobPost job = jobPostService.getJobById(jobId);

        // Prevent duplicate applications
        if (seeker != null && job != null &&
                !applicationRepo.existsByJobSeeker_IdAndJobPost_Id(seeker.getId(), job.getId())) {

            JobApplication app = new JobApplication();
            app.setJobPost(job);
            app.setJobSeeker(seeker);
            app.setStatus("PENDING");
            applicationRepo.save(app);
        }

        return "redirect:/jobs/landing";  // Return to job listings
    }

    // View my applications (for job seeker)
    @GetMapping("/my-applications")
    public String viewMyApplications(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        RegisterJobSeeker user = profileService.getByUserName(username);
        if (user == null) return "redirect:/login";

        JobSeekerProfile seeker = profileService.getByUser(user);
        if (seeker == null) return "redirect:/profile/create";  // Force profile creation if missing

        List<JobApplication> apps = applicationRepo.findByJobSeeker_Id(seeker.getId());
        model.addAttribute("applications", apps);
        return "job_applications";
    }
}
