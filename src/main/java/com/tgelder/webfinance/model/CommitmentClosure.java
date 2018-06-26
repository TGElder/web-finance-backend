package com.tgelder.webfinance.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tgelder.webfinance.controller.GenericGetPostController;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Entity
@Data
public class CommitmentClosure implements Identifiable {

  @Id
  @GeneratedValue
  @Null(groups = {GenericGetPostController.PostValidation.class})
  private Long id;

  @OneToOne
  @JsonBackReference
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private Commitment commitment;
  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private Long epochSecond;

  public CommitmentClosure(Commitment commitment, Long epochSecond) {
    this.commitment = commitment;
    this.epochSecond = epochSecond;
  }

  // Required by JPA
  CommitmentClosure() {

  }

}
