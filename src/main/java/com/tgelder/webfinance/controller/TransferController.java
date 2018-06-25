package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.Transfer;
import com.tgelder.webfinance.persistence.TransactionRepository;
import com.tgelder.webfinance.persistence.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transfers")
public class TransferController extends TransactionController<Transfer> {

  @Autowired
  private TransferRepository transferRepository;

  @Override
  TransactionRepository<Transfer> getTransactionRepository() {
    return transferRepository;
  }

  @RequestMapping(method = RequestMethod.GET, value = "")
  ResponseEntity<Iterable<Transfer>> getAll(@RequestParam(value = "account", required = false) Long accountId) {

    if (accountId == null) {
      return ResponseEntity.ok(this.getTransactionRepository().findAll());
    }

    Optional<Account> account = this.accountRepository.findById(accountId);

    if (account.isPresent()) {
      List<Transfer> out = new ArrayList<>();
      out.addAll(this.getTransactionRepository().findByFrom(account.get()));
      out.addAll(this.getTransactionRepository().findByTo(account.get()));
      return ResponseEntity.ok(out);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
