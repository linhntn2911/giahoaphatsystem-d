# Đặc tả Tính năng: Đăng nhập hệ thống (Login)
**Mã tính năng**: FE-LOG  
**Đối tượng áp dụng**: Khách hàng (Customer), Nhân viên (Staff), Quản lý (Manager), Quản trị viên (Admin)  

---

## 1. Mục tiêu (Goal)
Cho phép người dùng đã có tài khoản đăng nhập vào hệ thống bằng cách nhập tên đăng nhập và mật khẩu. Sau khi xác thực thành công, hệ thống lưu thông tin vào Session và điều hướng người dùng tới trang tương ứng với vai trò của họ.

---

## 2. Thông tin yêu cầu
*   **Tên đăng nhập / Email (`userName` / `email`)**: Không được để trống.
*   **Mật khẩu (`password`)**: Không được để trống.

---

## 3. Luồng nghiệp vụ chính (Normal Flow)
1.  Người dùng truy cập vào đường dẫn đăng nhập `/login`.
2.  Hệ thống hiển thị Form đăng nhập.
3.  Người dùng nhập tên đăng nhập và mật khẩu, nhấn nút **Đăng nhập**.
4.  Hệ thống thực hiện kiểm tra tính hợp lệ của dữ liệu đầu vào.
5.  Backend truy vấn CSDL:
    *   Tìm tài khoản Khách hàng (Customer) hoặc nhân viên trong bảng tương ứng khớp với username/email nhập vào.
6.  Hệ thống thực hiện kiểm tra mật khẩu bằng cách so sánh mật khẩu nhập vào với mật khẩu băm đã lưu (sử dụng **bcrypt**).
7.  Nếu mật khẩu chính xác:
    *   Thiết lập HTTP Session mới.
    *   Lưu thông tin người dùng vào Session (đối tượng User/Customer và quyền truy cập).
    *   Điều hướng người dùng về trang đích tương ứng:
        *   Khách hàng (Customer) -> Trang chủ `/`
        *   Nhân viên (Staff) -> DashBoard Nhân viên `/staff/dashboard` (hoặc tương tự)
        *   Quản lý/Admin -> DashBoard Admin `/admin/dashboard`
8.  Nếu thông tin không chính xác (sai tài khoản hoặc mật khẩu):
    *   Hiển thị thông báo lỗi chung *"Tên đăng nhập hoặc mật khẩu không chính xác"* để bảo mật (không báo cụ thể là sai trường nào).

---

## 4. Ràng buộc bảo mật (Security Constraints)
*   **Mã hóa**: So khớp mật khẩu sử dụng `BCrypt.checkpw`.
*   **Xử lý Session**: Khi đăng nhập thành công, hệ thống phải thực hiện hủy session cũ và tạo session mới (`request.getSession(true)`) để ngăn chặn lỗ hổng Session Fixation.
*   **Session Timeout**: Cấu hình thời gian hết hạn session mặc định (ví dụ: 30 phút).
