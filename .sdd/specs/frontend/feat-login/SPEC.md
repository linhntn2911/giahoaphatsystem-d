# Đặc tả Giao diện: Đăng nhập (Login UI)
**Mã tính năng**: FE-LOG-UI  
**Tác nhân**: Khách vãng lai (Guest)  

---

## 1. Thiết kế Giao diện & Trải nghiệm Người dùng (UI/UX)
*   **Phong cách**: Đồng bộ với trang Đăng ký (Register UI), thiết kế Card layout căn giữa màn hình với các góc bo tròn (`border-radius: 12px`) và hiệu ứng bóng đổ mịn.
*   **Kiểu chữ**: Outfit hoặc Inter, cỡ chữ rõ ràng, dễ đọc.
*   **Responsive**: Giao diện hiển thị sắc nét, co giãn tự động trên mọi màn hình di động, máy tính bảng và máy tính để bàn.

---

## 2. Các Thành phần trên Form Đăng nhập
Form bao gồm:
1.  **Tên đăng nhập / Email**: Input Text/Email, placeholder: *"Nhập tên đăng nhập hoặc email"*.
2.  **Mật khẩu**: Input Password, placeholder: *"Nhập mật khẩu"*.
3.  **Nút bấm**: Nút **Đăng nhập** với hiệu ứng hover và active.
4.  **Tùy chọn phụ**: Checkbox *"Ghi nhớ đăng nhập"*.
5.  **Liên kết bổ trợ**:
    *   Liên kết *"Quên mật khẩu?"*.
    *   Liên kết *"Chưa có tài khoản? Đăng ký ngay"* trỏ tới trang `/register`.

---

## 3. Xác thực phía Client (Client-side Validation)
Sử dụng Javascript thuần kiểm tra thông tin trước khi submit:
*   *Kiểm tra trống*: Cả hai trường Tên đăng nhập và Mật khẩu không được để trống.
*   *Hiển thị lỗi*: Hiển thị lỗi tức thời bằng chữ đỏ bên dưới ô input bị thiếu thông tin.
