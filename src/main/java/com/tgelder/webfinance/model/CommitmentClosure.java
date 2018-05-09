package com.tgelder.webfinance.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@Data
public class CommitmentClosure {

  @Id
  private long id;
  @MapsId
  @OneToOne
  private Commitment commitment;
  private Date closed;

  public CommitmentClosure(Commitment commitment, Date closed) {
    this.commitment = commitment;
    this.closed = closed;
  }

  // Required by JPA
  CommitmentClosure() {

  }

}
