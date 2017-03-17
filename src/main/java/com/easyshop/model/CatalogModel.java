package com.easyshop.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;


/**
 * Created by admin-hp on 19/2/17.
 */
@Entity
@Table(name="item")
@Getter
@Setter
@Data
public class CatalogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemId;

    @NotNull
    private String itemName;

    @NotNull
    private String itemDescription;

    @NotNull
    private float itemPrice;

    @NotNull
    private long itemQuantity;

    @NotNull
    private String itemImage;

    public CatalogModel(){}

}
