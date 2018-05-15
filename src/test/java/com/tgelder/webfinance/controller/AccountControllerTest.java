package com.tgelder.webfinance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tgelder.webfinance.App;
import com.tgelder.webfinance.model.Account;
import com.tgelder.webfinance.persistence.AccountRepository;
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
public class AccountControllerTest {

  @Autowired
  private AccountRepository accountRepository;

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

  @Before
  public void setup() {
    mockMvc = webAppContextSetup(webApplicationContext).build();
    accountRepository.saveAll(testAccounts);
  }

  @After
  public void tearDown() {
    accountRepository.deleteAll();
  }

  @Test
  public void testGetAccount() throws Exception {
    mockMvc.perform(get("/accounts/" + testAccounts.get(0).getId()))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.id", is(testAccounts.get(0).getId().intValue())))
           .andExpect(jsonPath("$.name", is("savings")));
  }

  @Test
  public void testGetAccounts() throws Exception {
    mockMvc.perform(get("/accounts/"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.[*].name", containsInAnyOrder("savings", "personal")));
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testPostAccount() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(ImmutableMap.of("name", "new"));

    MvcResult result = mockMvc.perform(post("/accounts/").contentType(contentType).content(json))
                              .andExpect(status().isCreated())
                              .andReturn();

    mockMvc.perform(get(result.getResponse().getHeader("Location")))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.name", is("new")));
  }

  @Test
  public void testPostAccountWithFieldMissing() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(ImmutableMap.of("other", "field"));

    mockMvc.perform(post("/accounts/").contentType(contentType).content(json))
           .andExpect(status().is4xxClientError())
           .andReturn();
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testPostAccountWithExtraField() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(ImmutableMap.of("name", "new", "extra", "field"));

    MvcResult result = mockMvc.perform(post("/accounts/").contentType(contentType).content(json))
                              .andExpect(status().isCreated())
                              .andReturn();

    mockMvc.perform(get(result.getResponse().getHeader("Location")))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.extra").doesNotExist());
  }

  @Test
  public void testPostAccountShouldNotAcceptProvidedId() throws Exception {
    String json = OBJECT_MAPPER.writeValueAsString(ImmutableMap.of("id", 1, "name", "new"));

    MvcResult result = mockMvc.perform(post("/accounts/").contentType(contentType).content(json))
                              .andExpect(status().is4xxClientError())
                              .andReturn();

  }


}
