package com.easyshop.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by pavan on 2/17/17.
 */
@Getter
@Setter
@Entity
@Table(name = "cust_messages")
@Data
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @NotNull
    private long custId;

    @NotNull
    private String messageContent;

    @NotNull
    private boolean isRead;

    @NotNull
    @DateTimeFormat
    private Date messageTime;

    public MessageModel(){}
}
