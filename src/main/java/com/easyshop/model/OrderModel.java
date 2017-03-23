package com.easyshop.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by pavan on 2/17/17.
 */
@Getter
@Setter
public class OrderModel {

    private Long orderHdrId;

    @NotNull
    private Long orderId;

    @NotNull
    private int custId;

    @NotNull
    private int orderItemCount;

    @NotNull
    private long orderTotal;

    @NotNull
    private String orderStatus;

    @NotNull
    private int orderAddressId;

    @NotNull
    private String orderCreatedDate;

    @NotNull
    private String orderUpdatedDate;

    @OneToMany
    private List<OrderDtlModel> items;

    public OrderModel(){}
}
