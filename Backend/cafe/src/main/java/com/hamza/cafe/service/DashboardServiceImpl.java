package com.hamza.cafe.service;

import com.hamza.cafe.Repository.BillRepository;
import com.hamza.cafe.Repository.CategoryRepository;
import com.hamza.cafe.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    private final CategoryRepository categoryRepository;
    private final BillRepository billRepository;
    private final ProductRepository productRepository;
    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String,Object> map=new HashMap<>();
        map.put("category",categoryRepository.count());
        map.put("product",productRepository.count());
        map.put("bill",billRepository.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
