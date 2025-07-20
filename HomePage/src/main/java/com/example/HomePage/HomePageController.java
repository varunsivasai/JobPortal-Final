package com.example.HomePage;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomePageController {

    @Autowired
    private RegisterJobSeekerRepository jobSeekerRepository;

    @Autowired
    private RegisterJobRecuiterRepository recuiterRepository;

    @GetMapping("/")
    public String homepage() {
        return "HomePage";  // Landing page (with login/register links)
    }

    @GetMapping("/register/jobseeker")
    public String registerJobseekerPage() {
        return "Register-jobseeker";
    }

    @PostMapping("/register/jobseeker")
    public String registerJobseeker(@RequestParam String username, @RequestParam String password) {
        RegisterJobSeeker js = new RegisterJobSeeker();
        js.setUsername(username);
        js.setPassword(password);
        jobSeekerRepository.save(js);
        return "registerJobSeekerSuccesfully";
    }

    @GetMapping("/register/recruiter")
    public String registerRecuiterPage() {
        return "Register-recuiter";
    }

    @PostMapping("/register/recruiter")
    public String registerRecuiter(@RequestParam String username, @RequestParam String password) {
        RegisterRecuiter rec = new RegisterRecuiter();
        rec.setUsername(username);
        rec.setPassword(password);
        recuiterRepository.save(rec);
        return "registerRecuiterSuccessfully";
    }

    @GetMapping("/login/jobseeker")
    public String loginPage() {
        return "LoginJobseekerPage"; // Generic login page
    }
    @GetMapping("/login/recruiter")
    public String loginPage1() {
        return "LoginJobseekerPage"; // Generic login page
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam String role,
                        HttpSession session) {

        // Check Job Seeker login
        if ("jobseeker".equalsIgnoreCase(role)) {
            RegisterJobSeeker js = jobSeekerRepository.findByUsername(username);
            if (js != null && js.getPassword().equals(password)) {
                session.setAttribute("username", username);
                return handlePostLoginRedirect(session, "redirect:/jobseeker/homepage");
            }
        }

        // Check Recruiter login
        else if ("recruiter".equalsIgnoreCase(role)) {
            RegisterRecuiter rec = recuiterRepository.findByUsername(username);
            if (rec != null && rec.getPassword().equals(password)) {
                session.setAttribute("username", username);
                return handlePostLoginRedirect(session, "redirect:/recruiter/dashboard");
            }
        }

        return "loginFailed"; // Show login failed page
    }

    // Redirect logic (handles Apply Job redirect)
    private String handlePostLoginRedirect(HttpSession session, String defaultRedirect) {
        String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
        if (redirectAfterLogin != null) {
            session.removeAttribute("redirectAfterLogin");
            return "redirect:" + redirectAfterLogin;
        }
        return defaultRedirect;
    }
}
