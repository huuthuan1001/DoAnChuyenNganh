package FigureShop.repository;

import FigureShop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingAndCategoryIdAndPriceBetween(String name, Long categoryId, Double minPrice, Double maxPrice);
    List<Product> findByNameContainingIgnoreCase(String name);
}
