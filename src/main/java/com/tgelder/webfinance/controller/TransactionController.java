package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.Transaction;
import com.tgelder.webfinance.persistence.AccountRepository;
import com.tgelder.webfinance.persistence.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

abstract class TransactionController<T extends Transaction> {

  @Autowired
  private AccountRepository accountRepository;

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  ResponseEntity<T> get(@PathVariable Long id) {
    return this.getTransactionRepository().findById(id)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
  }


  @RequestMapping(method = RequestMethod.GET, value = "")
  ResponseEntity<Iterable<T>> getAll(@RequestParam(value = "account", required = false) Long accountId) {

    if (accountId == null) {
      return ResponseEntity.ok(this.getTransactionRepository().findAll());
    }

    Optional<Account> account = accountRepository.findById(accountId);

    if (account.isPresent()) {
      List<T> out = new ArrayList<>();
      out.addAll(this.getTransactionRepository().findByFrom(account.get()));
      out.addAll(this.getTransactionRepository().findByTo(account.get()));
      return ResponseEntity.ok(out);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @RequestMapping(method = RequestMethod.POST, value = "")
  ResponseEntity<T> post(@RequestBody @Validated(GenericGetPostController.PostValidation.class) T t) {
    this.getTransactionRepository().save(t);
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(t.getId()).toUri();
    return ResponseEntity.created(location).build();
  }

  abstract TransactionRepository<T> getTransactionRepository();

}
