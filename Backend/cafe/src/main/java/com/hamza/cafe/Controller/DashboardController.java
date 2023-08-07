package com.hamza.cafe.Controller;

import com.hamza.cafe.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/Daschboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    private final DashboardService dashboardService;
    @GetMapping("/details")
    public ResponseEntity<Map<String,Object>> getCount(){
        return dashboardService.getCount();
    }
}
