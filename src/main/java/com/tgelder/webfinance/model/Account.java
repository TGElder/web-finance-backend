package com.tgelder.webfinance.model;

import com.tgelder.webfinance.controller.GenericGetPostController;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Entity
@Data
public class Account implements Identifiable {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  @Null(groups = {GenericGetPostController.PostValidation.class})
  private Long id;

  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private String name;

  public Account(String name) {
    this.name = name;
  }

  // Required by JPA
  public Account() {

  }

}
