# 📜 CONSTITUTION.md — Gia Hoa Phat (GHP)

**Ngày phê duyệt:** 2026-07-08 | **Nhóm:** SWD392-G2 | **Phiên bản:** 2.0

> **QUY TẮC TỐI THƯỢNG:** Mọi thay đổi đối với tài liệu này đều yêu cầu sự nhất trí (100% đồng thuận) của toàn bộ nhóm. Tất cả specs, features, và code phải tuân thủ nghiêm ngặt các quy tắc dưới đây.

---

## ĐIỀU 1 — STACK CÔNG NGHỆ (Không thể thay đổi)

| Tầng | Công nghệ | Ghi chú / Quy định |
| :--- | :--- | :--- |
| **Backend** | Java 17+ / Spring Boot 3.1+ | Sử dụng Spring Data JPA, Web MVC, Maven quản lý dependency. |
| **Frontend** | Thymeleaf, HTML, CSS, JS thuần | Chỉ phục vụ trang/template. Gọi REST API cho data động khi cần. |
| **Database** | MySQL | Sử dụng kết nối JPA/Hibernate tự động quản lý kết nối. |
| **Auth** | Session-based / Spring Session | Sử dụng HTTP Session để duy trì trạng thái đăng nhập của người dùng. |
| **Testing** | JUnit 5 / Spring Boot Test | Viết unit/integration tests cho Controller, Service, Repositories. |
| **Styling** | Vanilla CSS + Tailwind | Tailwind CSS (CDN) và hệ thống Design System trong `DESIGN.md`. |

---

## ĐIỀU 2 — TIÊU CHUẨN CODE (Coding Standards)

### 2.1. Định dạng & Linting
*   **Frontend**: Cấu hình Prettier/ESLint cho Javascript và CSS.
*   **Backend**: Cấu hình Format chuẩn của dự án trong IDE (IntelliJ IDEA).

### 2.2. Giới hạn độ dài (Enforcement Limits)
| Chỉ số | Giới hạn tối đa | Quy định |
|---|---|---|
| **Dòng code mỗi Method/Function** | **40 dòng** | Bắt buộc phải refactor, tách nhỏ nếu vượt quá. |
| **Dòng code mỗi File** | **300 dòng** | Phân rã Class/Component nếu vượt quá kích thước này. |
| **Kích thước mỗi Pull Request (PR)** | **400 dòng** | PR lớn hơn phải được chia nhỏ thành các nhánh con. |

### 2.3. Nguyên tắc Comments & Tài liệu hóa
*   Chỉ viết comment để giải thích **TẠI SAO** (Why - lý do thiết kế, quyết định đặc biệt) chứ **KHÔNG** giải thích **LÀM GÌ** (What - mô tả lại cú pháp lệnh).
*   Tuyệt đối không commit code chứa `console.log()` hoặc `System.out.println()`. Sử dụng Slf4j Logger của Spring Boot (`@Slf4j` hoặc `LoggerFactory`).

### 2.4. Tách biệt rõ ràng Frontend / Backend
*   **Backend chịu trách nhiệm TOÀN BỘ**: Business logic (ví dụ: tính toán tồn kho, cập nhật trạng thái giao vận), Phân quyền & Authorization, Validation nghiệp vụ (validate input, check điều kiện ví).
*   **Frontend CHỈ được phép**: Render giao diện Thymeleaf HTML, Quản lý UI state và các tương tác UX.

---

## ĐIỀU 3 — CHÍNH SÁCH BẢO MẬT & RÀNG BUỘC CSDL

### 3.1. Xác thực & Phân quyền (Auth & AuthZ)
*   **Băm mật khẩu**: Sử dụng `bcrypt` với cost rate >= 12.
*   **Quản lý Session**: Khi đăng nhập thành công, hệ thống phải thực hiện hủy session cũ và tạo session mới (`request.getSession(true)`) để ngăn chặn Session Fixation.

### 3.2. Bảo vệ dữ liệu & JPA
*   Sử dụng Spring Data JPA Repositories để tự động hóa parameterized queries, ngăn chặn SQL Injection. Cấm nối chuỗi SQL thủ công.
*   Validate dữ liệu đầu vào bằng `jakarta.validation.constraints` trên DTO ở REST Controller.

### 3.3. Ràng buộc Nghiệp vụ Kho & Đơn hàng
*   **Soft Delete**: Không xóa vật lý dữ liệu giao dịch quan trọng. Sử dụng cột trạng thái hoặc cờ xóa để ẩn bản ghi khỏi giao diện.
*   **Quản lý Transaction**: Mọi tác vụ thay đổi số dư ví hoặc tồn kho phải được thực hiện trong `@Transactional` của Spring.

---

## ĐIỀU 4 — QUY TRÌNH PHỐI HỢP VỚI AI AGENT

1.  **Đọc trước khi làm**: Trước khi bắt đầu thực hiện bất kỳ nhiệm vụ lập trình nào, AI Agent bắt buộc phải đọc qua các tài liệu cốt lõi: `.sdd/constitution.md` và `.agents/AGENTS.md`.
2.  **Đặc tả trước khi Code (Specify First)**: Tuyệt đối không viết code khi chưa có file spec tương ứng trong `.sdd/specs/` được duyệt làm nguồn chân lý duy nhất.
3.  **Lập kế hoạch (Plan Review)**: AI Agent phải đề xuất Implementation Plan chi tiết. Lập trình viên (Con người) phải review và duyệt kế hoạch này trước khi AI tiến hành code.
4.  **Bảo toàn di sản**: AI Agent không được tự ý xóa hoặc thay đổi các đoạn code/comments hiện có trừ khi đó là yêu cầu trực tiếp của task.
