package com.tgelder.webfinance.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Entity
@Data
public class Account {

  @Id
  @GeneratedValue
  @Null(groups = {PostValidation.class})
  private Long id;
  @NotNull(groups = {PostValidation.class})
  private String name;

  public Account(String name) {
    this.name = name;
  }

  // Required by JPA
  public Account() {

  }

  // For validation groups
  public interface PostValidation {

  }

}
