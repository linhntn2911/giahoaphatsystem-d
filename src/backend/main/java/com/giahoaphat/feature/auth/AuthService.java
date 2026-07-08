package com.giahoaphat.feature.auth;

import com.giahoaphat.shared.dal.CustomerRepository;
import com.giahoaphat.shared.model.Customer;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;

    public AuthService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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
}
