package com.rev.app.service.impl;

import com.rev.app.entity.Resume;
import com.rev.app.entity.JobSeekerProfile;
import com.rev.app.repository.ResumeRepository;
import com.rev.app.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Override
    public Resume getResumeBySeeker(JobSeekerProfile seeker) {
        return resumeRepository.findByJobSeeker(seeker).orElse(null);
    }

    @Override
    public Resume saveResume(Resume resume) {
        return resumeRepository.save(resume);
    }

    @Override
    public String uploadResume(JobSeekerProfile seeker, MultipartFile file) {
        if (file.isEmpty())
            return null;

        // Validation: Size < 2MB
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new RuntimeException("File size exceeds 2MB limit");
        }

        // Validation: Format (.pdf, .docx)
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.toLowerCase().endsWith(".pdf")
                && !originalFilename.toLowerCase().endsWith(".docx"))) {
            throw new RuntimeException("Only PDF and DOCX formats are allowed");
        }

        try {
            String uploadDir = "uploads/resumes/";
            File dir = new File(uploadDir);
            if (!dir.exists())
                dir.mkdirs();

            String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
            Path filePath = Paths.get(uploadDir + fileName);
            Files.copy(file.getInputStream(), filePath);

            Resume resume = getResumeBySeeker(seeker);
            if (resume == null) {
                resume = new Resume();
                resume.setJobSeeker(seeker);
            }

            // Store the relative web path
            resume.setResumePath("/resumes/" + fileName);
            resumeRepository.save(resume);

            return resume.getResumePath();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload resume: " + e.getMessage());
        }
    }
}
