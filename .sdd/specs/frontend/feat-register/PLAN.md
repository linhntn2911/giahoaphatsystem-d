# Kế hoạch triển khai giao diện (PLAN) - Đăng ký (Register UI)

Kế hoạch này chi tiết hóa việc xây dựng giao diện đăng ký tài khoản Khách hàng và giao diện Xác thực OTP bằng Thymeleaf.

---

## 1. Các file thay đổi/tạo mới

### [NEW] [register.html](file:///d:/SU26/giahoaphat/src/main/resources/templates/register.html)
- Thiết kế khung HTML cho form đăng ký, tích hợp nút đăng ký bằng Google.
- Tích hợp cú pháp Thymeleaf để hiển thị lỗi và giữ lại dữ liệu cũ.

### [NEW] [verify-otp.html](file:///d:/SU26/giahoaphat/src/main/resources/templates/verify-otp.html)
- Thiết kế card layout nhập mã OTP 6 số.

### [NEW] [register.css](file:///d:/SU26/giahoaphat/src/main/resources/static/css/pages/register.css)
- CSS cho cả form đăng ký và form OTP: card layout, hover, active, error styles.

### [NEW] [register.js](file:///d:/SU26/giahoaphat/src/main/resources/static/js/pages/register.js)
- JS xác thực form đăng ký.

### [NEW] [verify-otp.js](file:///d:/SU26/giahoaphat/src/main/resources/static/js/pages/verify-otp.js)
- JS xác thực form OTP và đếm ngược gửi lại mã.

---

## 2. Quy trình kiểm tra (Testing)
- **UI & Responsive Check**: Test trên mobile, tablet, desktop.
- **Form Register Validation**: Nhập lỗi để kiểm tra báo lỗi đỏ và ngăn chặn submit.
- **OTP Validation**: Nhập ký tự không phải số hoặc dưới 6 chữ số để kiểm tra chặn submit.
