package com.easyshop.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by pavan on 2/17/17.
 */
@Getter
@Setter
@Entity
@Table(name = "address")
@Data
public class AddressModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotNull
    private long custId;

    @NotNull
    private String address1;

    @NotNull
    private String address2;

    @NotNull
    private String state;

    @NotNull
    private String city;

    @NotNull
    private String country;

    @NotNull
    private Integer zipcode;

    @NotNull
    private String phoneNumber;

    public AddressModel(){}
}
