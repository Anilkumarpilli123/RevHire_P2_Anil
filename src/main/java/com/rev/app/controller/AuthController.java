package com.rev.app.controller;

import com.rev.app.dto.SignupRequest;
import com.rev.app.service.AuthService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("signupRequest") SignupRequest signupRequest,
            BindingResult result, Model model) {
        logger.info("Registration attempt for email: {}, role: {}", signupRequest.getEmail(), signupRequest.getRole());
        if (result.hasErrors()) {
            logger.warn("Validation errors: {}", result.getAllErrors());
            return "register";
        }
        try {
            authService.registerUser(signupRequest);
            logger.info("Registration successful for: {}", signupRequest.getEmail());
            return "redirect:/login?registered=true";
        } catch (RuntimeException e) {
            logger.error("Registration failed for {}: {}", signupRequest.getEmail(), e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm(Model model) {
        model.addAttribute("forgotPasswordRequest", new com.rev.app.dto.ForgotPasswordRequest());
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @Valid @ModelAttribute("forgotPasswordRequest") com.rev.app.dto.ForgotPasswordRequest forgotPasswordRequest,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "forgot-password";
        }
        try {
            authService.initiatePasswordReset(forgotPasswordRequest.getEmail());
            model.addAttribute("message", "Password reset link has been sent to your email.");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(String token, Model model) {
        if (!authService.validatePasswordResetToken(token)) {
            model.addAttribute("error", "Invalid or expired reset token.");
            model.addAttribute("forgotPasswordRequest", new com.rev.app.dto.ForgotPasswordRequest());
            return "forgot-password";
        }
        com.rev.app.dto.ResetPasswordRequest request = new com.rev.app.dto.ResetPasswordRequest();
        request.setToken(token);
        model.addAttribute("resetPasswordRequest", request);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @Valid @ModelAttribute("resetPasswordRequest") com.rev.app.dto.ResetPasswordRequest resetRequest,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            model.addAttribute("token", resetRequest.getToken());
            return "reset-password";
        }
        try {
            authService.resetPassword(resetRequest.getToken(), resetRequest.getNewPassword());
            return "redirect:/login?resetSuccess=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", resetRequest.getToken());
            return "reset-password";
        }
    }
}
