package com.tgelder.webfinance.persistence;

import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository<T extends Transaction> extends CrudRepository<T, Long> {

  List<T> findByFrom(Account account);

  List<T> findByTo(Account account);

}
