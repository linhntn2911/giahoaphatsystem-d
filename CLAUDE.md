# CLAUDE.md — Gia Hoa Phat (GHP) Architecture Map & Guidelines

Tài liệu này chi tiết hóa bản đồ kiến trúc hệ thống, danh sách màn hình UI, các quyết định kiến trúc (ADR), bài học kinh nghiệm và Anti-patterns của hệ thống **Gia Hoa Phát (GHP)** với framework Spring Boot.

---

## 1. TỔNG QUAN KIẾN TRÚC & PHÂN TẦNG

Hệ thống áp dụng kiến trúc phân lớp tiêu chuẩn (**3-Layer Architecture**) sử dụng Spring Boot 3.1, Spring Data JPA, và Thymeleaf Template Engine.

### 1.1. Sơ đồ Kiến trúc Phân tầng
```
┌────────────────────────────────────────────────────────────────────────┐
│                        FRONTEND PORTAL (Thymeleaf/HTML/CSS/JS)         │
│  - Render giao diện HTML sử dụng các thuộc tính th: của Thymeleaf.     │
│  - Gọi controller qua URL Mappings.                                    │
│  - UX validation, UI state management.                                  │
└──────────────────────────────────┬─────────────────────────────────────┘
                                   │ (HTTP GET/POST / Thymeleaf Forms)
                                   ▼
┌────────────────────────────────────────────────────────────────────────┐
│                        BACKEND (Spring Boot 3.1)                       │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │  Controller Layer (@Controller / @RestController)                │  │
│  │  - Tiếp nhận HTTP Request, xử lý định tuyến và validate dữ liệu.   │  │
│  │  - Trả về Thymeleaf View Name hoặc ApiResponse JSON.               │  │
│  └───────────────────────────────┬──────────────────────────────────┘  │
│                                  │
│                                  ▼
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │  Service Layer (@Service)                                        │  │
│  │  - Xử lý business logic nghiệp vụ chính (tồn kho, đơn hàng, ship).│  │
│  │  - Quản lý Transaction tự động bằng @Transactional.                 │  │
│  └───────────────────────────────┬──────────────────────────────────┘  │
│                                  │
│                                  ▼
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │  Repository Layer (@Repository)                                  │  │
│  │  - Giao tiếp CSDL thông qua interface JpaRepository của Spring.   │  │
│  └──────────────────────────────────────────────────────────────────┘  │
└──────────────────────────────────┬─────────────────────────────────────┘
                                   │ (Spring Data JPA / Hibernate)
                                   ▼
┌────────────────────────────────────────────────────────────────────────┐
│                      DATABASE & SERVICES EXTERNAL                      │
│   - MySQL Server 8.0 - Cơ sở dữ liệu quan hệ chính.                    │
│   - VNPay/MoMo Webhook API Listener (Xử lý thanh toán).                │
│   - API GHN/Viettel Post (Giao vận).                                    │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 2. CẤU TRÚC THƯ MỤC CODE THỰC TẾ

```
d:\SU26\giahoaphat\
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── giahoaphat/
│       │           ├── GiaHoaPhatApplication.java # Spring Boot Main class
│       │           ├── shared/         # Tầng dùng chung toàn hệ thống
│       │           │   ├── model/      # Các thực thể dữ liệu Spring Data JPA
│       │           │   ├── dal/        # Các Spring Data JPA Repositories
│       │           │   ├── config/     # Các cấu hình Spring Boot
│       │           │   └── dto/        # DTOs dùng chung
│       │           └── feature/        # Phân lát cắt nghiệp vụ
│       │               └── auth/       # Phân hệ Xác thực (Login, Register...)
│       │                   ├── AuthController.java
│       │                   ├── AuthService.java
│       │                   └── RegisterRequest.java
│       └── resources/
│           ├── templates/              # Thư mục giao diện Thymeleaf (HTML)
│           │   ├── login.html
│           │   ├── register.html
│           │   └── verify-otp.html
│           ├── static/                 # Thư mục tài nguyên tĩnh
│           │   ├── css/
│           │   └── js/
│           └── application.properties # File cấu hình kết nối CSDL, Server Port
├── pom.xml                             # Cấu hình dependency Maven
└── .sdd/                               # Quy tắc phát triển và đặc tả SDD
    ├── constitution.md                 # Hiến pháp dự án GHP
    ├── sdd-workflow-guide.md           # Hướng dẫn quy trình phát triển
    └── specs/
        └── SPEC.md                     # Tài liệu đặc tả nguồn chân lý
```

---

## 3. CORE USE CASES & LUỒNG NGHIỆP VỤ

*   **Duyệt & Tìm kiếm Sản phẩm (FE-01)**: Khách hàng xem danh mục, lọc theo nguyên liệu/thiết bị từ MySQL thông qua các Repository.
*   **Đặt hàng & Thanh toán (FE-02)**: Thêm vào giỏ hàng -> Chọn COD hoặc ví -> Thực hiện lưu đơn hàng và chi tiết đơn hàng qua JPA.
*   **Kiểm kho & Cảnh báo (FE-03)**: Nhân viên xem tồn kho tại các chi nhánh cửa hàng. Nightly Cron Job quét tìm hàng sắp hết để thông báo.
*   **Quy trình Xử lý Đơn hàng (FE-04)**: Staff xác nhận đơn -> Đổi trạng thái. Nếu đơn bị hủy, tiến trình tự động hoàn số lượng về `Inventory`.

---

## 4. LESSONS LEARNED & SYSTEM SAFEGUARDS (Phòng ngừa lỗi CSDL)

*   **Sử dụng Parameterized Queries mặc định**: Spring Data JPA tự động sử dụng prepared statements cho mọi truy vấn giúp ngăn chặn hoàn toàn SQL Injection. Cấm viết các câu lệnh SQL native nối chuỗi thô.
*   **Quản lý Transactions**: Sử dụng `@Transactional(rollbackFor = Exception.class)` ở lớp Service cho các phương thức có nhiều thao tác ghi dữ liệu phức tạp.
*   **Soft Delete**: Không xóa vật lý dữ liệu giao dịch quan trọng. Sử dụng cột trạng thái hoặc cờ xóa để ẩn bản ghi khỏi giao diện.

---

## 5. ANTI-PATTERNS (Các mẫu thiết kế tồi cần tránh)

*   ❌ **Hardcode Credentials**: Cấm viết trực tiếp mật khẩu database hoặc mã bảo mật vào code. Cấu hình hoàn toàn ở `application.properties`.
*   ❌ **Trả Entity trực tiếp ra API bên ngoài**: Trả JPA Entity trực tiếp ra API dễ gây lỗi vòng lặp tuần tự hóa (circular serialization) và làm lộ cấu trúc CSDL. Hãy sử dụng DTO.
*   ❌ **Thực hiện business logic tài chính/kho ở Controller**: Phải đưa toàn bộ logic nghiệp vụ, tính toán, và kiểm tra quyền vào Service layer.
