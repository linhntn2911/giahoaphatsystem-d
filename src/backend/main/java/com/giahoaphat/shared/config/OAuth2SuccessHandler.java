package com.giahoaphat.shared.config;

import com.giahoaphat.shared.dal.CustomerRepository;
import com.giahoaphat.shared.model.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final CustomerRepository customerRepository;

    public OAuth2SuccessHandler(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        if (email == null || email.trim().isEmpty()) {
            response.sendRedirect("/login?error=Google account does not provide email.");
            return;
        }

        Optional<Customer> customerOpt = customerRepository.findByEmail(email.trim());
        Customer customer;
        if (customerOpt.isPresent()) {
            customer = customerOpt.get();
        } else {
            // Auto register Google user
            String baseUsername = email.split("@")[0];
            String username = baseUsername;
            int count = 1;
            while (customerRepository.findByUserName(username).isPresent()) {
                username = baseUsername + count;
                count++;
            }

            // Generate secure random dummy password
            String dummyPassword = UUID.randomUUID().toString();
            String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(dummyPassword, org.mindrot.jbcrypt.BCrypt.gensalt(12));

            customer = new Customer(
                    name != null ? name : "Google User",
                    email.trim(),
                    null, // phone is null
                    null, // address is null
                    hashedPassword,
                    username
            );
            customer = customerRepository.save(customer);
        }

        // Set session user attribute
        HttpSession session = request.getSession();
        session.setAttribute("currentUser", customer);

        response.sendRedirect("/");
    }
}
