package com.example.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RolesControllerTest {
  final ObjectMapper mapper = new ObjectMapper();
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
  void return403WhenGetRolesByUnauthenticatedUser() throws Exception {
    ResultActions perform = mockMvc.perform(get("/api/roles"));
    perform.andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "aez", roles = "DEFAULT")
  void return200WhenGetRolesByAuthenticatedUser() throws Exception {
    mockMvc.perform(get("/api/roles"))
      .andExpect(status().is(200));
  }

  @Test
  @WithMockUser(username = "ae", roles = "DEFAULT")
  void return403WhenGetRolesByAuthenticatedUserWithDefaultRole() throws Exception {
    mockMvc.perform(post("/api/roles")
      .contentType(MediaType.APPLICATION_JSON)
      .content(mapper.writeValueAsString("new_role")
      )).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "ae", roles = "ADMIN")
  void return200WhenGetRolesByAuthenticatedUserWithAdminRole() throws Exception {
    mockMvc.perform(post("/api/roles")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString("new_role"))
      )
      .andExpect(status().is(200));
  }
}
