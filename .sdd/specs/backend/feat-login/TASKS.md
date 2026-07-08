# Danh sách nhiệm vụ Backend (TASKS) - Tính năng Đăng nhập (Login)

Danh sách theo dõi tiến trình làm phần Backend cho tính năng Đăng nhập bằng Spring Boot.

---

## 1. Nghiệp vụ & Repositories
- [ ] `[MODIFY]` [AuthService.java](file:///d:/SU26/giahoaphat/src/main/java/com/giahoaphat/feature/auth/AuthService.java): Viết phương thức `login` thực hiện kiểm tra mật khẩu bcrypt.

---

## 2. Controller & Định tuyến
- [ ] `[MODIFY]` [AuthController.java](file:///d:/SU26/giahoaphat/src/main/java/com/giahoaphat/feature/auth/AuthController.java): Định nghĩa GET và POST mapping cho URL `/login`, thiết lập Session đăng nhập.

---

## 3. Giao diện & Tích hợp
- [ ] `[NEW]` [login.html](file:///d:/SU26/giahoaphat/src/main/resources/templates/login.html): Tạo giao diện Thymeleaf HTML cho form đăng nhập.

---

## 4. Kiểm thử & Nghiệm thu
- [ ] Viết unit test cho `AuthService.login`.
- [ ] Tiến hành khởi chạy ứng dụng và kiểm thử đăng nhập toàn diện từ trình duyệt.
