package ca.uwindsor.appliedcomputing.final_project.service;


import ca.uwindsor.appliedcomputing.final_project.dto.ProductData;

import java.util.Set;

public interface ProductService {
    Set<ProductData> getProducts();
    Set<ProductData> getProductsByKeyword(String keyword) throws Exception;
}