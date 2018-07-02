package com.tgelder.webfinance.service;

import lombok.Data;

@Data
public class Balance {

  private final long lastReading;
  private final long transfersIn;
  private final long transfersOut;
  private final long commitmentsIn;
  private final long commitmentsOut;

  public long getTotal() {
    return lastReading + transfersIn + commitmentsIn - (transfersOut + commitmentsOut);
  }

}
