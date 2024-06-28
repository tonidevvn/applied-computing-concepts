package ca.uwindsor.appliedcomputing.final_project.service;


import ca.uwindsor.appliedcomputing.final_project.dto.Page;
import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;

import java.util.Set;

public interface ProductService {
    Page<Set<ProductData>> getProducts(String keyword, int page, int limit);
    Set<ProductData> getProductsByKeyword(String keyword) throws Exception;
}