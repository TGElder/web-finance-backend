package com.tgelder.webfinance.model;

import com.tgelder.webfinance.service.Balance;
import lombok.Data;

@Data
public class GetAccount {

  private final Long id;
  private final String name;
  private final Balance balance;

}
