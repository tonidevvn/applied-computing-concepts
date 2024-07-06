package ca.uwindsor.appliedcomputing.final_project.repository;

// Annotation
import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository
        extends JpaRepository<ProductData, Long> {

    /**
     * Finds a list of ProductData where the name or description contains the specified parameter.
     *
     * @param param the parameter to filter by
     * @return a list of ProductData that matches the filter criteria
     */
    @Query("SELECT p FROM ProductData p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<ProductData> findByKeyword(String param);

    /**
     * Finds a list of ProductData where the name or description contains the specified parameter with a limit on the number of results.
     *
     * @param param the parameter to filter by
     * @param limit the maximum number of results to return
     * @return a list of ProductData that matches the filter criteria and is limited by the specified number
     */
    @Query(value = "SELECT * FROM ProductData WHERE LOWER(name) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(description) LIKE LOWER(CONCAT('%', ?1, '%')) LIMIT ?2", nativeQuery = true)
    List<ProductData> findByKeywordWithLimit(String param, int limit);

    /**
     * Finds a list of ProductData with a limit on the number of results.
     *
     * @param limit the maximum number of results to return
     * @return a list of ProductData limited by the specified number
     */
    @Query(value = "SELECT * FROM ProductData LIMIT ?1", nativeQuery = true)
    List<ProductData> fetchProductList(int limit);
}
