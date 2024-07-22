package ca.uwindsor.appliedcomputing.final_project.spec;

import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<ProductData> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ProductData> hasPrice(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else if (maxPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<ProductData> hasExactPrice(Double price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("price"), price);
    }

    public static Specification<ProductData> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<ProductData> hasStore(String store) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("store"), store);
    }
}