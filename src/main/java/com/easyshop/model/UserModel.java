package com.easyshop.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;


/**
 * Created by admin on 22/10/16.
 */
@Entity
@Table(name="customer")
@Getter
@Setter
@Data
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long custId;

    @NotNull
    private String custFirstName;

    @NotNull
    private String custLastName;
    @NotNull
    private String custEmailid;
    @NotNull
    private String custPhoneNumber;
    @NotNull
    private String custPassword;

    private String address1;
    
    private String address2;

    @NotNull
    private String city;

    @NotNull
    private String state;

    @NotNull
    private int zipcode;

    @NotNull
    private boolean activeStatus;

    private int securityQuesId;

    private String securityQuesAns;

    @OneToOne
    @JoinColumn(name = "securityQuesId", nullable = false, insertable = false, updatable = false)
    private QuestionModel question;

    public UserModel(){}


}
