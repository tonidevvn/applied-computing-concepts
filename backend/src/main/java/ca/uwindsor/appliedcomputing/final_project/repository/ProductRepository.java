package ca.uwindsor.appliedcomputing.final_project.repository;

// Annotation
import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductData, Long>, JpaSpecificationExecutor<ProductData> {
}
