package com.tgelder.webfinance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@Entity
@Data
public class Reading {

  @Id
  @GeneratedValue
  @Null(groups = {Account.PostValidation.class})
  private Long id;
  @ManyToOne
  @NotNull(groups = {Account.PostValidation.class})
  private Account account;
  @NotNull(groups = {Account.PostValidation.class})
  private long amount;
  @NotNull(groups = {Account.PostValidation.class})
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  private Date date; // Java 8 DateTime not supported in current Spring (needs JPA 2.2)

  public Reading(Account account, long amount, Date date) {
    this.account = account;
    this.amount = amount;
    this.date = date;
  }

  // Required by JPA
  Reading() {

  }

  // For validation groups
  public interface PostValidation {

  }

}
