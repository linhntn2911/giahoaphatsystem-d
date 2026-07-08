# Kế hoạch triển khai Backend (PLAN) - Tính năng Đăng nhập (Login)

Kế hoạch triển khai phần Backend của tính năng Đăng nhập bằng Spring Boot.

---

## 1. Các file thay đổi/tạo mới

### [MODIFY] [AuthController.java](file:///d:/SU26/giahoaphat/src/main/java/com/giahoaphat/feature/auth/AuthController.java)
- Thêm mapping GET và POST cho `/login`.
- Nhận thông tin đăng nhập, gọi `AuthService.login`, lưu thông tin vào HttpSession nếu thành công và redirect về trang chủ `/` (hoặc dashboard).

### [MODIFY] [AuthService.java](file:///d:/SU26/giahoaphat/src/main/java/com/giahoaphat/feature/auth/AuthService.java)
- Bổ sung phương thức `login(String usernameOrEmail, String password)`:
  - Truy vấn database thông qua `CustomerRepository`.
  - Đối chiếu mật khẩu bằng `BCrypt.checkpw`.
  - Trả về Customer nếu hợp lệ hoặc ném ngoại lệ nếu sai thông tin.

### [NEW] [login.html](file:///d:/SU26/giahoaphat/src/main/resources/templates/login.html)
- Viết giao diện đăng nhập sử dụng cú pháp Thymeleaf HTML.

---

## 2. Kế hoạch Xác minh (Verification Plan)
- **Unit Test**: Viết unit test cho `AuthService.login` kiểm thử các luồng đúng/sai.
- **Manual Test**: Chạy ứng dụng, truy cập `/login`, nhập tài khoản đã tạo qua luồng register để kiểm tra đăng nhập thành công.
