package com.easyshop.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name="item")
@Getter
@Setter
@Data

/**
 * Created by admin-hp on 19/2/17.
 */
public class CatalogModel {
    @Id
    private long itemId;

    @NotNull
    private String itemName;

    @NotNull
    private String itemDescription;

    @NotNull
    private float itemPrice;

    @NotNull
    private long itemQuantity;

    public CatalogModel(){}

    public String getItemName() {
        return itemName;
    }
}
