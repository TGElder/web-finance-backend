package com.tgelder.webfinance.persistence;

import com.tgelder.webfinance.model.Commitment;
import org.springframework.data.repository.CrudRepository;

public interface CommitmentRepository extends CrudRepository<Commitment, Long> {

}
