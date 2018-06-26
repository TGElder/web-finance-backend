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
public class Reading implements Identifiable {

  @Id
  @GeneratedValue
  @Null(groups = {GenericGetPostController.PostValidation.class})
  private Long id;

  @ManyToOne
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private Account account;
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private Long amount;
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
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
