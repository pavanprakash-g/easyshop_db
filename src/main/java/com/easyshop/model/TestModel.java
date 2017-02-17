package com.easyshop.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


/**
 * Created by admin on 22/10/16.
 */
@Entity
@Table(name="pav")
@Getter
@Setter
@Data
public class TestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String col1;

    public TestModel(){}

    //public QuestionsModel(String title, String description, Date createdAt, Date updatedAt, int userId) {
    public TestModel( String col1) {
        this.col1 = col1;
    }
}
