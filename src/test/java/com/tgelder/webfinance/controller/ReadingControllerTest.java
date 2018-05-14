package com.tgelder.webfinance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.tgelder.webfinance.App;
import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.Reading;
import com.tgelder.webfinance.persistence.AccountRepository;
import com.tgelder.webfinance.persistence.ReadingRepository;
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
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

  private String testDateString = "2018-05-14T18:03:07.537Z";
  private final Date testDate = Date.from(Instant.parse(testDateString));

  private List<Account> testAccounts = ImmutableList.of(
          new Account("savings"),
          new Account("personal")
  );

  private List<Reading> testReadings = ImmutableList.of(
          new Reading(testAccounts.get(0), 12345, testDate),
          new Reading(testAccounts.get(1), 2345, testDate));

  @Before
  public void setup() {
    mockMvc = webAppContextSetup(webApplicationContext).build();
    accountRepository.deleteAll();
    accountRepository.saveAll(testAccounts);
    readingRepository.deleteAll();
    readingRepository.saveAll(testReadings);
  }

  @Test
  public void testGetReading() throws Exception {
    mockMvc.perform(get("/readings/" + testReadings.get(0).getId()))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.id", is(testReadings.get(0).getId().intValue())))
           .andExpect(jsonPath("$.account", is("savings")))
           .andExpect(jsonPath("$.amount", is(12345)))
           .andExpect(jsonPath("$.date", is(testDateString)));
  }
//
//  @Test
//  public void testGetReadings() throws Exception {
//    mockMvc.perform(get("/accounts/"))
//           .andExpect(status().isOk())
//           .andExpect(content().contentType(contentType))
//           .andExpect(jsonPath("$.[*].name", containsInAnyOrder("savings", "personal")));
//  }
//
//  @Test
//  public void testPostReadingWithDate() throws Exception {
//    String json = OBJECT_MAPPER.writeValueAsString(ImmutableMap.of("name", "new"));
//
//    MvcResult result = mockMvc.perform(post("/accounts/").contentType(contentType).content(json))
//                              .andExpect(status().isCreated())
//                              .andReturn();
//
//    mockMvc.perform(get(result.getResponse().getHeader("Location")))
//           .andExpect(status().isOk())
//           .andExpect(content().contentType(contentType))
//           .andExpect(jsonPath("$.name", is("new")));
//  }
//
//  @Test
//  public void testPostReadingWithoutDate() throws Exception {
//    String json = OBJECT_MAPPER.writeValueAsString(ImmutableMap.of("name", "new"));
//
//    MvcResult result = mockMvc.perform(post("/accounts/").contentType(contentType).content(json))
//                              .andExpect(status().isCreated())
//                              .andReturn();
//
//    mockMvc.perform(get(result.getResponse().getHeader("Location")))
//           .andExpect(status().isOk())
//           .andExpect(content().contentType(contentType))
//           .andExpect(jsonPath("$.name", is("new")));
//  }
//
//  @Test
//  public void testPostReadingNonexistentAccount() throws Exception {
//
//  }
//
//  @Test
//  public void testPostReadingWithoutAccount() throws Exception {
//    String json = OBJECT_MAPPER.writeValueAsString(ImmutableMap.of("name", "new"));
//
//    MvcResult result = mockMvc.perform(post("/accounts/").contentType(contentType).content(json))
//                              .andExpect(status().isCreated())
//                              .andReturn();
//
//    mockMvc.perform(get(result.getResponse().getHeader("Location")))
//           .andExpect(status().isOk())
//           .andExpect(content().contentType(contentType))
//           .andExpect(jsonPath("$.name", is("new")));
//  }
//
//  @Test
//  public void testPostReadingWithoutBalance() throws Exception {
//    String json = OBJECT_MAPPER.writeValueAsString(ImmutableMap.of("name", "new"));
//
//    MvcResult result = mockMvc.perform(post("/accounts/").contentType(contentType).content(json))
//                              .andExpect(status().isCreated())
//                              .andReturn();
//
//    mockMvc.perform(get(result.getResponse().getHeader("Location")))
//           .andExpect(status().isOk())
//           .andExpect(content().contentType(contentType))
//           .andExpect(jsonPath("$.name", is("new")));
//  }


}
