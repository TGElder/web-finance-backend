package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.Reading;
import com.tgelder.webfinance.persistence.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/readings")
public class ReadingController {

  private ReadingRepository readingRepository;

  @Autowired
  ReadingController(ReadingRepository readingRepository) {
    this.readingRepository = readingRepository;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  ResponseEntity<Reading> getReading(@PathVariable Long id) {
    return this.readingRepository.findById(id)
                                 .map(ResponseEntity::ok)
                                 .orElse(ResponseEntity.notFound().build());
  }

  @RequestMapping(method = RequestMethod.GET, value = "/")
  ResponseEntity<Iterable<Reading>> getReadings() {
    return ResponseEntity.ok(this.readingRepository.findAll());
  }

  @RequestMapping(method = RequestMethod.POST, value = "/")
  ResponseEntity<Account> addAccount(@RequestBody @Validated(Reading.PostValidation.class) Reading reading) {
    Reading result = readingRepository.save(reading);
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(result.getId()).toUri();
    return ResponseEntity.created(location).build();
  }

}
