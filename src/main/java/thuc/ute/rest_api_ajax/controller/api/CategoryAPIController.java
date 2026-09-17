package thuc.ute.rest_api_ajax.controller.api;

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
import thuc.ute.rest_api_ajax.model.Response;
import thuc.ute.rest_api_ajax.service.ICategoryService;
import thuc.ute.rest_api_ajax.service.IStorageService;

@RestController
@RequestMapping(path = "/api/category")
@RequiredArgsConstructor
public class CategoryAPIController {
    private final ICategoryService categoryService;
    private final IStorageService storageService;

    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        return ResponseEntity.ok(new Response(true, "Thành công", categoryService.findAll()));
    }


    @PostMapping(path = "/getCategory")
    public ResponseEntity<?> getCategory(@Validated @RequestParam("id") Long id) {
        Optional<Category> category = categoryService.findById(id);

        if (category.isPresent()) {
            return ResponseEntity.ok(new Response(true, "Thành công", category.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Không tìm thấy Category", null));
        }
    }



    @PostMapping(path = "/addCategory")
    public ResponseEntity<?> addCategory(@Validated @RequestParam("categoryName")
                                         String categoryName,
                                         @RequestParam(value = "icon", required = false) MultipartFile icon) {
        String normalizedName = categoryName.trim();
        if (normalizedName.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new Response(false, "Tên Category không được để trống", null));
        }

        Optional<Category> optCategory = categoryService.findByCategoryName(normalizedName);

        if (optCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response(false, "Category đã tồn tại trong hệ thống", optCategory.get()));
        } else {
            Category category = new Category();
            if (icon != null && !icon.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                category.setIcon(storageService.getSorageFilename(icon, uuid.toString()));
                storageService.store(icon, category.getIcon());
            }

            category.setCategoryName(normalizedName);
            categoryService.save(category);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new Response(true, "Thêm Category thành công", category));
        }
    }
    @PutMapping(path = "/updateCategory")
    public ResponseEntity<?> updateCategory(@Validated @RequestParam("categoryId")
                                            Long categoryId,
                                            @Validated @RequestParam("categoryName") String categoryName,
                                            @RequestParam(value = "icon", required = false) MultipartFile icon) {
        Optional<Category> optCategory = categoryService.findById(categoryId);

        if (optCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Không tìm thấy Category", null));
        }

        String normalizedName = categoryName.trim();
        if (normalizedName.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new Response(false, "Tên Category không được để trống", null));
        }

        Optional<Category> duplicate = categoryService.findByCategoryName(normalizedName);
        if (duplicate.isPresent() && !duplicate.get().getCategoryId().equals(categoryId)) {
            return ResponseEntity.badRequest()
                    .body(new Response(false, "Category đã tồn tại trong hệ thống", duplicate.get()));
        }

        Category category = optCategory.get();
        if (icon != null && !icon.isEmpty()) {
            String oldIcon = category.getIcon();
            category.setIcon(storageService.getSorageFilename(icon, UUID.randomUUID().toString()));
            storageService.store(icon, category.getIcon());
            deleteFileQuietly(oldIcon);
        }

        category.setCategoryName(normalizedName);
        categoryService.save(category);
        return ResponseEntity.ok(new Response(true, "Cập nhật Category thành công", category));
    }

    @DeleteMapping(path = "/deleteCategory")
    public ResponseEntity<?> deleteCategory(@Validated @RequestParam("categoryId")
                                            Long categoryId){
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Không tìm thấy Category", null));
        }

        Category category = optCategory.get();
        categoryService.delete(category);
        deleteFileQuietly(category.getIcon());
        return ResponseEntity.ok(new Response(true, "Xóa Category thành công", category));
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
