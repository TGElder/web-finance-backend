package com.tgelder.webfinance.model;

import com.tgelder.webfinance.controller.GenericGetPostController;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Transfer extends Transaction {


  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private String what;
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  @Min(value = 0L, groups = {GenericGetPostController.PostValidation.class})
  private Long amount;
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private Long epochSecond;

  public Transfer(Account from, Account to, String what, Long amount, Long epochSecond) {
    super(from, to);
    this.what = what;
    this.amount = amount;
    this.epochSecond = epochSecond;
  }

  // Required by JPA
  Transfer() {

  }

}
