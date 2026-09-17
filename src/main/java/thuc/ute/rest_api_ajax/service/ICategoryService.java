package thuc.ute.rest_api_ajax.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import thuc.ute.rest_api_ajax.entity.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    List<Category> findAll();

    Optional<Category> findById(Long id);

    Optional<Category> findByCategoryName(String name);

    Category save(Category category);

    void delete(Category category);

    void deleteById(Long id);

    List<Category>
    findByCategoryNameContaining(String name);

    Page<Category>
    findByCategoryNameContaining(
            String name,
            Pageable pageable
    );
}