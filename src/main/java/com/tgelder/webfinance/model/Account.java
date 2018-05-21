package com.tgelder.webfinance.model;

import com.tgelder.webfinance.controller.GenericGetPostController;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Account extends Identifiable {

  @NotNull(groups = {GenericGetPostController.PostValidation.class})
  private String name;

  public Account(String name) {
    this.name = name;
  }

  // Required by JPA
  public Account() {

  }

}
