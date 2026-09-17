-- Du lieu mau cho RestAPI_Ajax_DB.
-- INSERT ... SELECT ... WHERE NOT EXISTS giup khoi dong lai ma khong trung du lieu.

INSERT INTO Categories (category_name, icon)
SELECT N'Điện thoại', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM Categories WHERE category_name = N'Điện thoại'
);

INSERT INTO Categories (category_name, icon)
SELECT N'Máy tính xách tay', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM Categories WHERE category_name = N'Máy tính xách tay'
);

INSERT INTO Categories (category_name, icon)
SELECT N'Phụ kiện', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM Categories WHERE category_name = N'Phụ kiện'
);

INSERT INTO Categories (category_name, icon)
SELECT N'Thiết bị gia dụng', NULL
WHERE NOT EXISTS (
    SELECT 1 FROM Categories WHERE category_name = N'Thiết bị gia dụng'
);

INSERT INTO Products
    (product_name, quantity, unit_price, images, description, discount, create_date, status, category_id)
SELECT
    N'iPhone 15 256GB', 12, 21990000, NULL,
    N'Điện thoại Apple với dung lượng lưu trữ 256GB.', 1500000, GETDATE(), 1, category_id
FROM Categories
WHERE category_name = N'Điện thoại'
  AND NOT EXISTS (
      SELECT 1 FROM Products WHERE product_name = N'iPhone 15 256GB'
  );

INSERT INTO Products
    (product_name, quantity, unit_price, images, description, discount, create_date, status, category_id)
SELECT
    N'Samsung Galaxy S24', 15, 18990000, NULL,
    N'Điện thoại Android cao cấp, màn hình sắc nét.', 1200000, GETDATE(), 1, category_id
FROM Categories
WHERE category_name = N'Điện thoại'
  AND NOT EXISTS (
      SELECT 1 FROM Products WHERE product_name = N'Samsung Galaxy S24'
  );

INSERT INTO Products
    (product_name, quantity, unit_price, images, description, discount, create_date, status, category_id)
SELECT
    N'MacBook Air M3', 6, 28990000, NULL,
    N'Laptop mỏng nhẹ sử dụng chip Apple M3.', 2000000, GETDATE(), 1, category_id
FROM Categories
WHERE category_name = N'Máy tính xách tay'
  AND NOT EXISTS (
      SELECT 1 FROM Products WHERE product_name = N'MacBook Air M3'
  );

INSERT INTO Products
    (product_name, quantity, unit_price, images, description, discount, create_date, status, category_id)
SELECT
    N'ASUS Vivobook 15', 9, 15990000, NULL,
    N'Laptop 15 inch phù hợp học tập và văn phòng.', 1000000, GETDATE(), 1, category_id
FROM Categories
WHERE category_name = N'Máy tính xách tay'
  AND NOT EXISTS (
      SELECT 1 FROM Products WHERE product_name = N'ASUS Vivobook 15'
  );

INSERT INTO Products
    (product_name, quantity, unit_price, images, description, discount, create_date, status, category_id)
SELECT
    N'Tai nghe Bluetooth TWS', 30, 890000, NULL,
    N'Tai nghe không dây nhỏ gọn, hỗ trợ sạc nhanh.', 90000, GETDATE(), 1, category_id
FROM Categories
WHERE category_name = N'Phụ kiện'
  AND NOT EXISTS (
      SELECT 1 FROM Products WHERE product_name = N'Tai nghe Bluetooth TWS'
  );

INSERT INTO Products
    (product_name, quantity, unit_price, images, description, discount, create_date, status, category_id)
SELECT
    N'Chuột không dây Silent', 40, 350000, NULL,
    N'Chuột không dây yên tĩnh dành cho văn phòng.', 50000, GETDATE(), 1, category_id
FROM Categories
WHERE category_name = N'Phụ kiện'
  AND NOT EXISTS (
      SELECT 1 FROM Products WHERE product_name = N'Chuột không dây Silent'
  );

INSERT INTO Products
    (product_name, quantity, unit_price, images, description, discount, create_date, status, category_id)
SELECT
    N'Nồi chiên không dầu 5L', 8, 2490000, NULL,
    N'Nồi chiên dung tích 5 lít, điều khiển điện tử.', 300000, GETDATE(), 1, category_id
FROM Categories
WHERE category_name = N'Thiết bị gia dụng'
  AND NOT EXISTS (
      SELECT 1 FROM Products WHERE product_name = N'Nồi chiên không dầu 5L'
  );

INSERT INTO Products
    (product_name, quantity, unit_price, images, description, discount, create_date, status, category_id)
SELECT
    N'Máy lọc không khí', 5, 3990000, NULL,
    N'Máy lọc không khí cho phòng có diện tích vừa.', 450000, GETDATE(), 1, category_id
FROM Categories
WHERE category_name = N'Thiết bị gia dụng'
  AND NOT EXISTS (
      SELECT 1 FROM Products WHERE product_name = N'Máy lọc không khí'
  );
