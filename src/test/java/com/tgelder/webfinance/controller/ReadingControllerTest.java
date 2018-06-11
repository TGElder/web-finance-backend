package com.tgelder.webfinance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tgelder.webfinance.App;
import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.Reading;
import com.tgelder.webfinance.persistence.AccountRepository;
import com.tgelder.webfinance.persistence.ReadingRepository;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
@ActiveProfiles(profiles = "test")
public class ReadingControllerTest {


  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private ReadingRepository readingRepository;

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

  private List<Reading> testReadings = ImmutableList.of(
          new Reading(testAccounts.get(0), 12345L, 60L),
          new Reading(testAccounts.get(1), 2345L, 61L));

  @Before
  public void setup() {
    mockMvc = webAppContextSetup(webApplicationContext).build();
    accountRepository.saveAll(testAccounts);
    readingRepository.saveAll(testReadings);
  }

  @After
  public void tearDown() {
    readingRepository.deleteAll();
    accountRepository.deleteAll();
  }

  @Test
  public void shouldGetReading() throws Exception {
    mockMvc.perform(get("/readings/" + testReadings.get(0).getId()))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.id", is(testReadings.get(0).getId().intValue())))
           .andExpect(jsonPath("$.account.name", is("savings")))
           .andExpect(jsonPath("$.amount", is(12345)))
           .andExpect(jsonPath("$.epochSecond", is(60)));
  }

  @Test
  public void shouldGetReadings() throws Exception {
    mockMvc.perform(get("/readings"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.[*].account.name", containsInAnyOrder("savings", "personal")));
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void shouldPostReading() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(
            ImmutableMap.of("account", ImmutableMap.of("id", testAccounts.get(0).getId()),
                            "amount", 1234,
                            "epochSecond", 70));

    MvcResult result = mockMvc.perform(post("/readings").contentType(contentType).content(json))
                              .andExpect(status().isCreated())
                              .andReturn();

    mockMvc.perform(get(result.getResponse().getHeader("Location")))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.account.id", is(testAccounts.get(0).getId().intValue())))
           .andExpect(jsonPath("$.account.name", is("savings")))
           .andExpect(jsonPath("$.amount", is(1234)))
           .andExpect(jsonPath("$.epochSecond", is(70)));
  }

  @Test
  public void shouldRejectReadingWithFieldMissing() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(
            ImmutableMap.of("account", ImmutableMap.of("id", testAccounts.get(0).getId()),
                            "amount", 1234));

    mockMvc.perform(post("/readings").contentType(contentType).content(json))
           .andExpect(status().is4xxClientError())
           .andReturn();
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void shouldIgnoreExtraFieldInReading() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(
            ImmutableMap.of("account", ImmutableMap.of("id", testAccounts.get(0).getId()),
                            "amount", 1234,
                            "epochSecond", 70,
                            "extra", "field"));

    MvcResult result = mockMvc.perform(post("/readings").contentType(contentType).content(json))
                              .andExpect(status().isCreated())
                              .andReturn();

    mockMvc.perform(get(result.getResponse().getHeader("Location")))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.extra").doesNotExist());
  }

  @Test
  public void shouldRejectReadingWithProvidedId() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(
            ImmutableMap.of("id", 1,
                            "account", ImmutableMap.of("id", testAccounts.get(0).getId()),
                            "amount", 1234,
                            "epochSecond", 70));

    mockMvc.perform(post("/readings").contentType(contentType).content(json))
           .andExpect(status().is4xxClientError())
           .andReturn();

  }


}
