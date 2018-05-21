package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.CommitmentClosure;
import com.tgelder.webfinance.persistence.CommitmentClosureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/commitments/close")
public class CommitmentClosureController {

  @Autowired
  private CrudRepository<CommitmentClosure, Long> repository;

  @RequestMapping(method = RequestMethod.POST, value = "/")
  ResponseEntity<CommitmentClosure> post(@RequestBody @Validated(GenericGetPostController.PostValidation.class) CommitmentClosure commitmentClosure) {
    if (!((CommitmentClosureRepository) repository).findByCommitment(commitmentClosure.getCommitment()).isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    CommitmentClosure result = repository.save(commitmentClosure);
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(commitmentClosure.getId()).toUri();
    return ResponseEntity.created(location).build();
  }


}
