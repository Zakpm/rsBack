package com.rootsshivasou.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.rootsshivasou.security.service.ResetPasswordRequestService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ResetPasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResetPasswordRequestService resetPasswordRequestService;

    @Test
    @WithMockUser(roles = {"USER"})
    public void testRequestPasswordReset() throws Exception {
        // Configurer le mock pour simuler un comportement réussi de la méthode createResetPasswordRequest
        doNothing().when(resetPasswordRequestService).createResetPasswordRequest(anyString());

        // Exécuter la requête POST pour demander une réinitialisation du mot de passe
        mockMvc.perform(post("/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"user@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Demande de réinitialisation du mot de passe envoyée."));
    }

    @Test
    public void testRequestPasswordResetFailure() throws Exception {
        // Configurer le mock pour simuler une exception lors de la demande de réinitialisation du mot de passe
        doThrow(new RuntimeException("Erreur lors de la demande de réinitialisation du mot de passe."))
                .when(resetPasswordRequestService).createResetPasswordRequest(anyString());

        // Exécuter la requête POST pour demander une réinitialisation du mot de passe et attendre un échec
        mockMvc.perform(post("/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"invalid@example.com\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Erreur lors de la demande de réinitialisation du mot de passe."));
    }
}
