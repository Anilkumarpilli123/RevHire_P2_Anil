package com.rev.app.controller;

import com.rev.app.entity.*;
import com.rev.app.repository.UserRepository;
import com.rev.app.service.ApplicationService;
import com.rev.app.service.EmployerService;
import com.rev.app.service.JobService;
import com.rev.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    @Autowired
    private JobService jobService;

    @Autowired
    private EmployerService employerService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @ModelAttribute
    public void addAttributes(Model model, Authentication auth) {
        if (auth != null) {
            User user = userRepository.findByEmail(auth.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("currentUser", user);
                try {
                    model.addAttribute("profile", employerService.getProfileByUser(user));
                    model.addAttribute("unreadCount", notificationService.getUnreadCount(user));
                } catch (Exception e) {
                }
            }
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        EmployerProfile profile = employerService.getProfileByUser(user);

        if (!profile.isComplete()) {
            return "redirect:/employer/profile/edit?message=complete_profile_first";
        }

        if (profile.getCompany() != null) {
            List<Job> jobs = jobService.getJobsByCompany(profile.getCompany().getId());
            model.addAttribute("jobs", jobs);

            // Analytics
            long totalApplications = jobs.stream()
                    .mapToLong(j -> applicationService.getApplicationsByJob(j).size())
                    .sum();
            long totalShortlisted = jobs.stream()
                    .flatMap(j -> applicationService.getApplicationsByJob(j).stream())
                    .filter(a -> "SHORTLISTED".equals(a.getStatus()))
                    .count();

            long totalPending = jobs.stream()
                    .flatMap(j -> applicationService.getApplicationsByJob(j).stream())
                    .filter(a -> "APPLIED".equals(a.getStatus()))
                    .count();

            model.addAttribute("totalApplications", totalApplications);
            model.addAttribute("totalShortlisted", totalShortlisted);
            model.addAttribute("totalPending", totalPending);
            model.addAttribute("jobCount", jobs.size());
        }
        return "employer/dashboard";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        EmployerProfile profile = employerService.getProfileByUser(user);

        if (!profile.isComplete()) {
            return "redirect:/employer/profile/edit?message=complete_profile_first";
        }

        model.addAttribute("profile", profile);
        model.addAttribute("email", user.getEmail());
        return "employer/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        EmployerProfile profile = employerService.getProfileByUser(user);
        model.addAttribute("profile", profile);
        return "employer/profile-edit";
    }

    @PostMapping("/profile")
    public String saveProfile(@RequestParam String companyName,
            @RequestParam String companyIndustry,
            @RequestParam String companyDescription,
            @RequestParam String companyWebsite,
            @RequestParam String companyLocation,
            @RequestParam(required = false) String personalName,
            @RequestParam(required = false) String personalPhone,
            @RequestParam(required = false) String personalLocation,
            @RequestParam(required = false) String jobRole,
            Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        EmployerProfile profile = employerService.getProfileByUser(user);

        // Update Personal Details
        profile.setName(cleanValue(personalName));
        profile.setPhone(cleanValue(personalPhone));
        profile.setLocation(cleanValue(personalLocation));
        profile.setJobRole(cleanValue(jobRole));

        // Update Company Details
        Company company = profile.getCompany();
        if (company == null) {
            company = new Company();
        }
        company.setName(cleanValue(companyName));
        company.setIndustry(cleanValue(companyIndustry));
        company.setDescription(cleanValue(companyDescription));
        company.setWebsite(cleanValue(companyWebsite));
        company.setLocation(cleanValue(companyLocation));

        profile.setCompany(company);
        employerService.updateProfile(profile);

        return "redirect:/employer/dashboard?success=profile_updated";
    }

    private String cleanValue(String val) {
        if (val == null)
            return null;
        val = val.trim();
        if (val.contains(",")) {
            String[] parts = val.split(",");
            for (String part : parts) {
                if (part != null && !part.trim().isEmpty()) {
                    return part.trim();
                }
            }
        }
        return val;
    }

    @GetMapping("/jobs/new")
    public String newJob(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        EmployerProfile profile = employerService.getProfileByUser(user);

        if (!profile.isComplete()) {
            return "redirect:/employer/profile/edit?message=complete_profile_first";
        }

        model.addAttribute("job", new Job());
        return "employer/post-job";
    }

    @PostMapping("/jobs")
    public String saveJob(@ModelAttribute Job job, Authentication auth, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(auth.getName()).get();
        EmployerProfile profile = employerService.getProfileByUser(user);

        if (!profile.isComplete()) {
            return "redirect:/employer/profile/edit?message=complete_profile_first";
        }

        job.setCompany(profile.getCompany());
        jobService.createJob(job);
        return "redirect:/employer/dashboard?success=job_posted";
    }

    @GetMapping("/applicants/{jobId}")
    public String viewApplicants(@PathVariable int jobId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String experience,
            Model model) {
        Job job = jobService.getJobById(jobId);
        List<Application> applications;

        if (status != null || name != null || experience != null) {
            applications = applicationService.searchApplications(job, status, name, experience);
        } else {
            applications = applicationService.getApplicationsByJob(job);
        }

        System.out.println("DEBUG: viewApplicants for jobId=" + jobId + ", found " + applications.size() + " apps");
        model.addAttribute("job", job);
        model.addAttribute("applications", applications);
        return "employer/applicants";
    }

    @GetMapping("/jobs/edit/{id}")
    public String editJob(@PathVariable int id, Model model) {
        System.out.println("DEBUG: editJob hit with id=" + id);
        Job job = jobService.getJobById(id);
        System.out.println("DEBUG: Found job: " + job.getTitle());
        model.addAttribute("job", job);
        return "employer/edit-job";
    }

    @PostMapping("/jobs/edit/{id}")
    public String updateJob(@PathVariable int id, @ModelAttribute Job job, Authentication auth) {
        System.out.println("DEBUG: updateJob hit with id=" + id);
        Job existingJob = jobService.getJobById(id);
        System.out.println("DEBUG: Updating existingJob: " + existingJob.getTitle());

        existingJob.setTitle(job.getTitle());
        existingJob.setDescription(job.getDescription());
        existingJob.setLocation(job.getLocation());
        existingJob.setJobType(job.getJobType());
        existingJob.setSalaryRange(job.getSalaryRange());
        existingJob.setExperienceRequired(job.getExperienceRequired());
        existingJob.setSkillsRequired(job.getSkillsRequired());
        existingJob.setEducationRequired(job.getEducationRequired());
        existingJob.setDeadline(job.getDeadline());
        existingJob.setNumberOfOpenings(job.getNumberOfOpenings());

        jobService.updateJob(existingJob);
        System.out.println("DEBUG: Job updated successfully");
        return "redirect:/employer/dashboard?success=job_updated";
    }

    @PostMapping("/applications/{applicationId}/status")
    public String updateApplicationStatus(@PathVariable int applicationId,
            @RequestParam String status,
            @RequestParam(required = false) String notes) {
        System.out.println("DEBUG: updateApplicationStatus hit for app=" + applicationId + ", status=" + status);
        Application app = applicationService.updateStatus(applicationId, status, notes);
        return "redirect:/employer/applicants/" + app.getJob().getId() + "?success=status_updated";
    }

    @PostMapping("/applications/bulk-status")
    public String bulkUpdateStatus(
            @RequestParam(value = "applicationIds", required = false) List<Integer> applicationIds,
            @RequestParam String status,
            @RequestParam(required = false) String notes,
            @RequestParam int jobId) {
        if (applicationIds == null || applicationIds.isEmpty()) {
            return "redirect:/employer/applicants/" + jobId + "?error=no_selection";
        }
        applicationService.bulkUpdateStatus(applicationIds, status, notes);
        return "redirect:/employer/applicants/" + jobId + "?success=bulk_updated";
    }

    @GetMapping("/jobs/manage")
    public String manageJobs(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        EmployerProfile profile = employerService.getProfileByUser(user);

        if (!profile.isComplete()) {
            return "redirect:/employer/profile/edit?message=complete_profile_first";
        }

        List<Job> jobs = jobService.getJobsByCompany(profile.getCompany().getId());
        model.addAttribute("jobs", jobs);
        return "employer/manage-jobs";
    }

    @PostMapping("/jobs/{id}/status")
    public String updateJobStatus(@PathVariable int id, @RequestParam String status) {
        jobService.updateJobStatus(id, status);
        return "redirect:/employer/jobs/manage?success=status_updated";
    }

    @PostMapping("/jobs/{id}/delete")
    public String deleteJob(@PathVariable int id) {
        jobService.deleteJob(id);
        return "redirect:/employer/jobs/manage?success=job_deleted";
    }

    @GetMapping("/notifications")
    public String viewNotifications(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        model.addAttribute("notifications", notificationService.getNotificationsByUser(user));
        return "employer/notifications";
    }
}
