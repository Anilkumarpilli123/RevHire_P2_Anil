package com.rev.app.controller;

import com.rev.app.entity.Application;
import com.rev.app.entity.Job;
import com.rev.app.entity.EmploymentStatus;
import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.entity.*;
import com.rev.app.repository.FavoriteJobRepository;
import com.rev.app.repository.JobRepository;
import com.rev.app.repository.UserRepository;
import com.rev.app.service.ApplicationService;
import com.rev.app.service.JobSeekerService;
import com.rev.app.service.JobService;
import com.rev.app.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/seeker")
public class SeekerController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobSeekerService jobSeekerService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private FavoriteJobRepository favoriteJobRepository;

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute
    public void addAttributes(Model model, Authentication auth) {
        if (auth != null) {
            User user = userRepository.findByEmail(auth.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("currentUser", user);
                try {
                    model.addAttribute("profile", getOrCreateProfile(user));
                } catch (Exception e) {
                }
            }
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        JobSeekerProfile profile = getOrCreateProfile(user);

        if (!profile.isComplete()) {
            return "redirect:/seeker/profile/edit?message=complete_profile_first";
        }

        List<Application> applications = applicationService.getApplicationsBySeeker(profile);

        long appliedJobs = applications.size();
        long shortlisted = applications.stream().filter(a -> "SHORTLISTED".equals(a.getStatus())).count();

        model.addAttribute("appliedJobs", appliedJobs);
        model.addAttribute("shortlisted", shortlisted);
        model.addAttribute("profile", profile);
        model.addAttribute("currentUser", user);
        return "seeker/dashboard";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Authentication auth) {
        try {
            User user = userRepository.findByEmail(auth.getName()).get();
            JobSeekerProfile profile = getOrCreateProfile(user);

            if (profile == null) {
                System.err.println("PROFILE IS NULL FOR USER: " + user.getEmail());
                return "redirect:/seeker/dashboard?error=profile_creation_failed";
            }

            if (!profile.isComplete()) {
                return "redirect:/seeker/profile/edit?message=complete_profile_first";
            }

            model.addAttribute("profile", profile);
            model.addAttribute("email", user.getEmail());

            // Calculate profile completion percentage
            int completion = 0;
            int totalFields = 5; // Basic: Name, Phone, Location, Status, Experience

            if (profile.getName() != null && !profile.getName().isEmpty())
                completion++;
            if (profile.getPhone() != null && !profile.getPhone().isEmpty())
                completion++;
            if (profile.getLocation() != null && !profile.getLocation().isEmpty())
                completion++;
            if (profile.getEmploymentStatus() != null)
                completion++;
            if (profile.getExperience() != null && !profile.getExperience().isEmpty())
                completion++;

            if (EmploymentStatus.EMPLOYED.equals(profile.getEmploymentStatus())) {
                totalFields += 2; // Company, Role
                if (profile.getCompanyName() != null && !profile.getCompanyName().isEmpty())
                    completion++;
                if (profile.getJobRole() != null && !profile.getJobRole().isEmpty())
                    completion++;
            } else if (EmploymentStatus.STUDENT.equals(profile.getEmploymentStatus())) {
                totalFields += 2; // College, Branch
                if (profile.getCollegeName() != null && !profile.getCollegeName().isEmpty())
                    completion++;
                if (profile.getBranch() != null && !profile.getBranch().isEmpty())
                    completion++;
            } else if (EmploymentStatus.UNEMPLOYED.equals(profile.getEmploymentStatus())) {
                if (!"Fresher".equalsIgnoreCase(profile.getExperience())) {
                    totalFields += 1; // Role mandatory if not fresher
                    if (profile.getJobRole() != null && !profile.getJobRole().isEmpty())
                        completion++;
                }
            }

            int percentage = (totalFields > 0) ? (completion * 100) / totalFields : 0;
            model.addAttribute("completionPercentage", percentage);

            return "seeker/profile";
        } catch (Exception e) {
            System.err.println("ERROR IN SHOWPROFILE: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        JobSeekerProfile profile = getOrCreateProfile(user);
        model.addAttribute("profile", profile);
        model.addAttribute("email", user.getEmail());
        return "seeker/profile-edit";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute JobSeekerProfile profileData, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        JobSeekerProfile existingProfile = getOrCreateProfile(user);

        existingProfile.setName(profileData.getName());
        existingProfile.setPhone(profileData.getPhone());
        existingProfile.setLocation(profileData.getLocation());
        existingProfile.setEmploymentStatus(profileData.getEmploymentStatus());
        existingProfile.setCompanyName(profileData.getCompanyName());
        existingProfile.setCollegeName(profileData.getCollegeName());
        existingProfile.setBranch(profileData.getBranch());
        existingProfile.setJobRole(profileData.getJobRole());
        existingProfile.setExperience(profileData.getExperience());

        jobSeekerService.updateProfile(existingProfile);
        return "redirect:/seeker/profile?success=profile_updated";
    }

    @GetMapping("/jobs")
    public String browseJobs(Model model, Authentication auth,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) Integer experience) {

        User user = userRepository.findByEmail(auth.getName()).get();
        JobSeekerProfile profile = getOrCreateProfile(user);
        if (!profile.isComplete()) {
            return "redirect:/seeker/profile/edit?message=complete_profile_first";
        }

        List<Job> jobs;
        if (title != null || location != null || companyName != null || jobType != null || experience != null) {
            jobs = jobService.searchJobs(title, location, null, experience, companyName, jobType);
        } else {
            jobs = jobService.getAllOpenJobs();
        }

        // Fetch favorite job IDs for the current user
        java.util.Set<Integer> favoriteJobIds = favoriteJobRepository.findByUser(user)
                .stream()
                .map(f -> f.getJob().getId())
                .collect(java.util.stream.Collectors.toSet());

        model.addAttribute("jobs", jobs);
        model.addAttribute("favoriteJobIds", favoriteJobIds);
        return "seeker/jobs";
    }

    @GetMapping("/resume")
    public String editResume(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        JobSeekerProfile profile = getOrCreateProfile(user);
        if (!profile.isComplete()) {
            return "redirect:/seeker/profile/edit?message=complete_profile_first";
        }

        Resume resume = resumeService.getResumeBySeeker(profile);
        if (resume == null) {
            resume = new Resume();
            resume.setJobSeeker(profile);
        }
        model.addAttribute("resume", resume);
        return "seeker/resume";
    }

    @PostMapping("/resume")
    public String updateResume(@ModelAttribute Resume resume,
            @RequestParam(value = "resumeFile", required = false) MultipartFile file,
            Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        JobSeekerProfile profile = getOrCreateProfile(user);

        try {
            Resume existing = resumeService.getResumeBySeeker(profile);
            if (existing != null) {
                resume.setId(existing.getId());
                resume.setResumePath(existing.getResumePath()); // Preserve existing path if no new file
            }
            resume.setJobSeeker(profile);
            resumeService.saveResume(resume);

            if (file != null && !file.isEmpty()) {
                resumeService.uploadResume(profile, file);
            }
            return "redirect:/seeker/resume?success=true";
        } catch (RuntimeException e) {
            return "redirect:/seeker/resume?error="
                    + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }

    @GetMapping("/applications")
    public String myApplications(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        JobSeekerProfile profile = getOrCreateProfile(user);
        if (!profile.isComplete()) {
            return "redirect:/seeker/profile/edit?message=complete_profile_first";
        }

        List<Application> applications = applicationService.getApplicationsBySeeker(profile);
        model.addAttribute("applications", applications);
        return "seeker/applications";
    }

    @PostMapping("/jobs/{id}/favorite")
    public String toggleFavorite(@PathVariable int id, Authentication auth,
            @RequestParam(required = false) String redirect) {
        User user = userRepository.findByEmail(auth.getName()).get();
        jobService.toggleFavorite(user, id);
        return "redirect:" + (redirect != null ? redirect : "/seeker/jobs");
    }

    @GetMapping("/favorites")
    public String viewFavorites(Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        List<FavoriteJob> favorites = favoriteJobRepository.findByUser(user);
        model.addAttribute("favorites", favorites);
        return "seeker/favorites";
    }

    @GetMapping("/jobs/{id}")
    public String jobDetails(@PathVariable int id, Model model, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        JobSeekerProfile profile = getOrCreateProfile(user);
        if (!profile.isComplete()) {
            return "redirect:/seeker/profile/edit?message=complete_profile_first";
        }

        Job job = jobService.getJobById(id);
        List<Application> apps = applicationService.getApplicationsBySeeker(profile);
        boolean alreadyApplied = apps.stream().anyMatch(a -> a.getJob().getId() == id);

        // Check if the job is favorited by the current user
        boolean isFavorited = favoriteJobRepository.existsByUserAndJob(user, job);

        model.addAttribute("job", job);
        model.addAttribute("alreadyApplied", alreadyApplied);
        model.addAttribute("isFavorited", isFavorited);
        return "seeker/job-details";
    }

    @PostMapping("/jobs/apply/{id}")
    public String applyForJob(@PathVariable int id, @RequestParam(required = false) String coverLetter,
            Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).get();
        JobSeekerProfile profile = jobSeekerService.getProfileByUser(user);
        if (!profile.isComplete()) {
            return "redirect:/seeker/profile/edit?message=complete_profile_first";
        }

        Job job = jobService.getJobById(id);
        applicationService.applyForJob(job, profile, coverLetter);
        return "redirect:/seeker/applications?success=applied";
    }

    @PostMapping("/applications/withdraw/{id}")
    public String withdrawApplication(@PathVariable int id) {
        applicationService.withdrawApplication(id, "Withdrawn by candidate");
        return "redirect:/seeker/applications?success=withdrawn";
    }

    private JobSeekerProfile getOrCreateProfile(User user) {
        if (user == null)
            return null;
        try {
            return jobSeekerService.getProfileByUser(user);
        } catch (Exception e) {
            System.err.println("Profile not found for user " + user.getEmail() + ", creating one...");
            try {
                JobSeekerProfile profile = new JobSeekerProfile();
                profile.setUser(user);
                String defaultName = user.getEmail().split("@")[0];
                profile.setName(defaultName != null ? defaultName : "User");
                return jobSeekerService.updateProfile(profile);
            } catch (Exception ex) {
                System.err.println("CRITICAL: Failed to create profile: " + ex.getMessage());
                ex.printStackTrace();
                return null;
            }
        }
    }
}
