package com.hamza.cafe.service;

import com.hamza.cafe.Wrapper.ProductWraper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ResponseEntity<String> addNewProduct(Map<String,String> request);
    ResponseEntity<List<ProductWraper>> getAllProduct();
    ResponseEntity<String> updateProduct(Map<String,String> request);

    ResponseEntity<String> deleteProduct(Integer id);

    ResponseEntity<String> updateStaus(Map<String,String> request);
    ResponseEntity<List<ProductWraper>> getByCategory(Integer id);

    ResponseEntity<ProductWraper> getProductById(Integer id);

}
