package com.hamza.cafe.service;

import com.hamza.cafe.entities.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface BillService {
    ResponseEntity<String> generateRaport(Map<String,Object> request);

    ResponseEntity<List<Bill>> getBills();

    ResponseEntity<byte[]> getPdf(Map<String,Object> request);

    ResponseEntity<String> deleteBill(Integer id);


}
