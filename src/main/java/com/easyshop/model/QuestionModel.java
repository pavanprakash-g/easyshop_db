package com.easyshop.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by pavan on 2/7/17.
 */
@Entity
@Table(name="questions")
@Getter
@Setter
@Data
public class QuestionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long securityQuesId;

    @NotNull
    private String securityQuesDescription;

    QuestionModel(){}
}
