package com.tgelder.webfinance.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tgelder.webfinance.controller.GenericController;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Commitment extends Identifiable {

  @ManyToOne
  @NotNull(groups = {GenericController.PostValidation.class})
  private Account from;
  @ManyToOne
  @NotNull(groups = {GenericController.PostValidation.class})
  private Account to;
  @NotNull(groups = {GenericController.PostValidation.class})
  private String what;
  @NotNull(groups = {GenericController.PostValidation.class})
  private Long amount;
  @NotNull(groups = {GenericController.PostValidation.class})
  private Long epochSecond;
  @OneToOne(mappedBy = "commitment")
  @JsonManagedReference
  private CommitmentClosure closure;

  public Commitment(Account from, Account to, String what, Long amount, Long epochSecond) {
    this.from = from;
    this.to = to;
    this.what = what;
    this.amount = amount;
    this.epochSecond = epochSecond;
  }

  // Required by JPA
  Commitment() {

  }

}
