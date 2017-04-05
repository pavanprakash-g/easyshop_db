package com.easyshop.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin-hp on 2/4/17.
 */
@Getter
@Setter
@Entity
@Table(name = "subs_next_due_date")
@Data
public class NextDueDateModel {
    @Id
    @NotNull
    private Long subsOrderId;

    @NotNull
    private int subscriptionType; // 1: 1 month; 2: 2 months; 3: 3 months; 6: 6 months; 12: 12 months

    @NotNull
    private Calendar nextDueDate;
}
