package com.rootsshivasou.security;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rootsshivasou.security.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthenticationService authenticationService;

  private final String mockJwt = "mock.jwt.token";

  @BeforeEach
  void setUp() {
    // Configurer le comportement simulé de AuthenticationService pour retourner un JWT sur l'authentification réussie
    given(authenticationService.authenticate(anyString(), anyString()))
      .willReturn(mockJwt);
  }

  @Test
  public void testAuthenticateUserSuccess() throws Exception {
    mockMvc
      .perform(
        post("/auth/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"login\":\"admin\",\"password\":\"validpassword\"}")
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.jwt").value(mockJwt));
  }
}
