package com.example.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UsersControllerTest {
  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders
      .webAppContextSetup(context)
      .apply(springSecurity())
      .build();
  }

  @Test
  @WithMockUser(username = "ae")
  void get200ToGetHelloCurrentUser() throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/"))
      .andExpect(status().is(200))
      .andReturn();
    assertEquals("Hello ae", mvcResult.getResponse().getContentAsString());
  }

  @Test
  void get403ToGetHelloCurrentUser() throws Exception {
    mockMvc.perform(get("/"))
      .andExpect(status().is(403));
  }

  @Test
  @WithMockUser(username = "ae", roles = "ADMIN")
  void get200WhenGetAllUsersWithAuthenticatedUserWithAdminRole() throws Exception {
    mockMvc.perform(get("/api/users"))
      .andExpect(status().is(200));
  }

  @Test
  @WithMockUser(username = "ae", roles = "USER")
  void get403WhenGetAllUsersWithAuthenticatedUserWithUserRole() throws Exception {
    mockMvc.perform(get("/api/users"))
      .andExpect(status().is(403));
  }
}
