package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class GenericGetPostController<T extends Identifiable> {

  @Autowired
  private CrudRepository<T, Long> repository;

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  ResponseEntity<T> get(@PathVariable Long id) {
    return this.repository.findById(id)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
  }

  @RequestMapping(method = RequestMethod.GET, value = "")
  ResponseEntity<Iterable<T>> getAll() {
    return ResponseEntity.ok(this.repository.findAll());
  }

  @RequestMapping(method = RequestMethod.POST, value = "")
  ResponseEntity<T> post(@RequestBody @Validated(PostValidation.class) T t) {
    T result = repository.save(t);
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(t.getId()).toUri();
    return ResponseEntity.created(location).build();
  }

  // For validation groups
  public interface PostValidation {

  }

}
