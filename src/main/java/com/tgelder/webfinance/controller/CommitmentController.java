package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Commitment;
import com.tgelder.webfinance.persistence.CommitmentRepository;
import com.tgelder.webfinance.persistence.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commitments")
public class CommitmentController extends TransactionController<Commitment> {

  @Autowired
  private CommitmentRepository commitmentRepository;

  @Override
  TransactionRepository<Commitment> getTransactionRepository() {
    return commitmentRepository;
  }
}
