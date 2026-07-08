# Danh sách nhiệm vụ Backend (TASKS) - Tính năng Đăng ký (Register)

Danh sách theo dõi tiến trình làm phần Backend cho tính năng Đăng ký bằng Spring Boot.

---

## 1. Cấu hình & Khởi tạo dự án
- [x] `[MODIFY]` [pom.xml](file:///d:/SU26/giahoaphat/pom.xml): Thêm Spring Boot parent, starters và thiết lập sourceDirectory.
- [x] `[NEW]` [application.properties](file:///d:/SU26/giahoaphat/src/backend/main/resources/application.properties): Cấu hình MySQL datasource và Hibernate.
- [x] `[NEW]` [GiaHoaPhatApplication.java](file:///d:/SU26/giahoaphat/src/backend/main/java/com/giahoaphat/GiaHoaPhatApplication.java): Tạo class khởi động Spring Boot.

---

## 2. Mô hình & Data Access Layer (DAL)
- [x] `[NEW]` [Customer.java](file:///d:/SU26/giahoaphat/src/backend/main/java/com/giahoaphat/shared/model/Customer.java): Tạo JPA Entity.
- [x] `[NEW]` [CustomerRepository.java](file:///d:/SU26/giahoaphat/src/backend/main/java/com/giahoaphat/shared/dal/CustomerRepository.java): Tạo Repository kế thừa JpaRepository.

---

## 3. Nghiệp vụ & Controller (Auth Logic & Controller)
- [x] `[NEW]` [RegisterRequest.java](file:///d:/SU26/giahoaphat/src/backend/main/java/com/giahoaphat/feature/auth/RegisterRequest.java): Tạo DTO đăng ký.
- [x] `[NEW]` [AuthService.java](file:///d:/SU26/giahoaphat/src/backend/main/java/com/giahoaphat/feature/auth/AuthService.java): Triển khai service xử lý logic sinh OTP và băm mật khẩu.
- [x] `[NEW]` [AuthController.java](file:///d:/SU26/giahoaphat/src/backend/main/java/com/giahoaphat/feature/auth/AuthController.java): Tạo Controller định tuyến `/register` và `/verify-otp`.

---

## 4. Giao diện & Deployment
- [x] `[NEW]` [register.html](file:///d:/SU26/giahoaphat/src/frontend/templates/auth/register.html): Xây dựng giao diện đăng ký Thymeleaf.
- [x] `[NEW]` [verify-otp.html](file:///d:/SU26/giahoaphat/src/frontend/templates/auth/verify-otp.html): Xây dựng giao diện nhập OTP Thymeleaf.

---

## 5. Kiểm thử & Nghiệm thu
- [x] Viết test cases kiểm chứng `AuthService`.
- [x] Khởi động Spring Boot, thực hiện đăng ký qua giao diện và kiểm tra CSDL.
