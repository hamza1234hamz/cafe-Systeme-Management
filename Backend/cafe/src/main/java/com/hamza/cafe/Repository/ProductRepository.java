package com.hamza.cafe.Repository;

import com.hamza.cafe.Wrapper.ProductWraper;
import com.hamza.cafe.entities.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query("select new com.hamza.cafe.Wrapper.ProductWraper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p")
    List<ProductWraper> getAllProduct();

    @Modifying
    @Transactional
    @Query("update Product p set p.status=:status where p.id=:id")
    Integer updateProductStatus(@Param("status") String status,@Param("id") Integer id);

    @Query("select new com.hamza.cafe.Wrapper.ProductWraper(p.id,p.name) from Product p where p.category.id=:id and p.status='true'")
    List<ProductWraper> getProductByCategory(@Param("id") Integer id);
    @Query("select new com.hamza.cafe.Wrapper.ProductWraper(p.id,p.name,p.description,p.price) from Product p where p.id=:id")
    ProductWraper getProductById(@Param("id") Integer id);
}
