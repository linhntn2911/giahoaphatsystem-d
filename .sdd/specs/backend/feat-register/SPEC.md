# Đặc tả Tính năng: Đăng ký tài khoản Khách hàng (Register)
**Mã tính năng**: FE-REG  
**Đối tượng áp dụng**: Khách vãng lai (Guest)  

---

## 1. Mục tiêu (Goal)
Cho phép khách vãng lai đăng ký tài khoản khách hàng mới bằng cách nhập thông tin cá nhân, xác thực mã OTP gửi về email. Sau khi đăng ký thành công, hệ thống tự động đăng nhập và đưa người dùng về trang chủ.

---

## 2. Các trường thông tin yêu cầu
*   **Họ và tên (`fullName`)**: Không được để trống.
*   **Email (`email`)**: Định dạng email hợp lệ, duy nhất trong hệ thống.
*   **Số điện thoại (`phone`)**: Định dạng số điện thoại hợp lệ.
*   **Địa chỉ (`address`)**: Không được để trống.
*   **Tên đăng nhập (`userName`)**: Duy nhất trong hệ thống, tối thiểu 5 ký tự.
*   **Mật khẩu (`password`)**: 6 ký tự trở lên, 1 kí tự đặc biệt, 1 chữ viết Hoa
*   **Xác nhận mật khẩu (`confirmPassword`)**: Trùng khớp với mật khẩu.

---

## 3. Luồng nghiệp vụ chính (Normal Flow - Khớp 100% PDF)
1.  Khách vãng lai click nút **Đăng ký** trên website.
2.  Hệ thống hiển thị Form điền thông tin đăng ký.
3.  Khách điền đầy đủ các thông tin yêu cầu và nhấn nút **Submit**.
4.  Hệ thống validate thông tin (kiểm tra rỗng, định dạng email/sđt, trùng lặp email/username trong DB).
5.  Nếu thông tin hợp lệ:
    *   Hệ thống sinh một mã xác thực ngẫu nhiên **OTP gồm 6 chữ số** và gửi về email khách hàng (ở môi trường local sẽ ghi log/in ra console để test).
    *   Hệ thống lưu tạm dữ liệu đăng ký vào session và chuyển hướng người dùng sang trang **Xác thực OTP (`/verify-otp`)**.
6.  Người dùng nhập mã OTP nhận được vào ô input và nhấn **Xác nhận**.
7.  Hệ thống đối chiếu mã OTP:
    *   *Nếu khớp*: Băm mật khẩu bằng **bcrypt**, lưu bản ghi khách hàng mới vào Database, xóa dữ liệu tạm trong session, khởi tạo HTTP Session đăng nhập tự động cho khách hàng, điều hướng về Trang chủ.
    *   *Nếu sai*: Hiển thị lỗi thông báo *"Mã OTP không hợp lệ hoặc đã hết hạn"*.

---

## 4. Luồng thay thế (Alternative Flow - Đăng ký qua Google)
1.  Người dùng nhấn nút **Sign up with Google**.
2.  Hệ thống chuyển hướng người dùng tới trang chọn tài khoản Google OAuth2.
3.  Sau khi xác thực thành công, Google trả về thông tin (họ tên, email).
4.  Hệ thống kiểm tra email:
    *   Nếu email đã tồn tại: Tiến hành đăng nhập.
    *   Nếu email chưa tồn tại: Tạo mới tài khoản và đăng nhập tự động.

---

## 5. Xử lý Lỗi & Ràng buộc (Error Handling & Constraints)
*   **E1: Database is down**: Không thể hoàn thành đăng ký, hiển thị thông báo hệ thống bận.
*   **E2: Email system is down**: Không gửi được OTP, quy trình đăng ký dừng lại.
*   **E3: Email already used**: Báo lỗi trùng lặp email.
*   **E4: Wrong OTP code typed**: Báo lỗi sai mã xác thực OTP.
*   **E5: Google login fails**: Quay lại trang đăng ký và báo lỗi.
