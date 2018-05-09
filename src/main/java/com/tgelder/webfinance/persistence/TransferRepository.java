package com.tgelder.webfinance.persistence;

import com.tgelder.webfinance.model.Transfer;
import org.springframework.data.repository.CrudRepository;

public interface TransferRepository extends CrudRepository<Transfer, Long> {

}
