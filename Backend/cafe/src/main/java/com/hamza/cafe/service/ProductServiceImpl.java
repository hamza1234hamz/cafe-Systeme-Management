package com.hamza.cafe.service;

import com.hamza.cafe.Exception.CafeUtils;
import com.hamza.cafe.Exception.MessageError.CafeErros;
import com.hamza.cafe.Repository.CategoryRepository;
import com.hamza.cafe.Repository.ProductRepository;
import com.hamza.cafe.Repository.UserRepository;
import com.hamza.cafe.Wrapper.ProductWraper;
import com.hamza.cafe.entities.Category;
import com.hamza.cafe.entities.Product;
import com.hamza.cafe.entities.Role;
import com.hamza.cafe.security.config.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class ProductServiceImpl implements ProductService {
    private final JwtAuthenticationFilter filter;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> request) {
        try {
            if (filter.isAdmin()){
                if (validateProductMap(request,false)){
                    productRepository.save(getProductFromMap(request,false));
                    return CafeUtils.getResponseEntity("Product Added successfully",HttpStatus.OK);
                }
                else {
                    return CafeUtils.getResponseEntity(CafeErros.INVALID_DATA,HttpStatus.BAD_REQUEST);
                }
            }else {
                return CafeUtils.getResponseEntity(CafeErros.UNAUTHORIWED_ACCES,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    private boolean validateProductMap(Map<String, String> request, boolean validateId) {
        if (request.containsKey("name")){
            if (request.containsKey("id") && validateId){
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }
    private Product getProductFromMap(Map<String, String> request, boolean isAdd){
        Category category=new Category();
        category.setId(Integer.parseInt(request.get("categoryId")));
        Product product=new Product();
        if (isAdd){
            product.setId(Integer.parseInt(request.get("id")));
        }
        else{
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(request.get("name"));
        product.setDescription(request.get("description"));
        product.setPrice(Integer.parseInt(request.get("price")));
        return product;
    }
    @Override
    public ResponseEntity<List<ProductWraper>> getAllProduct() {
        try{
            return new ResponseEntity<>(productRepository.getAllProduct(),HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> request) {
        try{
            String email=filter.getCurrentUset();
            var user=userRepository.findByEmail(email).orElse(null);
            if (user.getRole()== Role.admin){
                if (validateProductMap(request,true)){
                   Optional<Product> optional= productRepository.findById(Integer.parseInt(request.get("id")));
                   if (!optional.isEmpty()){
                       Product product=getProductFromMap(request,true);
                       product.setStatus(optional.get().getStatus());
                       productRepository.save(product);
                       return CafeUtils.getResponseEntity("Product updated successfully",HttpStatus.OK);
                   }else {
                       return CafeUtils.getResponseEntity("Product id does not exist",HttpStatus.OK);
                   }
                }else {
                    return CafeUtils.getResponseEntity(CafeErros.INVALID_DATA,HttpStatus.BAD_REQUEST);
                }
            }else {
                return CafeUtils.getResponseEntity(CafeErros.UNAUTHORIWED_ACCES,HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try{
            if (filter.isAdmin()){
                Optional optional= productRepository.findById(id);
                if (!optional.isEmpty()){
                    productRepository.deleteById(id);
                    return CafeUtils.getResponseEntity("Product deleted successfully",HttpStatus.OK);
                }else {
                    return CafeUtils.getResponseEntity("Product id does not exist",HttpStatus.OK);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeErros.UNAUTHORIWED_ACCES,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStaus(Map<String, String> request) {
        try {
            String email=filter.getCurrentUset();
            var user=userRepository.findByEmail(email).orElse(null);
            if (user.getRole()== Role.admin){
                Optional optional= productRepository.findById(Integer.parseInt(request.get("id")));
                if (!optional.isEmpty()){
                    productRepository.updateProductStatus(request.get("status"),Integer.parseInt(request.get("id")));
                    return CafeUtils.getResponseEntity("Product status updated successfully",HttpStatus.OK);
                }else {
                    return CafeUtils.getResponseEntity("Product id does not exist",HttpStatus.OK);
                }

            }else {
                return CafeUtils.getResponseEntity(CafeErros.UNAUTHORIWED_ACCES,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWraper>> getByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productRepository.getProductByCategory(id),HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWraper> getProductById(Integer id) {
        try{
            return new ResponseEntity<>(productRepository.getProductById(id),HttpStatus.OK);
        }catch (Exception ex){

        }
        return new ResponseEntity<>(new ProductWraper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
