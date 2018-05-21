package com.tgelder.webfinance.service;

import com.tgelder.webfinance.App;
import com.tgelder.webfinance.model.*;
import com.tgelder.webfinance.persistence.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
@ActiveProfiles(profiles = "test")
public class BalanceServiceTest {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private ReadingRepository readingRepository;

  @Autowired
  private TransferRepository transferRepository;

  @Autowired
  private CommitmentRepository commitmentRepository;

  @Autowired
  private CommitmentClosureRepository commitmentClosureRepository;

  @Autowired
  private BalanceService balanceService;

  private Account account;
  private Account other;
  private Account another;

  @Before
  public void setup() {
    account = accountRepository.save(new Account("account"));
    other = accountRepository.save(new Account("other"));
    another = accountRepository.save(new Account("another"));
  }

  @Test
  public void noReadingShouldBeTreatedZero() {
    assertThat(balanceService.getAllBalances().get(account)).isEqualTo(0L);
  }

  @Test
  public void readingShouldAddToBalance() {
    readingRepository.save(new Reading(account, 100L, 0L));
    assertThat(balanceService.getAllBalances().get(account)).isEqualTo(100L);
  }

  @Test
  public void shouldOnlyUseLatestReading() {
    readingRepository.save(new Reading(account, 100L, 0L));
    readingRepository.save(new Reading(account, 101L, 1L));
    assertThat(balanceService.getAllBalances().get(account)).isEqualTo(101L);
  }

  @Test
  public void shouldChooseRandomlyIfTieForLatest() {
    readingRepository.save(new Reading(account, 100L, 0L));
    readingRepository.save(new Reading(account, 101L, 0L));
    assertThat(balanceService.getAllBalances().get(account)).isIn(100L, 101L);
  }

  @Test
  public void transferToShouldAddToBalance() {
    transferRepository.save(new Transfer(other, account, "in", 100L, 0L));
    assertThat(balanceService.getAllBalances().get(account)).isEqualTo(100L);
  }

  @Test
  public void transferFromShouldSubtractFromBalance() {
    transferRepository.save(new Transfer(account, other, "out", 100L, 0L));
    assertThat(balanceService.getAllBalances().get(account)).isEqualTo(-100L);
  }

  @Test
  public void transfersShouldCumulate() {
    transferRepository.save(new Transfer(other, account, "in", 100L, 0L));
    transferRepository.save(new Transfer(account, another, "out", 25L, 0L));
    assertThat(balanceService.getAllBalances().get(account)).isEqualTo(75L);
  }

  @Test
  public void transferToSelfShouldCancelOut() {
    transferRepository.save(new Transfer(account, account, "self", 100L, 0L));
    assertThat(balanceService.getAllBalances().get(account)).isEqualTo(0L);
  }

  @Test
  public void openCommitmentToShouldAddToBalance() {
    commitmentRepository.save(new Commitment(other, account, "in", 100L, 0L));
    assertThat(balanceService.getAllBalances().get(account)).isEqualTo(100L);
  }

  @Test
  public void openCommitmentFromShouldSubtractFromBalance() {
    commitmentRepository.save(new Commitment(account, other, "out", 100L, 0L));
    assertThat(balanceService.getAllBalances().get(account)).isEqualTo(-100L);
  }

  @Test
  public void openCommitmentsShouldCumulate() {
    commitmentRepository.save(new Commitment(other, account, "in", 100L, 0L));
    commitmentRepository.save(new Commitment(account, another, "out", 25L, 0L));
    assertThat(balanceService.getAllBalances().get(account)).isEqualTo(75L);
  }

  @Test
  public void closedCommitmentsShouldHaveNoAffect() {
    Commitment commitment = commitmentRepository.save(new Commitment(other, account, "in", 100L, 0L));
    commitmentClosureRepository.save(new CommitmentClosure(commitment, 0L));
    assertThat(balanceService.getAllBalances().get(account)).isEqualTo(0L);
  }

  @Test
  public void combinationTest() {
    readingRepository.save(new Reading(account, 1000L, 0L));
    readingRepository.save(new Reading(account, 2000L, 1L));
    transferRepository.save(new Transfer(other, account, "in", 100L, 2L));
    transferRepository.save(new Transfer(account, another, "out", 25L, 3L));
    commitmentRepository.save(new Commitment(other, account, "in", 10L, 4L));
    Commitment commitment = commitmentRepository.save(new Commitment(account, another, "out", 3L, 5L));
    commitmentRepository.save(commitment);
    commitmentClosureRepository.save(new CommitmentClosure(commitment, 6L));
    assertThat(balanceService.getAllBalances().get(account)).isEqualTo(2085L);
  }


}
