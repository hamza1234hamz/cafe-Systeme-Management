package com.hamza.cafe.Wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWraper {
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private String status;
    private Integer categoryId;
    private String categoryName;

    public ProductWraper(Integer id,String name){
        this.id=id;
        this.name=name;
    }

    public ProductWraper(Integer id,String name,String description,Integer price){
        this.id=id;
        this.name=name;
        this.description=description;
        this.price=price;
    }
}
