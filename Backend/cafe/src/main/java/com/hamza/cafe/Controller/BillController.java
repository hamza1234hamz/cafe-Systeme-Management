package com.hamza.cafe.Controller;

import com.hamza.cafe.Exception.CafeUtils;
import com.hamza.cafe.Exception.MessageError.CafeErros;
import com.hamza.cafe.entities.Bill;
import com.hamza.cafe.service.BillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Bill")
@RequiredArgsConstructor
@Slf4j
public class BillController {
    private final BillService billService;
    @PostMapping("/generateRaport")
    public ResponseEntity<String> generateRaport(@RequestBody Map<String,Object> request) {
        try{
            return billService.generateRaport(request);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("getBills")
    public ResponseEntity<List<Bill>> getBills(){
        try {
            return billService.getBills();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    @PostMapping("getPdf")
    public ResponseEntity<byte[]> getPdf(@RequestBody Map<String,Object> request){
        try {
            return billService.getPdf(request);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable Integer id){
        try {
            return billService.deleteBill(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
