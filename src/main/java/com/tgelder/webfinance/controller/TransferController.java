package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Transfer;
import com.tgelder.webfinance.persistence.TransactionRepository;
import com.tgelder.webfinance.persistence.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfers")
public class TransferController extends TransactionController<Transfer> {

  @Autowired
  private TransferRepository transferRepository;

  @Override
  TransactionRepository<Transfer> getTransactionRepository() {
    return transferRepository;
  }

}
