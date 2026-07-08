package com.giahoaphat.feature.auth;

import com.giahoaphat.shared.dal.CustomerRepository;
import com.giahoaphat.shared.model.Customer;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${spring.mail.password}")
    private String mailPassword;

    public AuthService(CustomerRepository customerRepository, JavaMailSender mailSender) {
        this.customerRepository = customerRepository;
        this.mailSender = mailSender;
    }

    @Transactional(readOnly = true)
    public void validateRegistration(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không trùng khớp.");
        }

        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã được đăng ký trong hệ thống.");
        }

        if (customerRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");
        }
    }

    public String generateOTP() {
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpValue);
        
        System.out.println("==================================================");
        System.out.println("MÃ OTP CỦA BẠN LÀ: " + otp);
        System.out.println("==================================================");
        
        return otp;
    }

    @Transactional
    public Customer completeRegistration(RegisterRequest request) {
        // Validate double-check
        validateRegistration(request);

        // Hash password
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt(12));

        Customer customer = new Customer(
                request.getFullName(),
                request.getEmail(),
                request.getPhone(),
                request.getAddress(),
                hashedPassword,
                request.getUserName()
        );

        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer login(String usernameOrEmail, String password) {
        Optional<Customer> customerOpt = customerRepository.findByUserName(usernameOrEmail);
        if (customerOpt.isEmpty()) {
            customerOpt = customerRepository.findByEmail(usernameOrEmail);
        }

        if (customerOpt.isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không chính xác.");
        }

        Customer customer = customerOpt.get();
        if (!BCrypt.checkpw(password, customer.getPassword())) {
            throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không chính xác.");
        }

        return customer;
    }

    @Transactional(readOnly = true)
    public Customer verifyForgotPasswordEmail(String email) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);
        if (customerOpt.isEmpty()) {
            throw new IllegalArgumentException("Email không tồn tại trong hệ thống.");
        }
        return customerOpt.get();
    }

    @Transactional
    public void resetPassword(String email, String newPassword) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email không tồn tại trong hệ thống."));
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
        customer.setPassword(hashedPassword);
        customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public boolean isUsernameTaken(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        return customerRepository.findByUserName(username.trim()).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean isEmailTaken(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return customerRepository.findByEmail(email.trim()).isPresent();
    }

    public void sendOTPEmail(String email, String otp) {
        System.out.println("DEBUG MAIL SYSTEM CONFIG:");
        System.out.println("  - spring.mail.username (mailFrom): " + mailFrom);
        System.out.println("  - spring.mail.password (mailPassword): " + mailPassword);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom != null ? mailFrom : "linhntn291104@gmail.com");
            message.setTo(email.trim());
            message.setSubject("[Gia Hòa Phát] Mã xác thực OTP");
            message.setText("Chào bạn,\n\n"
                    + "Mã xác thực OTP của bạn là: " + otp + "\n"
                    + "Mã này có hiệu lực trong vòng 5 phút. Vui lòng không chia sẻ mã này với bất kỳ ai.\n\n"
                    + "Trân trọng,\n"
                    + "Gia Hòa Phát System");
            mailSender.send(message);
            System.out.println("OTP email sent successfully to " + email);
        } catch (Exception e) {
            System.err.println("Failed to send OTP email to " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
