package com.tgelder.webfinance.model;

import com.tgelder.webfinance.controller.GenericGetPostController;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Entity
@Data
public class Transaction implements Identifiable {

  @Id
  @GeneratedValue
  @Null(groups = {GenericGetPostController.PostValidation.class})
  private Long id;

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
