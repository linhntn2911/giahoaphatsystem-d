# Cấu hình AI Agent - Dự án Gia Hoa Phát (GHP)

Tài liệu này chứa các chỉ thị hành vi và quy tắc bắt buộc cho AI Agent khi lập trình trong không gian làm việc này.

---

## 1. Quy trình Phát triển dựa trên Đặc tả (Spec-Driven Development - SDD)
Khi được yêu cầu phát triển bất kỳ tính năng nào, bạn bắt buộc phải tuân theo quy trình 5 bước sau:

1. **Đọc Đặc tả (Specify)**: Luôn tham chiếu file đặc tả chính tại [SPEC.md](file:///d:/SU26/giahoaphat/.sdd/specs/SPEC.md) làm nguồn chân lý duy nhất. Không tự ý phỏng đoán nghiệp vụ.
2. **Lập Kế hoạch (Plan)**: 
   - Trước khi sửa đổi hoặc tạo bất kỳ file code nào, bạn phải tạo hoặc cập nhật file kế hoạch triển khai `implementation_plan.md` trong thư mục artifacts của cuộc hội thoại.
   - Kế hoạch phải liệt kê rõ ràng các file được tạo mới `[NEW]`, chỉnh sửa `[MODIFY]`, hoặc xóa bỏ `[DELETE]`.
   - **Bắt buộc**: Dừng lại và yêu cầu người dùng phê duyệt kế hoạch bằng cách nhấn nút **Proceed** hoặc gõ "Proceed" trong chat.
3. **Danh sách Nhiệm vụ (Task Checklist)**:
   - Sau khi kế hoạch được phê duyệt, tạo hoặc cập nhật file danh sách nhiệm vụ `task.md` trong thư mục artifacts.
   - Cập nhật trạng thái của các đầu việc theo thời gian thực: `[ ]` (chưa làm), `[/]` (đang làm), `[x]` (đã hoàn thành).
4. **Viết Code & Kiểm thử (Implement)**:
   - Tiến hành viết code theo kế hoạch và danh sách nhiệm vụ.
   - Viết các test case/unit test tương ứng và chạy thử nghiệm tự động.
5. **Nghiệm thu (Walkthrough)**:
   - Tạo hoặc cập nhật file `walkthrough.md` trong thư mục artifacts để tổng kết kết quả, các file đã thay đổi, các kết quả chạy test và hướng dẫn chạy thử nghiệm thực tế.

---

## 2. Quy định Công nghệ & Kiến trúc
* **Ngôn ngữ**: Java 17 LTS
* **Framework**: Jakarta Servlet API 6.0 (không dùng Spring Boot trừ khi được yêu cầu rõ ràng)
* **Giao diện**: JSP (Jakarta Server Pages) sử dụng JSTL cho các vòng lặp, logic hiển thị. CSS thuần đặt trong `/webapp/static/css`, JS thuần đặt trong `/webapp/static/js`.
* **Kết nối CSDL**: Sử dụng JDBC trực tiếp (Driver: `com.mysql.cj.jdbc.Driver`).
* **Thông tin kết nối cục bộ (Đã kiểm tra kết nối thành công)**:
  - **JDBC URL**: `jdbc:mysql://localhost:3306/gia_hoa_phat?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`
  - **Username**: `root`
  - **Password**: `123`
