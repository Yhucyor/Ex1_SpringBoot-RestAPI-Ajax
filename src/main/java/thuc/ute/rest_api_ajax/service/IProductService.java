package thuc.ute.rest_api_ajax.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import thuc.ute.rest_api_ajax.entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Optional<Product> findByProductName(String name);

    Product save(Product product);

    void delete(Product product);

    void deleteById(Long id);

    Page<Product>
    findByProductNameContaining(
            String name,
            Pageable pageable
    );
}