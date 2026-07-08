package com.giahoaphat.feature.auth;

import com.giahoaphat.shared.dal.CustomerRepository;
import com.giahoaphat.shared.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateRegistration_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("Nguyen Van A");
        request.setUserName("testuser");
        request.setEmail("test@gmail.com");
        request.setPhone("0912345678");
        request.setAddress("Ha Noi");
        request.setPassword("P@ssword123");
        request.setConfirmPassword("P@ssword123");

        when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.findByUserName(request.getUserName())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> authService.validateRegistration(request));
    }

    @Test
    public void testValidateRegistration_PasswordMismatch() {
        RegisterRequest request = new RegisterRequest();
        request.setPassword("P@ssword123");
        request.setConfirmPassword("differentPassword");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            authService.validateRegistration(request)
        );
        assertEquals("Mật khẩu xác nhận không trùng khớp.", exception.getMessage());
    }

    @Test
    public void testLogin_Success() {
        String username = "testuser";
        String plainPassword = "P@ssword123";
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));

        Customer mockCustomer = new Customer();
        mockCustomer.setUserName(username);
        mockCustomer.setPassword(hashedPassword);

        when(customerRepository.findByUserName(username)).thenReturn(Optional.of(mockCustomer));

        Customer result = authService.login(username, plainPassword);
        assertNotNull(result);
        assertEquals(username, result.getUserName());
    }

    @Test
    public void testLogin_Failure() {
        String username = "testuser";
        when(customerRepository.findByUserName(username)).thenReturn(Optional.empty());
        when(customerRepository.findByEmail(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            authService.login(username, "wrongpass")
        );
        assertEquals("Tên đăng nhập hoặc mật khẩu không chính xác.", exception.getMessage());
    }
}
