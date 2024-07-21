package ca.uwindsor.appliedcomputing.final_project.repository;

// Annotation
import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository
        extends JpaRepository<ProductData, Long> {
    @Query("SELECT p FROM product_data p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', ?1, '%')) ORDER BY p.price ASC")
    Page<ProductData> findByKeyword(String name, Pageable pageable);
}
