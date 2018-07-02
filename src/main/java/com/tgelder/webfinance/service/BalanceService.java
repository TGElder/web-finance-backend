package com.tgelder.webfinance.service;

import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.Commitment;
import com.tgelder.webfinance.model.Reading;
import com.tgelder.webfinance.model.Transfer;
import com.tgelder.webfinance.persistence.AccountRepository;
import com.tgelder.webfinance.persistence.CommitmentRepository;
import com.tgelder.webfinance.persistence.ReadingRepository;
import com.tgelder.webfinance.persistence.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BalanceService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private ReadingRepository readingRepository;

  @Autowired
  private TransferRepository transferRepository;

  @Autowired
  private CommitmentRepository commitmentRepository;

  public Map<Account, Balance> getAllBalances() {

    Iterable<Account> accounts = accountRepository.findAll();

    Map<Account, Long> latestReadings = getLatestReadings();
    Map<Account, Long> transfersFrom = getTransfersFrom();
    Map<Account, Long> transfersTo = getTransfersTo();
    Map<Account, Long> commitmentsFrom = getCommitmentsFrom();
    Map<Account, Long> commitmentsTo = getCommitmentsTo();

    return StreamSupport.stream(accounts.spliterator(), false)
                        .collect(Collectors.toMap(account -> account,
                                                  account -> new Balance(
                                                          latestReadings.getOrDefault(account, 0L),
                                                          transfersTo.getOrDefault(account, 0L),
                                                          transfersFrom.getOrDefault(account, 0L),
                                                          commitmentsTo.getOrDefault(account, 0L),
                                                          commitmentsFrom.getOrDefault(account, 0L))
                        ));
  }

  private Map<Account, Long> getLatestReadings() {
    Map<Account, Optional<Reading>> latestReadings = StreamSupport.stream(readingRepository.findAll().spliterator(), false)
                                                                  .collect(Collectors.groupingBy(Reading::getAccount,
                                                                                                 Collectors.maxBy(Comparator.comparing(Reading::getAmount))));
    return latestReadings.entrySet().stream()
                         .collect(Collectors.toMap(Map.Entry::getKey,
                                                   entry -> entry.getValue().map(Reading::getAmount).orElse(0L)));

  }

  private Map<Account, Long> getTransfersFrom() {
    return StreamSupport.stream(transferRepository.findAll().spliterator(), false)
                        .collect(Collectors.groupingBy(Transfer::getFrom,
                                                       Collectors.summingLong(Transfer::getAmount)));

  }

  private Map<Account, Long> getTransfersTo() {
    return StreamSupport.stream(transferRepository.findAll().spliterator(), false)
                        .collect(Collectors.groupingBy(Transfer::getTo,
                                                       Collectors.summingLong(Transfer::getAmount)));

  }


  private Map<Account, Long> getCommitmentsFrom() {
    return StreamSupport.stream(commitmentRepository.findAll().spliterator(), false)
                        .filter(commitment -> commitment.getClosure() == null)
                        .collect(Collectors.groupingBy(Commitment::getFrom,
                                                       Collectors.summingLong(Commitment::getAmount)));

  }

  private Map<Account, Long> getCommitmentsTo() {
    return StreamSupport.stream(commitmentRepository.findAll().spliterator(), false)
                        .filter(commitment -> commitment.getClosure() == null)
                        .collect(Collectors.groupingBy(Commitment::getTo,
                                                       Collectors.summingLong(Commitment::getAmount)));

  }


}
