package com.tgelder.webfinance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tgelder.webfinance.App;
import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.model.Transfer;
import com.tgelder.webfinance.persistence.AccountRepository;
import com.tgelder.webfinance.persistence.TransferRepository;
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
public class TransferControllerTest {


  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private TransferRepository transferRepository;

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

  private List<Transfer> testTransfers = ImmutableList.of(
          new Transfer(testAccounts.get(0),
                       testAccounts.get(1),
                       "holiday",
                       12345L,
                       60L),
          new Transfer(testAccounts.get(1),
                       testAccounts.get(0),
                       "bonus",
                       543L,
                       61L));

  @Before
  public void setup() {
    mockMvc = webAppContextSetup(webApplicationContext).build();
    accountRepository.saveAll(testAccounts);
    transferRepository.saveAll(testTransfers);
  }

  @After
  public void tearDown() {
    transferRepository.deleteAll();
    accountRepository.deleteAll();
  }

  @Test
  public void testGetTransfer() throws Exception {
    mockMvc.perform(get("/transfers/" + testTransfers.get(0).getId()))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.id", is(testTransfers.get(0).getId().intValue())))
           .andExpect(jsonPath("$.from.name", is("savings")))
           .andExpect(jsonPath("$.to.name", is("personal")))
           .andExpect(jsonPath("$.what", is("holiday")))
           .andExpect(jsonPath("$.amount", is(12345)))
           .andExpect(jsonPath("$.epochSecond", is(60)));
  }

  @Test
  public void testGetTransfers() throws Exception {
    mockMvc.perform(get("/transfers/"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.[*].amount", containsInAnyOrder(12345, 543)));
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testPostTransfer() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(
            ImmutableMap.of("from", ImmutableMap.of("id", testAccounts.get(0).getId()),
                            "to", ImmutableMap.of("id", testAccounts.get(1).getId()),
                            "what", "gift",
                            "amount", 777,
                            "epochSecond", 80));

    MvcResult result = mockMvc.perform(post("/transfers/").contentType(contentType).content(json))
                              .andExpect(status().isCreated())
                              .andReturn();

    mockMvc.perform(get(result.getResponse().getHeader("Location")))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.from.name", is("savings")))
           .andExpect(jsonPath("$.to.name", is("personal")))
           .andExpect(jsonPath("$.what", is("gift")))
           .andExpect(jsonPath("$.amount", is(777)))
           .andExpect(jsonPath("$.epochSecond", is(80)));
  }

  @Test
  public void testPostTransferWithFieldMissing() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(
            ImmutableMap.of("from", ImmutableMap.of("id", testAccounts.get(0).getId()),
                            "to", ImmutableMap.of("id", testAccounts.get(1).getId()),
                            "amount", 777,
                            "epochSecond", 80));

    mockMvc.perform(post("/transfers/").contentType(contentType).content(json))
           .andExpect(status().is4xxClientError())
           .andReturn();
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testPostTransferWithExtraField() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(
            ImmutableMap.builder()
                        .put("from", ImmutableMap.of("id", testAccounts.get(0).getId()))
                        .put("to", ImmutableMap.of("id", testAccounts.get(1).getId()))
                        .put("what", "gift")
                        .put("amount", 777)
                        .put("epochSecond", 80)
                        .put("extra", "field")
                        .build());

    MvcResult result = mockMvc.perform(post("/transfers/").contentType(contentType).content(json))
                              .andExpect(status().isCreated())
                              .andReturn();

    mockMvc.perform(get(result.getResponse().getHeader("Location")))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.extra").doesNotExist());
  }

  @Test
  public void testPostTransferShouldNotAcceptProvidedId() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(
            ImmutableMap.builder()
                        .put("id", 1)
                        .put("from", ImmutableMap.of("id", testAccounts.get(0).getId()))
                        .put("to", ImmutableMap.of("id", testAccounts.get(1).getId()))
                        .put("what", "gift")
                        .put("amount", 777)
                        .put("epochSecond", 80)
                        .build());

    mockMvc.perform(post("/transfers/").contentType(contentType).content(json))
           .andExpect(status().is4xxClientError())
           .andReturn();

  }


}
