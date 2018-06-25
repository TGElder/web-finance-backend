package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.Commitment;
import com.tgelder.webfinance.persistence.CommitmentRepository;
import com.tgelder.webfinance.persistence.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/commitments")
public class CommitmentController extends TransactionController<Commitment> {

  @Autowired
  private CommitmentRepository commitmentRepository;

  @Override
  TransactionRepository<Commitment> getTransactionRepository() {
    return commitmentRepository;
  }

  @RequestMapping(method = RequestMethod.GET, value = "")
  ResponseEntity<Iterable<Commitment>> getAll(@RequestParam(value = "account", required = false) Long accountId,
                                              @RequestParam(value = "closed", required = false) Boolean closed) {

    List<Commitment> out = new ArrayList<>();

    if (accountId == null) {
      this.getTransactionRepository().findAll().forEach(out::add);
    } else {
      Optional<Account> account = this.accountRepository.findById(accountId);

      if (account.isPresent()) {
        out.addAll(this.getTransactionRepository().findByFrom(account.get()));
        out.addAll(this.getTransactionRepository().findByTo(account.get()));
      } else {
        return ResponseEntity.notFound().build();
      }
    }

    if (closed != null) {
      out = out.stream()
               .filter(commitment -> (closed == (commitment.getClosure() != null)))
               .collect(Collectors.toList());
    }

    return ResponseEntity.ok(out);
  }

}
