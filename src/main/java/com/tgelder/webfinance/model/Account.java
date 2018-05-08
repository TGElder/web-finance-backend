package com.tgelder.webfinance.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Account {

  @Id
  @GeneratedValue
  private Long id;
  private String name;

  public Account(String name) {
    this.name = name;
  }

  // Required by JPA
  public Account() {

  }

}
