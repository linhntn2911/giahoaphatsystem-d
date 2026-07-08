# Kế hoạch triển khai Backend (PLAN) - Tính năng Đăng ký (Register)

Kế hoạch này chi tiết hóa việc triển khai Backend cho tính năng Đăng ký tài khoản Khách hàng bằng Spring Boot framework theo cấu trúc chia tách MMO.

---

## 1. Các file thay đổi/tạo mới

### [MODIFY] [pom.xml](file:///d:/SU26/giahoaphat/pom.xml)
- Cấu hình Parent và Dependencies: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-thymeleaf`, `spring-boot-starter-validation`, `mysql-connector-j`, `jbcrypt`.
- Thiết lập ánh xạ nguồn (`src/backend/main/java`) và tài nguyên (`src/frontend`).

### [NEW] [application.properties](file:///d:/SU26/giahoaphat/src/backend/main/resources/application.properties)
- Cấu hình kết nối MySQL và JPA Hibernate `ddl-auto=update`.

### [NEW] [GiaHoaPhatApplication.java](file:///d:/SU26/giahoaphat/src/backend/main/java/com/giahoaphat/GiaHoaPhatApplication.java)
- Khởi động Spring Boot Application.

### [NEW] [Customer.java](file:///d:/SU26/giahoaphat/src/backend/main/java/com/giahoaphat/shared/model/Customer.java)
- Lớp JPA Entity ánh xạ bảng `customer`.

### [NEW] [CustomerRepository.java](file:///d:/SU26/giahoaphat/src/backend/main/java/com/giahoaphat/shared/dal/CustomerRepository.java)
- Spring Data JPA Repository giao tiếp CSDL: `save`, `findByEmail`, `findByUserName`.

### [NEW] [RegisterRequest.java](file:///d:/SU26/giahoaphat/src/backend/main/java/com/giahoaphat/feature/auth/RegisterRequest.java)
- DTO chứa dữ liệu submit đăng ký kèm theo các validation constraint (`@NotBlank`, `@Email`, `@Size`).

### [NEW] [AuthService.java](file:///d:/SU26/giahoaphat/src/backend/main/java/com/giahoaphat/feature/auth/AuthService.java)
- `@Service` nghiệp vụ xác thực dữ liệu:
  - Sinh OTP, in OTP ra Console để giả lập gửi Email, lưu dữ liệu đăng ký tạm thời vào HTTP session.
  - `verifyOTP(...)`: So khớp OTP, băm mật khẩu qua BCrypt, lưu tài khoản vào Database thông qua `CustomerRepository`.

### [NEW] [AuthController.java](file:///d:/SU26/giahoaphat/src/backend/main/java/com/giahoaphat/feature/auth/AuthController.java)
- `@Controller` định tuyến các URL `/register` (GET/POST) và `/verify-otp` (GET/POST).

### [NEW] [register.html](file:///d:/SU26/giahoaphat/src/frontend/templates/auth/register.html) & [verify-otp.html](file:///d:/SU26/giahoaphat/src/frontend/templates/auth/verify-otp.html)
- Viết giao diện Thymeleaf HTML.

---

## 2. Kế hoạch Xác minh (Verification Plan)
- **Unit Test**: Viết test cases kiểm chứng `AuthService` bằng Spring Boot Test.
- **Manual Test**: Khởi động server, truy cập `/register`, nhập thông tin, xem OTP trên Console, nhập OTP để hoàn thành đăng ký.
