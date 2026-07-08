# Đặc tả Giao diện: Đăng ký tài khoản Khách hàng (Register UI)
**Mã tính năng**: FE-REG-UI  
**Tác nhân**: Khách vãng lai (Guest)  

---

## 1. Thiết kế Giao diện & Trải nghiệm Người dùng (UI/UX)
*   **Form Đăng ký (`/register`)**: Card layout bo tròn góc (`border-radius: 12px`), căn giữa, hỗ trợ responsive trên di động và máy tính.
*   **Trang Xác thực OTP (`/verify-otp`)**: Giao diện Card nhỏ gọn tối giản, hiển thị dòng trạng thái: *"Chúng tôi đã gửi mã xác thực 6 chữ số tới email của bạn. Vui lòng kiểm tra."* cùng với ô nhập mã OTP và nút gửi lại mã.

---

## 2. Các Thành phần trên Form
### 2.1. Form Đăng ký (`register.jsp`)
1.  **Họ và tên**: Input Text.
2.  **Email**: Input Email.
3.  **Số điện thoại**: Input Tel.
4.  **Địa chỉ**: Input Text.
5.  **Tên đăng nhập**: Input Text.
6.  **Mật khẩu**: Input Password.
7.  **Xác nhận mật khẩu**: Input Password.
8.  **Nút Đăng ký** & **Liên kết Đăng nhập**.
9.  **Nút Đăng ký bằng Google (Google Sign-up)**.

### 2.2. Form Xác thực OTP (`verify-otp.jsp`)
1.  **Mã OTP**: Ô nhập gồm 6 ký tự số.
2.  **Nút Xác nhận**: Nút submit gửi OTP để verify.
3.  **Nút Gửi lại mã (Resend OTP)**: Có bộ đếm ngược thời gian (Countdown 60s) trước khi cho phép bấm gửi lại để tránh spam.

---

## 3. Xác thực phía Client (Client-side Validation)
*   **Tại trang đăng ký**: Validate định dạng email, sđt Việt Nam, độ dài mật khẩu (>=6), so khớp mật khẩu và check trống.
*   **Tại trang verify OTP**: Chỉ cho phép nhập ký tự số, độ dài đúng 6 chữ số.
