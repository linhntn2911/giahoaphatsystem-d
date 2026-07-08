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

import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/api/auth/check-username")
    @ResponseBody
    public java.util.Map<String, Boolean> checkUsername(@RequestParam("username") String username) {
        boolean exists = authService.isUsernameTaken(username);
        java.util.Map<String, Boolean> response = new java.util.HashMap<>();
        response.put("exists", exists);
        return response;
    }

    @GetMapping("/api/auth/check-email")
    @ResponseBody
    public java.util.Map<String, Boolean> checkEmail(@RequestParam("email") String email) {
        boolean exists = authService.isEmailTaken(email);
        java.util.Map<String, Boolean> response = new java.util.HashMap<>();
        response.put("exists", exists);
        return response;
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
            String message = e.getMessage();
            if (message != null) {
                if (message.contains("Email")) {
                    result.rejectValue("email", "duplicate", message);
                } else if (message.contains("Tên đăng nhập")) {
                    result.rejectValue("userName", "duplicate", message);
                } else if (message.contains("Mật khẩu")) {
                    result.rejectValue("confirmPassword", "mismatch", message);
                } else {
                    model.addAttribute("globalError", message);
                }
            } else {
                model.addAttribute("globalError", "Đăng ký không hợp lệ.");
            }
            return "auth/register";
        }

        // Setup OTP
        String otp = authService.generateOTP();
        authService.sendOTPEmail(request.getEmail(), otp);
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

    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @RequestParam("email") String email,
            HttpSession session,
            Model model) {
        
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("errorEmail", "Email không được để trống");
            model.addAttribute("emailVal", email);
            return "auth/forgot-password";
        }

        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        if (!email.matches(emailRegex)) {
            model.addAttribute("errorEmail", "Email không đúng định dạng");
            model.addAttribute("emailVal", email);
            return "auth/forgot-password";
        }

        try {
            authService.verifyForgotPasswordEmail(email);
            String otp = authService.generateOTP();
            authService.sendOTPEmail(email, otp);
            session.setAttribute("forgotPasswordEmail", email);
            session.setAttribute("forgotPasswordOTP", otp);
            session.setAttribute("forgotPasswordOTPCreatedAt", System.currentTimeMillis());
            session.setAttribute("forgotPasswordOTPSentAt", System.currentTimeMillis());
            return "redirect:/forgot-password/verify";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorEmail", e.getMessage());
            model.addAttribute("emailVal", email);
            return "auth/forgot-password";
        }
    }

    @GetMapping("/forgot-password/verify")
    public String showForgotPasswordVerifyForm(HttpSession session) {
        if (session.getAttribute("forgotPasswordEmail") == null || session.getAttribute("forgotPasswordOTP") == null) {
            return "redirect:/forgot-password";
        }
        return "auth/forgot-password-verify";
    }

    @PostMapping("/forgot-password/verify")
    public String processForgotPasswordVerify(
            @RequestParam("otp") String otp,
            HttpSession session,
            Model model) {
        
        String sessionEmail = (String) session.getAttribute("forgotPasswordEmail");
        String sessionOtp = (String) session.getAttribute("forgotPasswordOTP");
        Long createdAt = (Long) session.getAttribute("forgotPasswordOTPCreatedAt");

        if (sessionEmail == null || sessionOtp == null) {
            return "redirect:/forgot-password";
        }

        if (createdAt == null || (System.currentTimeMillis() - createdAt) > 5 * 60 * 1000) {
            model.addAttribute("error", "Mã OTP đã hết hạn (hiệu lực trong 5 phút). Vui lòng gửi lại mã.");
            return "auth/forgot-password-verify";
        }

        if (otp == null || otp.trim().isEmpty()) {
            model.addAttribute("error", "Mã OTP không được để trống.");
            return "auth/forgot-password-verify";
        }

        if (!sessionOtp.equals(otp.trim())) {
            model.addAttribute("error", "Mã xác thực OTP không chính xác.");
            return "auth/forgot-password-verify";
        }

        session.setAttribute("forgotPasswordVerified", true);
        return "redirect:/forgot-password/reset";
    }

    @PostMapping("/forgot-password/resend")
    @ResponseBody
    public java.util.Map<String, Object> resendForgotPasswordOtp(HttpSession session) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        String email = (String) session.getAttribute("forgotPasswordEmail");
        if (email == null) {
            response.put("success", false);
            response.put("message", "Phiên làm việc đã hết hạn. Vui lòng quay lại nhập email.");
            return response;
        }

        Long lastSent = (Long) session.getAttribute("forgotPasswordOTPSentAt");
        if (lastSent != null && (System.currentTimeMillis() - lastSent) < 60000) {
            response.put("success", false);
            response.put("message", "Vui lòng đợi 60 giây trước khi yêu cầu gửi lại.");
            return response;
        }

        try {
            String otp = authService.generateOTP();
            authService.sendOTPEmail(email, otp);
            session.setAttribute("forgotPasswordOTP", otp);
            session.setAttribute("forgotPasswordOTPCreatedAt", System.currentTimeMillis());
            session.setAttribute("forgotPasswordOTPSentAt", System.currentTimeMillis());
            response.put("success", true);
            response.put("message", "Đã gửi lại mã OTP mới.");
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Gửi lại mã OTP thất bại: " + e.getMessage());
            return response;
        }
    }

    @GetMapping("/forgot-password/reset")
    public String showForgotPasswordResetForm(HttpSession session) {
        if (session.getAttribute("forgotPasswordEmail") == null || session.getAttribute("forgotPasswordVerified") == null) {
            return "redirect:/forgot-password";
        }
        return "auth/forgot-password-reset";
    }

    @PostMapping("/forgot-password/reset")
    public String processForgotPasswordReset(
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session,
            Model model) {
        
        String sessionEmail = (String) session.getAttribute("forgotPasswordEmail");
        Boolean verified = (Boolean) session.getAttribute("forgotPasswordVerified");

        if (sessionEmail == null || verified == null || !verified) {
            return "redirect:/forgot-password";
        }

        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("errorPassword", "Mật khẩu không được để trống.");
            return "auth/forgot-password-reset";
        }

        if (password.length() < 6 || !password.matches("^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$")) {
            model.addAttribute("errorPassword", "Mật khẩu phải từ 6 ký tự, gồm 1 chữ hoa và 1 ký tự đặc biệt.");
            return "auth/forgot-password-reset";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorConfirmPassword", "Mật khẩu xác nhận không khớp.");
            return "auth/forgot-password-reset";
        }

        try {
            authService.resetPassword(sessionEmail, password);
            session.removeAttribute("forgotPasswordEmail");
            session.removeAttribute("forgotPasswordOTP");
            session.removeAttribute("forgotPasswordVerified");
            model.addAttribute("successMessage", "Đổi mật khẩu thành công. Vui lòng đăng nhập.");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/forgot-password-reset";
        }
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
