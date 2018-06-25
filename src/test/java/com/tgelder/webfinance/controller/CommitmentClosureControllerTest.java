package com.tgelder.webfinance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tgelder.webfinance.App;
import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.Commitment;
import com.tgelder.webfinance.model.CommitmentClosure;
import com.tgelder.webfinance.persistence.AccountRepository;
import com.tgelder.webfinance.persistence.CommitmentClosureRepository;
import com.tgelder.webfinance.persistence.CommitmentRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
@ActiveProfiles(profiles = "test")
public class CommitmentClosureControllerTest {


  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private CommitmentRepository commitmentRepository;
  @Autowired
  private CommitmentClosureRepository commitmentClosureRepository;

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                MediaType.APPLICATION_JSON.getSubtype(),
                                                Charset.forName("utf8"));

  private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private List<Account> testAccounts = ImmutableList.of(
          new Account("savings"),
          new Account("personal")
  );

  private List<Commitment> testCommitments = ImmutableList.of(
          new Commitment(testAccounts.get(0),
                         testAccounts.get(1),
                         "holiday",
                         12345L,
                         60L),
          new Commitment(testAccounts.get(1),
                         testAccounts.get(0),
                         "bonus",
                         543L,
                         61L),
          new Commitment(testAccounts.get(0),
                         testAccounts.get(1),
                         "present",
                         808L,
                         59L));

  private List<CommitmentClosure> testCommitmentClosures = ImmutableList.of(
          new CommitmentClosure(
                  testCommitments.get(0),
                  62L),
          new CommitmentClosure(
                  testCommitments.get(1),
                  64L));

  @Before
  public void setup() {
    mockMvc = webAppContextSetup(webApplicationContext).build();
    accountRepository.saveAll(testAccounts);
    commitmentRepository.saveAll(testCommitments);
    commitmentClosureRepository.saveAll(testCommitmentClosures);
  }

  @After
  public void tearDown() {
    commitmentClosureRepository.deleteAll();
    commitmentRepository.deleteAll();
    accountRepository.deleteAll();
  }

  @Test
  public void shouldShowClosureOnCommitmentWithClosure() throws Exception {
    mockMvc.perform(get("/commitments/" + testCommitments.get(0).getId()))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.closure.id", is(testCommitmentClosures.get(0).getId().intValue())))
           .andExpect(jsonPath("$.closure.epochSecond", is(62)));
  }

  @Test
  public void shouldShowNullOnCommitmentWithNoClosure() throws Exception {
    mockMvc.perform(get("/commitments/" + testCommitments.get(2).getId()))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.closure", isEmptyOrNullString()));
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testPostCommitmentClosure() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(
            ImmutableMap.of("commitment", ImmutableMap.of("id", testCommitments.get(2).getId()),
                            "epochSecond", 64));

    mockMvc.perform(post("/commitments/close").contentType(contentType).content(json))
           .andExpect(status().isCreated());

    mockMvc.perform(get("/commitments/" + testCommitments.get(2).getId()))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.closure.epochSecond", is(64)));
  }

  @Test
  public void shouldRejectCommitmentClosureWhenCommitmentAlreadyClosed() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(
            ImmutableMap.of("commitment", ImmutableMap.of("id", testCommitments.get(0).getId()),
                            "epochSecond", 64));

    mockMvc.perform(post("/commitments/close").contentType(contentType).content(json))
           .andExpect(status().is4xxClientError());
  }

  @Test
  public void shouldRejectCommitmentClosureWithFieldMissing() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(
            ImmutableMap.of("commitment", ImmutableMap.of("id", testCommitments.get(2).getId())));

    mockMvc.perform(post("/commitments/close").contentType(contentType).content(json))
           .andExpect(status().is4xxClientError());
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void shouldIgnoreExtraFieldInCommitmentClosure() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(
            ImmutableMap.of("commitment", ImmutableMap.of("id", testCommitments.get(2).getId()),
                            "epochSecond", 64,
                            "extra", "field"));

    mockMvc.perform(post("/commitments/close").contentType(contentType).content(json))
           .andExpect(status().isCreated());

    mockMvc.perform(get("/commitments/" + testCommitments.get(2).getId()))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.closure.epochSecond", is(64)));
  }

  @Test
  public void shouldRejectCommitmentClosureWithProvidedId() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(
            ImmutableMap.of("id", 1,
                            "commitment", ImmutableMap.of("id", testCommitments.get(2).getId()),
                            "epochSecond", 64));


    mockMvc.perform(post("/commitments/close").contentType(contentType).content(json))
           .andExpect(status().is4xxClientError());

  }


}
