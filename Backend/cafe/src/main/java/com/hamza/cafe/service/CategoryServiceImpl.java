package com.hamza.cafe.service;

import com.hamza.cafe.Exception.CafeUtils;
import com.hamza.cafe.Exception.MessageError.CafeErros;
import com.hamza.cafe.Repository.CategoryRepository;
import com.hamza.cafe.Repository.UserRepository;
import com.hamza.cafe.dao.request.CategoryRequest;
import com.hamza.cafe.entities.Category;
import com.hamza.cafe.entities.Role;
import com.hamza.cafe.entities.User;
import com.hamza.cafe.security.config.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final JwtAuthenticationFilter filter;
    private final UserRepository userRepository;


    @Override
    public ResponseEntity<String> addNewCategory(CategoryRequest request) {
        try{
            String email=filter.getCurrentUset();
            var user=userRepository.findByEmail(email).orElse(null);
            if (user!=null && user.getRole()== Role.admin){
                var category = Category.builder().name(request.getName()).build();
                categoryRepository.save(category);
                return CafeUtils.getResponseEntity("Category Added successfuly", HttpStatus.OK);
            }else {
                return CafeUtils.getResponseEntity(CafeErros.UNAUTHORIWED_ACCES,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            if (filterValue.equalsIgnoreCase("true")){
                log.info("inside first if service");
                return new ResponseEntity<List<Category>>(categoryRepository.getAllCategory(),HttpStatus.OK);
            }
            else {
                log.info("inside first else service");
                return new ResponseEntity<List<Category>>(categoryRepository.findAll(),HttpStatus.OK);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        log.info("internal server service");
        return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> request) {
        try {
            String email=filter.getCurrentUset();
            var user=userRepository.findByEmail(email).orElse(null);
            if (user.getRole()== Role.admin){
                if (validateCategoryMap(request,true)){
                    Optional optional = categoryRepository.findById(Integer.parseInt(request.get("id")));
                    if (!optional.isEmpty()){
                        var category = Category.builder().id(Integer.parseInt(request.get("id"))).
                        name(request.get("name"))
                        .build();
                        categoryRepository.save(category);
                        return CafeUtils.getResponseEntity("Category updated succesfully",HttpStatus.OK);
                    }
                    else {
                        return CafeUtils.getResponseEntity("Category id does not exoist",HttpStatus.OK);
                    }
                }
                else {
                    return CafeUtils.getResponseEntity(CafeErros.INVALID_DATA,HttpStatus.BAD_REQUEST);
                }
            }
            else {
                return CafeUtils.getResponseEntity(CafeErros.UNAUTHORIWED_ACCES,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){

        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private boolean validateCategoryMap(Map<String, String> request,boolean validateId){
        if (request.containsKey("name")){
            if (request.containsKey("id") && validateId){
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }
}
