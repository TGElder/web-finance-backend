package com.tgelder.webfinance.persistence;

import com.tgelder.webfinance.model.Commitment;
import com.tgelder.webfinance.model.CommitmentClosure;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommitmentClosureRepository extends CrudRepository<CommitmentClosure, Long> {

  List<CommitmentClosure> findByCommitment(Commitment commitment);

}
