package com.easyshop.model;

/**
 * Created by admin-hp on 2/4/17.
 */

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "subs_order_dtl")
@Data
public class SubscriptionOrderDtlModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subsOrderDtlId;

    @NotNull
    private long subsOrderId;

    @NotNull
    private int subsOrderItemId;

    @NotNull
    private int subsOrderItemQuantity;

    @Transient
    private long subsOrderItemPrice;

    @NotNull
    private String subsOrderItemStatus;

    @Transient
    private String subsOrderItemName;

    public SubscriptionOrderDtlModel(){

    }
}

