# Class Diagram — Đăng ký tài khoản (Register)
> Chuẩn UML — Section 2.4, Figure 2.3 & 2.4
> Phiên bản v4 — đã sửa 4 lỗi so với v3

---

## Class Diagram

```mermaid
classDiagram
    direction TB

    %% ─────────────────────────────────────────
    %% RegisterRequest (DTO)
    %% ─────────────────────────────────────────
    class RegisterRequest {
        - fullName : String
        - email : String
        - phone : String
        - address : String
        - userName : String
        - password : String
        - confirmPassword : String
    }

    %% ─────────────────────────────────────────
    %% AuthController
    %% ─────────────────────────────────────────
    class AuthController {
        - authService : AuthService
        + showRegisterForm() : String
        + processRegister(request : RegisterRequest) : String
        + showOtpForm() : String
        + verifyOtp(otp : String) : String
        + checkUsername(username : String) : Map
        + checkEmail(email : String) : Map
    }

    %% ─────────────────────────────────────────
    %% AuthService
    %% ─────────────────────────────────────────
    class AuthService {
        - customerRepository : CustomerRepository
        - mailSender : JavaMailSender
        - mailFrom : String
        + validateRegistration(request : RegisterRequest) : void
        + generateOTP() : String
        + completeRegistration(request : RegisterRequest) : Customer
        + sendOTPEmail(email : String, otp : String) : void
        + isUsernameTaken(username : String) : boolean
        + isEmailTaken(email : String) : boolean
    }

    %% ─────────────────────────────────────────
    %% CustomerRepository (interface)
    %% ─────────────────────────────────────────
    class CustomerRepository {
        <<interface>>
        + findByEmail(email : String) : Optional~Customer~
        + findByUserName(userName : String) : Optional~Customer~
        + save(customer : Customer) : Customer
    }

    %% ─────────────────────────────────────────
    %% JpaRepository (interface cha — Spring Data)
    %% ─────────────────────────────────────────
    class JpaRepository {
        <<interface>>
        + save(entity : T) : T
        + findById(id : ID) : Optional~T~
        + findAll() : List~T~
    }

    %% ─────────────────────────────────────────
    %% Customer (Entity — maps to DB table)
    %% ─────────────────────────────────────────
    class Customer {
        - id : Integer
        - fullName : String
        - email : String
        - phone : String
        - address : String
        - password : String
        - userName : String
    }

    %% ─────────────────────────────────────────
    %% JavaMailSender (interface — Spring)
    %% ─────────────────────────────────────────
    class JavaMailSender {
        <<interface>>
        + send(message : SimpleMailMessage) : void
    }

    %% ─────────────────────────────────────────
    %% BCrypt (utility — static methods)
    %% ─────────────────────────────────────────
    class BCrypt {
        + hashpw(password : String, salt : String) : String$
        + checkpw(plain : String, hashed : String) : boolean$
        + gensalt(rounds : int) : String$
    }

    %% ═══════════════════════════════════════════
    %% RELATIONSHIPS
    %% ═══════════════════════════════════════════

    %% [1] ASSOCIATION (solid line)
    %% Field được lưu lâu dài bên trong class
    AuthController "1" --> "1" AuthService : uses
    AuthService "1" --> "1" CustomerRepository : queries
    AuthService "1" --> "1" JavaMailSender : sends via

    %% [2] GENERALIZATION (──|> hollow arrow)
    %% interface extends interface → Generalization
    CustomerRepository --|> JpaRepository

    %% [3] DEPENDENCY (..> dashed)
    %% Dùng tạm qua tham số method, không lưu vào field
    AuthController ..> RegisterRequest : <<uses>>
    AuthService ..> RegisterRequest : <<validates>>
    AuthService ..> Customer : <<creates>>
    AuthService ..> BCrypt : <<uses>>
    CustomerRepository ..> Customer : <<manages>>
```

---

## Tổng hợp 4 lỗi đã sửa (v3 → v4)

| # | Lỗi trong v3 | Sửa trong v4 | Lý do UML |
|---|---|---|---|
| 1 | `AuthController` không có attribute `authService` | Thêm `- authService : AuthService` | Field lưu trong class phải khai báo trong ngăn attributes (Figure 2.4) |
| 2 | `AuthService` thiếu `customerRepository`, `mailSender` | Thêm cả 2 vào ngăn attributes | Idem — field được inject qua constructor phải hiện trong class box |
| 3 | `BCrypt` methods không có ký hiệu static | Thêm `$` vào cuối mỗi method | UML: static member được gạch chân; Mermaid dùng `$` |
| 4 | (đã đúng) `CustomerRepository --|> JpaRepository` | Giữ nguyên Generalization | `interface extends interface` → đúng là Generalization (hollow arrow) |

---

## Bảng quan hệ theo sách (Figure 2.3)

| Quan hệ | Ký hiệu UML | Áp dụng trong diagram |
|---|---|---|
| **Association** | `──────►` solid + multiplicity | Controller→Service, Service→Repo, Service→Mail |
| **Generalization** | `──────▷` hollow arrowhead | `CustomerRepository ──▷ JpaRepository` |
| **Dependency** | `- - - -►` dashed | Controller→RegisterRequest, Service→Customer, Service→BCrypt |

---

## Multiplicity

| Đường nối | Multiplicity | Giải thích |
|---|---|---|
| `AuthController "1" → "1" AuthService` | 1:1 | 1 Controller inject đúng 1 Service |
| `AuthService "1" → "1" CustomerRepository` | 1:1 | 1 Service inject đúng 1 Repository |
| `AuthService "1" → "1" JavaMailSender` | 1:1 | 1 Service inject đúng 1 MailSender |
