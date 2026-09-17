package thuc.ute.rest_api_ajax.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import thuc.ute.rest_api_ajax.entity.Product;
import thuc.ute.rest_api_ajax.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl
        implements IProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Product>
    findByProductName(String name) {

        return productRepository
                .findByProductName(name);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product>
    findByProductNameContaining(
            String name,
            Pageable pageable) {

        return productRepository
                .findByProductNameContaining(
                        name,
                        pageable
                );
    }
}