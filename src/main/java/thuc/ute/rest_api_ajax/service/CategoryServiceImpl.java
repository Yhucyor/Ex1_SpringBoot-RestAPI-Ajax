package thuc.ute.rest_api_ajax.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import thuc.ute.rest_api_ajax.entity.Category;
import thuc.ute.rest_api_ajax.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl
        implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category>
    findByCategoryName(String name) {

        return categoryRepository
                .findByCategoryName(name);
    }

    @Override
    public Category save(Category category) {

        if (category.getCategoryId() != null) {

            Optional<Category> oldCategory =
                    categoryRepository.findById(
                            category.getCategoryId()
                    );

            if (oldCategory.isPresent()
                    && (category.getIcon() == null
                    || category.getIcon().isBlank())) {

                category.setIcon(
                        oldCategory.get().getIcon()
                );
            }
        }

        return categoryRepository.save(category);
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category>
    findByCategoryNameContaining(String name) {

        return categoryRepository
                .findByCategoryNameContaining(name);
    }

    @Override
    public Page<Category>
    findByCategoryNameContaining(
            String name,
            Pageable pageable) {

        return categoryRepository
                .findByCategoryNameContaining(
                        name,
                        pageable
                );
    }
}