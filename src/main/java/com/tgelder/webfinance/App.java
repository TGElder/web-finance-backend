package com.tgelder.webfinance;


import com.tgelder.webfinance.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.tgelder.webfinance.persistence.AccountRepository;

@SpringBootApplication
@Slf4j
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner demo(AccountRepository repository) {
    return (args) -> {
      // save a couple of accounts
      repository.save(new Account("Seven"));
      repository.save(new Account("Personal"));
    };
  }


}