Nguyễn Trọng Thức 
24110349
## REST API

### Category API

| Method | Endpoint | Chức năng |
|---|---|---|
| GET | `/api/category` | Lấy tất cả Category |
| POST | `/api/category/getCategory` | Lấy Category theo ID |
| POST | `/api/category/addCategory` | Thêm Category |
| PUT | `/api/category/updateCategory` | Cập nhật Category |
| DELETE | `/api/category/deleteCategory` | Xóa Category |

### Product API

| Method | Endpoint | Chức năng |
|---|---|---|
| GET | `/api/product` | Lấy tất cả Product |
| POST | `/api/product/getProduct` | Lấy Product theo ID |
| POST | `/api/product/addProduct` | Thêm Product |
| PUT | `/api/product/updateProduct` | Cập nhật Product |
| DELETE | `/api/product/deleteProduct` | Xóa Product |

- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **Swagger UI:** http://localhost:8080/swagger-ui.html

Swagger dùng để kiểm thử và xem tài liệu các REST API của project.

---

## Giao diện AJAX

- **Category Management:** http://localhost:8080/admin/categories
- **Product Management:** http://localhost:8080/admin/products

Dữ liệu trên các trang được lấy từ REST API bằng jQuery AJAX.
