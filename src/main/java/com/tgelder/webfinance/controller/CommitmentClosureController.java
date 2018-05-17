package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.CommitmentClosure;
import com.tgelder.webfinance.persistence.CommitmentClosureRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commitments/closures")
public class CommitmentClosureController extends GenericController<CommitmentClosure> {

  @RequestMapping(method = RequestMethod.POST, value = "/")
  ResponseEntity<CommitmentClosure> post(@RequestBody @Validated(PostValidation.class) CommitmentClosure commitmentClosure) {
    if (!((CommitmentClosureRepository) repository).findByCommitment(commitmentClosure.getCommitment()).isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    return super.post(commitmentClosure);
  }

}
