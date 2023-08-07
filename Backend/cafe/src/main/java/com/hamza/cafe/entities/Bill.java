package com.hamza.cafe.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Bill implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String uuid;
    private String name;
    private String email;
    private String contactNumber;
    private String paymentMethod;
    private Integer total;
    @Column(name="productdetail",columnDefinition = "json")
    private String productDetail;
    private String createdBy;
}
