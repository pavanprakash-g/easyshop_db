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
@Table(name = "order_hdr")
@Data
public class OrderHdrModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public OrderHdrModel(){}
}
