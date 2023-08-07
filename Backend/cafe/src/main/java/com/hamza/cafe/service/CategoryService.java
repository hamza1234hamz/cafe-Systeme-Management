package com.hamza.cafe.service;

import com.hamza.cafe.dao.request.CategoryRequest;
import com.hamza.cafe.entities.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    ResponseEntity<String> addNewCategory(CategoryRequest request);
    ResponseEntity<List<Category>> getAllCategory(String filterValue);

    ResponseEntity<String> updateCategory(Map<String,String> request);
}
