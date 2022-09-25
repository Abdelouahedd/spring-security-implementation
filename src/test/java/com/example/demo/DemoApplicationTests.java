package com.example.demo;

import com.example.demo.repo.RoleRepository;
import com.example.demo.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class DemoApplicationTests {
  @MockBean
  private RoleRepository roleRepository;
  @MockBean
  private UserRepository userRepository;
	@Test
	void contextLoads() {
	}

}
