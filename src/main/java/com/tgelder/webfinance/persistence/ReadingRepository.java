package com.tgelder.webfinance.persistence;

import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.Reading;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReadingRepository extends CrudRepository<Reading, Long> {

  List<Reading> findByAccount(Account account);

}
