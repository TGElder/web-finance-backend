package com.tgelder.webfinance.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tgelder.webfinance.controller.GenericController;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class CommitmentClosure extends Identifiable {

  @OneToOne
  @JsonBackReference
  @NotNull(groups = {GenericController.PostValidation.class})
  private Commitment commitment;
  @NotNull(groups = {GenericController.PostValidation.class})
  private Long epochSecond;

  public CommitmentClosure(Commitment commitment, Long epochSecond) {
    this.commitment = commitment;
    this.epochSecond = epochSecond;
  }

  // Required by JPA
  CommitmentClosure() {

  }

}
