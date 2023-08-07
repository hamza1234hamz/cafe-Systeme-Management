package com.hamza.cafe.Controller;

import com.hamza.cafe.Exception.CafeUtils;
import com.hamza.cafe.Exception.MessageError.CafeErros;
import com.hamza.cafe.Wrapper.ProductWraper;
import com.hamza.cafe.dao.request.CategoryRequest;
import com.hamza.cafe.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductContoller {
    private final ProductService productService;
    @PostMapping("/add")
    public ResponseEntity<String> addNewProduct(@RequestBody Map<String,String> request){
        try {
            return productService.addNewProduct(request);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/get")
    public ResponseEntity<List<ProductWraper>> getAllProduct(){
        try {
            return productService.getAllProduct();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("update")
    public ResponseEntity<String> updateProduct(@RequestBody Map<String,String> request){
        try{
            return productService.updateProduct(request);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
        try{
            return productService.deleteProduct(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("updateStatus")
    public ResponseEntity<String> updateStaus(@RequestBody Map<String,String> request){
        try {
            return productService.updateStaus(request);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/getByCategory/{id}")
    public ResponseEntity<List<ProductWraper>> getByCategory(@PathVariable Integer id){
        try {
            return productService.getByCategory(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductWraper> getProductById (@PathVariable Integer id){
        try{
            return productService.getProductById(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWraper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
