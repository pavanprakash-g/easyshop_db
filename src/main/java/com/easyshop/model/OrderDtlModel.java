package com.easyshop.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by pavan on 2/17/17.
 */
@Getter
@Setter
@Entity
@Table(name = "order_dtl")
@Data
public class OrderDtlModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDtlId;

    @NotNull
    private long orderId;

    @NotNull
    private int orderItemId;

    @NotNull
    private int orderItemQuantity;

    @NotNull
    private long orderItemPrice;

    @NotNull
    private String orderItemStatus;

    @Transient
    private String orderItemName;

    @NotNull
    private long orderShipmentId;

    public OrderDtlModel(){}
}
