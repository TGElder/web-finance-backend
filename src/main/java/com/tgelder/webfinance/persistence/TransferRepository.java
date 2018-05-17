package com.tgelder.webfinance.persistence;

import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.Transfer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransferRepository extends CrudRepository<Transfer, Long> {

  List<Transfer> findByFrom(Account account);

  List<Transfer> findByTo(Account account);

}
