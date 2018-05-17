package com.tgelder.webfinance.persistence;

import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.Commitment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommitmentRepository extends CrudRepository<Commitment, Long> {

  List<Commitment> findByFrom(Account account);

  List<Commitment> findByTo(Account account);

}
