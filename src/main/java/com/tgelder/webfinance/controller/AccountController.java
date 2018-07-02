package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.GetAccount;
import com.tgelder.webfinance.persistence.AccountRepository;
import com.tgelder.webfinance.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/accounts")
public class AccountController {

  @Autowired
  private AccountRepository repository;

  @Autowired
  private BalanceService balanceService;

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  ResponseEntity<GetAccount> get(@PathVariable Long id) {
    return this.repository.findById(id)
                          .map(this::createGetAccount)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
  }

  @RequestMapping(method = RequestMethod.GET, value = "")
  ResponseEntity<Iterable<GetAccount>> getAll() {
    return ResponseEntity.ok(StreamSupport.stream(this.repository.findAll().spliterator(), true)
                                          .map(this::createGetAccount)
                                          .collect(Collectors.toList()));
  }

  @RequestMapping(method = RequestMethod.POST, value = "")
  ResponseEntity<Account> post(@RequestBody @Validated(GenericGetPostController.PostValidation.class) Account t) {
    repository.save(t);
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(t.getId()).toUri();
    return ResponseEntity.created(location).build();
  }

  // For validation groups
  public interface PostValidation {

  }

  private GetAccount createGetAccount(Account account) {
    return new GetAccount(account.getId(), account.getName(), balanceService.getAllBalances().get(account));
  }

}
