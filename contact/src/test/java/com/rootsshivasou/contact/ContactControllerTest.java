package com.rootsshivasou.contact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.rootsshivasou.contact.controller.ContactController;
import com.rootsshivasou.contact.service.ContactService;
import com.rootsshivasou.moduleCommun.model.Contact;
import com.rootsshivasou.moduleCommun.model.dto.ContactDTO;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(ContactController.class)
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testGetAllContacts() throws Exception {
    Contact contact1 = new Contact(); // Assurez-vous d'initialiser ces objets correctement
    Contact contact2 = new Contact();
    List<ContactDTO> contacts = Arrays.asList(new ContactDTO(contact1), new ContactDTO(contact2));

    given(contactService.getAllContacts()).willReturn(Arrays.asList(contact1, contact2));

    mockMvc.perform(get("/contacts")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(contacts.size()));
}

@Test
@WithMockUser(roles = {"ADMIN"})
public void testGetContactById() throws Exception {
    int contactId = 1;
    Contact contact = new Contact();
    contact.setId(contactId); // Définissez explicitement l'id du contact
    // Assurez-vous que le reste des champs nécessaires sont également définis si besoin

    ContactDTO contactDTO = new ContactDTO(contact);
    // Supposons que votre ContactDTO a un champ id et que le constructeur copie cet id

    given(contactService.getContact(contactId)).willReturn(contact); // Simulez le retour du contact avec l'id défini

    mockMvc.perform(get("/contact/{id}", contactId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(contactId)); // Vérifiez que l'id retourné correspond à l'id attendu
}


@Test
public void testInsertContact() throws Exception {
    // Préparation des données pour le test
    Contact contact = new Contact(); // Préparez un objet Contact avec des données de test
    contact.setFirst_name("John");
    contact.setLast_name("Doe");
    contact.setEmail("johndoe@example.com");
    contact.setObject("Inquiry");
    contact.setContent("Hello, world!");
    // Supposons que cette instance de Contact est ce que le service devrait retourner après l'enregistrement
    
    ContactDTO expectedResponse = new ContactDTO(contact); // Créez un ContactDTO à partir de l'instance de Contact

    // Configuration du mock pour retourner un Contact lors de l'appel à saveContact
    // Notez que saveContact devrait retourner un Contact, qui est ensuite converti en ContactDTO dans le contrôleur
    given(contactService.saveContact(any(Contact.class), anyString())).willReturn(contact);

    // Effectuez la requête POST et vérifiez que la réponse correspond aux attentes
    mockMvc.perform(post("/contact")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"first_name\": \"John\", \"last_name\": \"Doe\", \"email\": \"johndoe@example.com\", \"object\": \"Inquiry\", \"content\": \"Hello, world!\" }"))
            .andExpect(status().isOk())
            // Assurez-vous que les champs de la réponse correspondent à ceux de expectedResponse
            .andExpect(jsonPath("$.first_name").value(expectedResponse.getFirst_name()))
            .andExpect(jsonPath("$.last_name").value(expectedResponse.getLast_name()))
            .andExpect(jsonPath("$.email").value(expectedResponse.getEmail()))
            .andExpect(jsonPath("$.object").value(expectedResponse.getObject()))
            .andExpect(jsonPath("$.content").value(expectedResponse.getContent()));
}



@Test
@WithMockUser(roles = {"ADMIN"})
public void testDeleteContact() throws Exception {
    int contactId = 1;
    given(contactService.getContact(contactId)).willReturn(new Contact());
    doNothing().when(contactService).deleteContact(contactId);

    mockMvc.perform(delete("/contact/{id}", contactId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
}



}