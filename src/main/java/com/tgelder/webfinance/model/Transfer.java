package com.tgelder.webfinance.model;

import com.tgelder.webfinance.controller.GenericGetPostController;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Transfer extends Identifiable {

  @ManyToOne
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private Account from;
  @ManyToOne
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private Account to;
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private String what;
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private Long amount;
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private Long epochSecond;

  public Transfer(Account from, Account to, String what, Long amount, Long epochSecond) {
    this.from = from;
    this.to = to;
    this.what = what;
    this.amount = amount;
    this.epochSecond = epochSecond;
  }

  // Required by JPA
  Transfer() {

  }

}
