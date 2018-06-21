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
public class Transaction extends Identifiable {

  @ManyToOne
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private Account from;
  @ManyToOne
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private Account to;

  Transaction(Account from, Account to) {
    this.from = from;
    this.to = to;
  }

  Transaction() {

  }

}
