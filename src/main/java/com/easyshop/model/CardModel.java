package com.easyshop.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by pavan on 2/19/17.
 */
@Getter
@Setter
@Data
@Table(name = "card")
@Entity
public class CardModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @NotNull
    private int custId;

    @NotNull
    private String cardNumber;

    @NotNull
    private int cardCvv;

    @NotNull
    private int cardExpMon;

    @NotNull
    private int cardExpYr;

    public CardModel(){}

}
