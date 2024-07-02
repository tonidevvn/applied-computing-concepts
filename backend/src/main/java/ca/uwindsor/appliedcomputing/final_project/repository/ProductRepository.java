package ca.uwindsor.appliedcomputing.final_project.repository;

// Annotation
import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository
        extends CrudRepository<ProductData, Long> {
}
