package thuc.ute.rest_api_ajax.controller.api;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import thuc.ute.rest_api_ajax.entity.Category;
import thuc.ute.rest_api_ajax.entity.Product;
import thuc.ute.rest_api_ajax.model.Response;
import thuc.ute.rest_api_ajax.service.ICategoryService;
import thuc.ute.rest_api_ajax.service.IProductService;
import thuc.ute.rest_api_ajax.service.IStorageService;

@RestController
@RequestMapping(path = "/api/product")
@RequiredArgsConstructor
public class ProductAPIController {

    private final IProductService productService;
    private final ICategoryService categoryService;
    private final IStorageService storageService;

    @GetMapping
    public ResponseEntity<Response> getAllProduct() {
        return ResponseEntity.ok(new Response(true, "Thành công", productService.findAll()));
    }

    @PostMapping(path = "/getProduct")
    public ResponseEntity<Response> getProduct(@Validated @RequestParam("id") Long id) {
        return productService.findById(id)
                .map(product -> ResponseEntity.ok(new Response(true, "Thành công", product)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Response(false, "Không tìm thấy Product", null)));
    }

    @PostMapping(path = "/addProduct")
    public ResponseEntity<Response> addProduct(
            @Validated @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @Validated @RequestParam("unitPrice") Double unitPrice,
            @Validated @RequestParam("discount") Double discount,
            @Validated @RequestParam("description") String description,
            @Validated @RequestParam("categoryId") Long categoryId,
            @Validated @RequestParam("quantity") Integer quantity,
            @Validated @RequestParam("status") Short status) {

        String normalizedName = productName.trim();
        ResponseEntity<Response> validationError = validateProduct(
                normalizedName, unitPrice, discount, description, quantity);
        if (validationError != null) {
            return validationError;
        }

        Optional<Product> duplicate = productService.findByProductName(normalizedName);
        if (duplicate.isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new Response(false, "Sản phẩm đã tồn tại trong hệ thống", duplicate.get()));
        }

        Optional<Category> category = categoryService.findById(categoryId);
        if (category.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new Response(false, "Category không tồn tại", null));
        }

        Product product = new Product();
        copyProductFields(product, normalizedName, unitPrice, discount, description,
                category.get(), quantity, status);
        product.setCreateDate(new Date());
        saveImage(product, imageFile);
        productService.save(product);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response(true, "Thêm Product thành công", product));
    }

    @PutMapping(path = "/updateProduct")
    public ResponseEntity<Response> updateProduct(
            @Validated @RequestParam("productId") Long productId,
            @Validated @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @Validated @RequestParam("unitPrice") Double unitPrice,
            @Validated @RequestParam("discount") Double discount,
            @Validated @RequestParam("description") String description,
            @Validated @RequestParam("categoryId") Long categoryId,
            @Validated @RequestParam("quantity") Integer quantity,
            @Validated @RequestParam("status") Short status) {

        Optional<Product> existing = productService.findById(productId);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Không tìm thấy Product", null));
        }

        String normalizedName = productName.trim();
        ResponseEntity<Response> validationError = validateProduct(
                normalizedName, unitPrice, discount, description, quantity);
        if (validationError != null) {
            return validationError;
        }

        Optional<Product> duplicate = productService.findByProductName(normalizedName);
        if (duplicate.isPresent() && !duplicate.get().getProductId().equals(productId)) {
            return ResponseEntity.badRequest()
                    .body(new Response(false, "Sản phẩm đã tồn tại trong hệ thống", duplicate.get()));
        }

        Optional<Category> category = categoryService.findById(categoryId);
        if (category.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new Response(false, "Category không tồn tại", null));
        }

        Product product = existing.get();
        copyProductFields(product, normalizedName, unitPrice, discount, description,
                category.get(), quantity, status);

        if (imageFile != null && !imageFile.isEmpty()) {
            String oldImage = product.getImages();
            saveImage(product, imageFile);
            deleteFileQuietly(oldImage);
        }

        productService.save(product);
        return ResponseEntity.ok(new Response(true, "Cập nhật Product thành công", product));
    }

    @DeleteMapping(path = "/deleteProduct")
    public ResponseEntity<Response> deleteProduct(@Validated @RequestParam("productId") Long productId) {
        Optional<Product> existing = productService.findById(productId);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Không tìm thấy Product", null));
        }

        Product product = existing.get();
        productService.delete(product);
        deleteFileQuietly(product.getImages());
        return ResponseEntity.ok(new Response(true, "Xóa Product thành công", product));
    }

    private ResponseEntity<Response> validateProduct(String productName, Double unitPrice,
                                                       Double discount, String description,
                                                       Integer quantity) {
        if (productName.isEmpty() || description.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new Response(false, "Tên và mô tả sản phẩm không được để trống", null));
        }
        if (unitPrice < 0 || discount < 0 || quantity < 0) {
            return ResponseEntity.badRequest()
                    .body(new Response(false, "Giá, giảm giá và số lượng phải lớn hơn hoặc bằng 0", null));
        }
        return null;
    }

    private void copyProductFields(Product product, String productName, Double unitPrice,
                                   Double discount, String description, Category category,
                                   Integer quantity, Short status) {
        product.setProductName(productName);
        product.setUnitPrice(unitPrice);
        product.setDiscount(discount);
        product.setDescription(description.trim());
        product.setCategory(category);
        product.setQuantity(quantity);
        product.setStatus(status);
    }

    private void saveImage(Product product, MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return;
        }
        String filename = storageService.getSorageFilename(imageFile, UUID.randomUUID().toString());
        storageService.store(imageFile, filename);
        product.setImages(filename);
    }

    private void deleteFileQuietly(String filename) {
        if (filename == null || filename.isBlank()) {
            return;
        }
        try {
            storageService.delete(filename);
        } catch (Exception ignored) {
            // Dữ liệu vẫn được cập nhật nếu tệp cũ không còn tồn tại.
        }
    }
}
