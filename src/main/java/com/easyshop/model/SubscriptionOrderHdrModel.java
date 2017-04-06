package com.easyshop.model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
/**
 * Created by admin-hp on 2/4/17.
 */

@Getter
@Setter
@Entity
@Table(name = "subs_order_hdr")
@Data
public class SubscriptionOrderHdrModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private long subsTaxAmount;

    @NotNull
    private String subsOrderStatus;

    @NotNull
    private int subsOrderAddressId;

    @NotNull
    @DateTimeFormat
    private Date subsOrderCreatedDate;

    @NotNull
    @DateTimeFormat
    private Date subsOrderUpdatedDate;

    public SubscriptionOrderHdrModel(){}

}



