# Kế hoạch triển khai giao diện (PLAN) - Đăng nhập (Login UI)

Kế hoạch này chi tiết hóa việc xây dựng giao diện đăng nhập cho hệ thống.

---

## 1. Các file thay đổi/tạo mới

### [NEW] [login.html](file:///d:/SU26/giahoaphat/src/main/resources/templates/login.html)
- Thiết kế layout HTML cho form đăng nhập sử dụng Thymeleaf.
- Tích hợp Thymeleaf to display error messages.

### [NEW] [login.css](file:///d:/SU26/giahoaphat/src/main/resources/static/css/pages/login.css)
- Xây dựng giao diện card đăng nhập căn giữa, bo góc, căn lề và phối màu đồng bộ với trang đăng ký.

### [NEW] [login.js](file:///d:/SU26/giahoaphat/src/main/resources/static/js/pages/login.js)
- Viết Javascript lắng nghe sự kiện `submit` của Form đăng nhập và validate client-side.

---

## 2. Quy trình kiểm tra (Testing)
- **UI & Responsive Check**: Kiểm tra trên Mobile, Tablet, Desktop.
- **Client-side Validation Test**: Bấm Đăng nhập khi để trống các ô dữ liệu để kiểm tra Javascript có hiển thị thông báo lỗi màu đỏ.
