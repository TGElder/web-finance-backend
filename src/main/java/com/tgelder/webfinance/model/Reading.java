package com.tgelder.webfinance.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data
public class Reading {

  @Id
  @GeneratedValue
  private Long id;
  @ManyToOne
  private Account from;
  private long Amount;
  private Date date; // Java 8 DateTime not supported in current Spring (needs JPA 2.2)

  public Reading(Account from, long amount, Date date) {
    this.from = from;
    Amount = amount;
    this.date = date;
  }

  // Required by JPA
  Reading() {

  }
}
