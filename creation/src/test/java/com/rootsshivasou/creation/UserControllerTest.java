package com.rootsshivasou.creation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.security.test.context.support.WithMockUser;


import com.rootsshivasou.creation.service.UserService;
import com.rootsshivasou.moduleCommun.model.User;
import com.rootsshivasou.moduleCommun.model.dto.UserDTO;
import com.rootsshivasou.moduleCommun.enums.*;


import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testAll() throws Exception {
        User user1 = new User(); 
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(users.size()));
    }

    @Test
@WithMockUser(roles = {"ADMIN"})
void testGetUserById() throws Exception {
    int userId = 1;
    User user = new User();
    user.setId(userId);
    UserDTO userDTO = new UserDTO(user);

    when(userService.getUser(userId)).thenReturn(user);

    mockMvc.perform(get("/user/{id}", userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(userId));
}

@Test
@WithMockUser(roles = {"ADMIN"})
void testCreateUser() throws Exception {
    // Assurez-vous que le corps de la requête correspond exactement à ce que votre API attend
    String requestBody = """
        {
            "first_name":"TestFirstName",
            "last_name":"TestLastName",
            "email":"test@example.com",
            "nickname":"TestUser",
            "password":"password",
            "genre":"HOMME",
            "date_naissance":"1990-01-01",
            "roles":["ROLE_USER"]
        }
        """;

    System.out.println("Request Body: " + requestBody);

    // Configuration initiale de l'utilisateur
    User user = new User();
    user.setId(1);
    user.setFirst_name("TestFirstName");
    user.setLast_name("TestLastName");
    user.setEmail("test@example.com");
    user.setNickname("TestUser");
    user.setPassword("password");
    user.setGenre(Genre.HOMME); // Assurez-vous que cela correspond à votre enum Genre
    user.setDate_naissance(LocalDate.of(1990, 1, 1));
    user.setRoles(Set.of(Role.ROLE_USER)); // Correspond à l'utilisation de l'enum Role

    // Configuration du mock pour retourner l'utilisateur lorsque saveUser est appelé
    when(userService.saveUser(any(User.class))).thenReturn(user);

    // Construction de la requête POST et exécution du test
    mockMvc.perform(post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nickname").value("TestUser"))
            .andExpect(jsonPath("$.first_name").value("TestFirstName"))
            .andExpect(jsonPath("$.last_name").value("TestLastName"))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            // Assurez-vous que le genre correspond à ce que vous attendez ("HOMME" dans ce cas)
            .andExpect(jsonPath("$.genre").value("HOMME"))
            .andExpect(jsonPath("$.date_naissance").value("1990-01-01"))
            // Vérifiez que le rôle est correctement retourné dans un tableau
            .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));

    System.out.println("Test 'testCreateUser' passed with 201 status.");
}



@Test
@WithMockUser(roles = {"ADMIN"})
void testDeleteUser() throws Exception {
    int userId = 1;

    when(userService.getUser(userId)).thenReturn(new User());
    doNothing().when(userService).deleteUser(userId);

    mockMvc.perform(delete("/user/{id}", userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
}

@Test
@WithMockUser(roles = {"ADMIN"})
void testUpdateUser() throws Exception {
    int userId = 1;
    User user = new User();
    user.setId(userId);
    UserDTO userDTO = new UserDTO(user);

    when(userService.getUser(userId)).thenReturn(user);
    when(userService.saveUser(any(User.class))).thenReturn(user);

    mockMvc.perform(put("/user/{id}", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"first_name\":\"UpdatedName\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.first_name").value("UpdatedName"));
}

}
