package com.giahoaphat.feature.auth;

import com.giahoaphat.shared.model.Customer;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("currentUser");
        if (customer != null) {
            model.addAttribute("customer", customer);
        }
        return "home"; // home.html will show the store homepage
    }

    @GetMapping("/register")
    public String showRegisterForm(HttpSession session, Model model) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/";
        }
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult result,
            HttpSession session,
            Model model) {
        
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            authService.validateRegistration(request);
        } catch (IllegalArgumentException e) {
            model.addAttribute("globalError", e.getMessage());
            return "auth/register";
        }

        // Setup OTP
        String otp = authService.generateOTP();
        session.setAttribute("tempRegisterRequest", request);
        session.setAttribute("registerOTP", otp);

        return "redirect:/verify-otp";
    }

    @GetMapping("/verify-otp")
    public String showOtpForm(HttpSession session) {
        if (session.getAttribute("tempRegisterRequest") == null) {
            return "redirect:/register";
        }
        return "auth/verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam("otp") String otp,
            HttpSession session,
            Model model) {
        
        RegisterRequest tempRequest = (RegisterRequest) session.getAttribute("tempRegisterRequest");
        String sessionOtp = (String) session.getAttribute("registerOTP");

        if (tempRequest == null || sessionOtp == null) {
            return "redirect:/register";
        }

        if (!sessionOtp.equals(otp.trim())) {
            model.addAttribute("error", "Mã xác thực OTP không chính xác.");
            return "auth/verify-otp";
        }

        try {
            Customer customer = authService.completeRegistration(tempRequest);
            // Auto login
            session.setAttribute("currentUser", customer);
            // Clean up session
            session.removeAttribute("tempRegisterRequest");
            session.removeAttribute("registerOTP");
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/verify-otp";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/";
        }
        return "auth/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(HttpSession session) {
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/";
        }
        return "auth/forgot-password";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam("usernameOrEmail") String usernameOrEmail,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        
        if (usernameOrEmail.trim().isEmpty() || password.trim().isEmpty()) {
            model.addAttribute("error", "Tên đăng nhập và mật khẩu không được để trống.");
            return "auth/login";
        }

        try {
            Customer customer = authService.login(usernameOrEmail, password);
            session.setAttribute("currentUser", customer);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
