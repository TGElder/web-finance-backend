package com.tgelder.webfinance.model;

import com.tgelder.webfinance.controller.GenericController;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Reading extends Identifiable {

  @ManyToOne
  @NotNull(groups = {GenericController.PostValidation.class})
  private Account account;
  @NotNull(groups = {GenericController.PostValidation.class})
  private Long amount;
  @NotNull(groups = {GenericController.PostValidation.class})
  private Long epochSecond;

  public Reading(Account account, Long amount, Long epochSecond) {
    this.account = account;
    this.amount = amount;
    this.epochSecond = epochSecond;
  }

  // Required by JPA
  Reading() {

  }

}
