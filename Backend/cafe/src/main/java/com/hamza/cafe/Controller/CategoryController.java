package com.hamza.cafe.Controller;

import com.hamza.cafe.Exception.CafeUtils;
import com.hamza.cafe.Exception.MessageError.CafeErros;
import com.hamza.cafe.dao.request.CategoryRequest;
import com.hamza.cafe.entities.Category;
import com.hamza.cafe.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/add")
    public ResponseEntity<String> addNewCategory(@RequestBody CategoryRequest request){
        try {
            return categoryService.addNewCategory(request);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/get/{filterValue}")
    public ResponseEntity<List<Category>> getAllCategory(@RequestBody(required = false) @PathVariable("filterValue") String filterValue){
        try{
            log.info("inside try controller");
            return categoryService.getAllCategory(filterValue);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        log.info("internal server controller");
        return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("/update")
    public ResponseEntity<String> updateCategory(@RequestBody Map<String,String> request){
        try {
            return categoryService.updateCategory(request);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
