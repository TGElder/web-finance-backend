package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.persistence.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/accounts")
public class AccountController {

  private AccountRepository accountRepository;

  @Autowired
  AccountController(AccountRepository birdRepository) {
    this.accountRepository = birdRepository;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  ResponseEntity<Account> getAccount(@PathVariable Long id) {
    return this.accountRepository.findById(id)
                                 .map(ResponseEntity::ok)
                                 .orElse(ResponseEntity.notFound().build());
  }

  @RequestMapping(method = RequestMethod.GET, value = "/")
  ResponseEntity<Iterable<Account>> getAccounts() {
    return ResponseEntity.ok(this.accountRepository.findAll());
  }

  @RequestMapping(method = RequestMethod.POST, value = "/")
  ResponseEntity<Account> addAccount(@RequestBody @Validated(Account.PostValidation.class) Account account) {
    Account result = accountRepository.save(account);
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(result.getId()).toUri();
    return ResponseEntity.created(location).build();
  }

}
