package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Transaction;
import com.tgelder.webfinance.persistence.AccountRepository;
import com.tgelder.webfinance.persistence.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

abstract class TransactionController<T extends Transaction> {

  @Autowired
  AccountRepository accountRepository;

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  ResponseEntity<T> get(@PathVariable Long id) {
    return this.getTransactionRepository().findById(id)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
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
