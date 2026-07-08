# Đặc tả Hệ thống E-business Gia Hoa Phát (GHP)
**Mã dự án**: GHP  
**Môn học**: SWD392  
**Nhóm thực hiện**: SWD392-G2  
**Loại ứng dụng**: Web Application  

---

## I. Tổng quan Dự án & Thành viên Nhóm
Gia Hoa Phát là một doanh nghiệp lâu năm hoạt động trong lĩnh vực thực phẩm và ngành làm bánh. Hiện nay, doanh nghiệp chủ yếu dựa trên mô hình bán lẻ truyền thống qua cửa hàng vật lý để bán nguyên liệu làm bánh (ví dụ: bột mì, bơ, đường) và thiết bị làm bánh (ví dụ: lò nướng, khuôn làm bánh). Dự án "Gia Hoa Phat E-Business System" được đề xuất để thực hiện chuyển đổi số, tập trung quản lý danh mục sản phẩm, tự động hóa quy trình mua hàng trực tuyến, tối ưu hóa quản lý kho trên nhiều chi nhánh và hỗ trợ hoàn tất đơn hàng một cách liền mạch.

### Thành viên nhóm dự án:
1. **Nguyễn Danh Triệu** (Leader) - nguyendanhtrieu250305@gmail.com - 0355425515
2. **Hà Mạnh Đức** (Member) - haduc8384@gmail.com - 0865054050
3. **Phạm Quang Tiến** (Member) - tienpqhe180293@fpt.edu.vn - 0979975240
4. **Nguyễn Thị Ngọc Linh** (Member) - nguyenthingoclinh291104@gmail.com - 0338728369
5. **Vũ Thị Thủy** (Member)
6. **Nguyễn Việt Thắng** (Member) - thangnvhe180855@fpt.edu.vn - 0389718793

---

## II. Cấu hình Công nghệ & Cơ sở Dữ liệu
* **Môi trường chạy**: Java 17 LTS, Apache Maven (WAR packaging)
* **Tầng Giao diện**: JSP (Jakarta Server Pages), JSTL, CSS, JS
* **Tầng Controller & Logic**: Jakarta Servlet API 6.0
* **Hệ quản trị CSDL**: MySQL Server 8.0.x (Port: `3306`)
* **Thông tin kết nối đã xác minh (Local)**:
  * **Driver Class**: `com.mysql.cj.jdbc.Driver`
  * **JDBC URL**: `jdbc:mysql://localhost:3306/gia_hoa_phat?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`
  * **Username**: `root`
  * **Password**: `123`

---

## III. Các Tính Năng Chính (Major Features)
* **FE-01: Centralized Catalog Display**: Duyệt, tìm kiếm và lọc danh mục sản phẩm trực tuyến tập trung bao gồm cả nguyên liệu làm bánh dễ hỏng (bột, bơ...) và thiết bị làm bánh nặng (lò nướng, máy trộn...).
* **FE-02: Shopping Cart & Checkout**: Thêm sản phẩm vào giỏ hàng và thanh toán bảo mật với nhiều phương thức thanh toán: COD (Thanh toán khi nhận hàng), hoặc ví điện tử/cổng thanh toán bên thứ ba (VNPay, MoMo).
* **FE-03: Real-time Multi-branch Inventory**: Quản lý và theo dõi mức tồn kho theo thời gian thực trên nhiều chi nhánh cửa hàng vật lý, tích hợp cảnh báo tồn kho thấp cho nhân viên.
* **FE-04: Order Lifecycle Management**: Quy trình xử lý đơn hàng toàn diện qua các trạng thái (Pending, Confirmed, Shipping, Completed, Cancelled), đi kèm quy trình trả hàng và hoàn tiền cho thiết bị kỹ thuật.
* **FE-05: Shipping Logistics Automation**: Tự động liên kết các đơn vị giao vận thứ ba để tạo vận đơn, tính toán phí giao hàng dựa trên trọng lượng/kích thước sản phẩm và cung cấp mã theo dõi hành trình (tracking) thời gian thực cho khách hàng.
* **FE-06: Administrative Dashboard**: Cung cấp trang quản trị phân quyền cho Staff và Admin để quản lý danh sách sản phẩm, quản lý tài khoản người dùng và xuất báo cáo doanh thu & bán hàng.
* **FE-07: Reviews & Ratings**: Cho phép khách hàng để lại đánh giá và xếp hạng cho các sản phẩm đã mua, nhân viên có thể phản hồi lại các ý kiến đóng góp của khách hàng.

---

## IV. Yêu Cầu Phi Chức Năng (Non-Functional Requirements)
1. **Payment Integration (Payment Webhook Listener API)**: Endpoint nhận phản hồi cập nhật trạng thái thanh toán bất đồng bộ (Thành công/Thất bại) từ cổng thanh toán VNPay/MoMo và tự động cập nhật trạng thái Đơn hàng trong database.
2. **Inventory Management (Low Stock Alert Cron Job)**: Hệ thống chạy tiến trình ngầm tự động vào lúc 12:00 AM hàng ngày quét bảng Inventory để tìm các nguyên liệu hoặc thiết bị có tồn kho dưới mức tối thiểu (`min_quantity`) và gửi email cảnh báo tự động cho Quản lý cửa hàng (Store Manager).
3. **Order Management (Auto-Cancel Unpaid Orders Job)**: Tiến trình ngầm chạy mỗi 30 phút tự động kiểm tra các đơn hàng ở trạng thái "Pending Payment" quá 24 giờ. Hệ thống tự động chuyển trạng thái đơn hàng sang "Cancelled" và hoàn lại số lượng hàng đã giữ vào kho khả dụng.
4. **Shipping Management (Delivery Status Sync API)**: Tự động liên kết với API giao vận (GHN, Viettel Post...) để đồng bộ hóa trạng thái giao hàng về database nội bộ thông qua webhook hoặc kiểm tra định kỳ.
5. **System Security & Performance (Expired Session Cleanup Batch)**: Tiến trình dọn dẹp hàng tuần để xóa các mã token hết hạn, mã OTP hết hạn và các giỏ hàng tạm thời bị bỏ quên nhằm tối ưu dung lượng và tốc độ truy vấn.
6. **Sales Reporting (Nightly Revenue Aggregation Service)**: Tiến trình ngầm chạy lúc 2:00 AM hàng ngày để tính toán tổng hợp doanh thu bán hàng, phí ship, mã giảm giá của ngày hôm trước và lưu vào một bảng báo cáo riêng để tối ưu tốc độ load dashboard của Admin.

---

## V. Tác Nhân Hệ Thống (Actors)
1. **Guest**: Người dùng chưa đăng nhập. Có thể tìm kiếm sản phẩm, xem danh sách sản phẩm theo danh mục/giá, xem blog, đăng ký tài khoản.
2. **Customer**: Người mua hàng trực tuyến. Có thể xem giỏ hàng, đặt hàng, thanh toán, áp dụng mã voucher, xem lịch sử và chi tiết đơn hàng, viết đánh giá sản phẩm.
3. **Staff**: Nhân viên chi nhánh. Quản lý đơn hàng (xác nhận đơn hàng, cập nhật trạng thái giao hàng, hủy đơn), quản lý giao vận (tạo/xóa/cập nhật bản ghi giao hàng).
4. **Manager**: Quản lý chi nhánh. Quản lý sản phẩm (thêm/sửa/xóa sản phẩm), quản lý kho tồn (cập nhật số lượng kho, ngưỡng cảnh báo), xem báo cáo bán hàng/doanh thu chi nhánh, quản lý nhân viên chi nhánh.
5. **Administrator**: Quản trị viên hệ thống. Có quyền cao nhất, quản trị toàn bộ hệ thống (danh mục, tài khoản, nhân viên, quản lý toàn bộ chi nhánh và xem báo cáo doanh thu toàn hệ thống).
6. **Payment Gateway** (External): Cổng thanh toán trực tuyến xử lý giao dịch.
7. **Delivery Service** (External): Đơn vị vận chuyển (GHN, Viettel Post...) xử lý vận đơn.

---

## VI. Thiết kế Cơ sở Dữ liệu (Database Schema Design)
Hệ thống bao gồm các bảng dữ liệu chính sau:

1. **Category**: Lưu thông tin danh mục sản phẩm (nguyên liệu, thiết bị...).
   * `id` (INT, PK, Auto Increment)
   * `name` (VARCHAR)
   * `description` (TEXT)
2. **Product**: Lưu thông tin sản phẩm tổng quát.
   * `id` (INT, PK, Auto Increment)
   * `name` (VARCHAR)
   * `description` (TEXT)
   * `image_url` (VARCHAR)
   * `status` (VARCHAR)
3. **Product_Variant**: Các biến thể sản phẩm (theo kích cỡ, khối lượng...).
   * `id` (INT, PK, Auto Increment)
   * `product_id` (INT, FK -> Product.id)
   * `name` (VARCHAR)
   * `sku` (VARCHAR)
   * `price` (DECIMAL)
   * `unit` (VARCHAR)
4. **Customer**: Tài khoản khách hàng.
   * `id` (INT, PK, Auto Increment)
   * `full_name` (VARCHAR)
   * `email` (VARCHAR, Unique)
   * `phone` (VARCHAR)
   * `address` (VARCHAR)
   * `password` (VARCHAR)
   * `user_name` (VARCHAR, Unique)
5. **Staff**: Tài khoản nhân viên.
   * `id` (INT, PK, Auto Increment)
   * `shop_id` (INT, FK -> Shop.id)
   * `full_name` (VARCHAR)
   * `phone_number` (VARCHAR)
   * `email` (VARCHAR, Unique)
6. **Shop_Manager**: Tài khoản quản lý chi nhánh.
   * `id` (INT, PK, Auto Increment)
   * `shop_id` (INT, FK -> Shop.id)
   * `full_name` (VARCHAR)
   * `phone_number` (VARCHAR)
   * `email` (VARCHAR, Unique)
7. **Shop**: Chi nhánh cửa hàng.
   * `id` (INT, PK, Auto Increment)
   * `shop_name` (VARCHAR)
   * `address` (VARCHAR)
   * `phone` (VARCHAR)
   * `email` (VARCHAR)
   * `status` (VARCHAR)
8. **Inventory**: Quản lý tồn kho theo từng chi nhánh.
   * `id` (INT, PK, Auto Increment)
   * `shop_id` (INT, FK -> Shop.id)
   * `product_id` (INT, FK -> Product.id)
   * `quantity` (INT)
   * `min_quantity` (INT)
9. **Cart**: Giỏ hàng của khách hàng.
   * `id` (INT, PK, Auto Increment)
   * `customer_id` (INT, FK -> Customer.id)
   * `status` (VARCHAR)
   * `item_quantity` (INT)
10. **Cart_item**: Các mặt hàng chi tiết trong giỏ.
    * `id` (INT, PK, Auto Increment)
    * `cart_id` (INT, FK -> Cart.id)
    * `product_variant_id` (INT, FK -> Product_Variant.id)
    * `quantity` (INT)
    * `unit_price` (DECIMAL)
11. **Order**: Đơn hàng.
    * `id` (INT, PK, Auto Increment)
    * `customer_id` (INT, FK -> Customer.id)
    * `total_amount` (DECIMAL)
    * `status` (VARCHAR)
    * `shipping_address` (TEXT)
    * `order_date` (DATETIME)
    * `payment_method` (VARCHAR)
12. **Order_Details**: Chi tiết sản phẩm trong đơn hàng.
    * `id` (INT, PK, Auto Increment)
    * `order_id` (INT, FK -> Order.id)
    * `product_variant_id` (INT, FK -> Product_Variant.id)
    * `quantity` (INT)
    * `unit_price` (DECIMAL)
13. **Shipment**: Thông tin giao hàng.
    * `id` (INT, PK, Auto Increment)
    * `order_id` (INT, FK -> Order.id)
    * `tracking_code` (VARCHAR)
    * `carrier` (VARCHAR)
    * `shipped_at` (DATETIME)
    * `estimated_delivery` (DATETIME)
    * `status` (VARCHAR)
14. **Payment**: Thông tin giao dịch thanh toán.
    * `id` (INT, PK, Auto Increment)
    * `order_id` (INT, FK -> Order.id)
    * `method` (VARCHAR)
    * `amount` (DECIMAL)
    * `paid_at` (DATETIME)
    * `status` (VARCHAR)
15. **Supplier**: Nhà cung cấp nguyên liệu/thiết bị.
    * `id` (INT, PK, Auto Increment)
    * `name` (VARCHAR)
    * `address` (VARCHAR)
    * `phone` (VARCHAR)
    * `email` (VARCHAR)
    * `contact_person` (VARCHAR)
