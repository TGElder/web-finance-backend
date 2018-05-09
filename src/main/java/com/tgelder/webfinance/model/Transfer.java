package com.tgelder.webfinance.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data
public class Transfer {

  @Id
  @GeneratedValue
  private Long id;
  @ManyToOne
  private Account from;
  @ManyToOne
  private Account to;
  private String what;
  private long amount;
  private Date added; // Java 8 DateTime not supported in current Spring (needs JPA 2.2)

  public Transfer(Account from, Account to, String what, long amount, Date added) {
    this.from = from;
    this.to = to;
    this.what = what;
    this.amount = amount;
    this.added = added;
  }

  // Required by JPA
  Transfer() {

  }

}
