package com.tgelder.webfinance.persistence;

import com.tgelder.webfinance.model.Reading;
import org.springframework.data.repository.CrudRepository;

public interface ReadingRepository extends CrudRepository<Reading, Long> {

}
