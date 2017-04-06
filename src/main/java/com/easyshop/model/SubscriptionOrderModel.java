package com.easyshop.model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * Created by admin-hp on 2/4/17.
 */@Getter
@Setter
public class SubscriptionOrderModel {

    private Long subsOrderHdrId;

    @NotNull
    private Long subsOrderId;

    @NotNull
    private int custId;

    @NotNull
    private int subsOrderItemCount;

    @NotNull
    private long subsOrderTotal;

    @NotNull
    private String subsOrderStatus;

    @NotNull
    private long subsTaxAmount;

    @NotNull
    private int subsOrderAddressId;

    @NotNull
    private Date subsOrderCreatedDate;

    @NotNull
    private Date subsOrderUpdatedDate;

    @NotNull
    private  int subscriptionType;

    @NotNull
    private Calendar nextDueDate;

    @OneToMany
    private List<SubscriptionOrderDtlModel> items;

    public SubscriptionOrderModel(){}

}

