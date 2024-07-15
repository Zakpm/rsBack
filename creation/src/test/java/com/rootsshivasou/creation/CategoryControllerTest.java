package com.rootsshivasou.creation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rootsshivasou.creation.service.CategoryService;
import com.rootsshivasou.moduleCommun.model.Category;
import com.rootsshivasou.moduleCommun.model.dto.CategoryDTO;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CategoryService categoryService;

  @Test
  @WithMockUser(roles = { "ADMIN" })
  void testAllCategories() throws Exception {
    Category category1 = new Category(); // Initialisez vos objets Category selon votre modèle
    Category category2 = new Category();
    List<CategoryDTO> categoryDTOs = Arrays.asList(
      new CategoryDTO(category1),
      new CategoryDTO(category2)
    );

    when(categoryService.getAllCategories())
      .thenReturn(Arrays.asList(category1, category2));

    mockMvc
      .perform(get("/categories").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.size()").value(categoryDTOs.size()));
  }

  @Test
  @WithMockUser(roles = { "ADMIN" })
  void testGetCategoryById() throws Exception {
    Category category = new Category(); // Assurez-vous d'initialiser correctement l'objet
    CategoryDTO categoryDTO = new CategoryDTO(category);

    int categoryId = 1; // Utilisez un identifiant de test
    when(categoryService.getCategory(categoryId)).thenReturn(category);

    mockMvc
      .perform(
        get("/category/{id}", categoryId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(category.getId()));
  }

  @Test
  @WithMockUser(roles = { "ADMIN" })
  void testInsertCategory() throws Exception {
    Category category = new Category(); 
    CategoryDTO categoryDTO = new CategoryDTO(category);

    when(categoryService.saveCategory(Mockito.any(Category.class)))
      .thenReturn(category);

    mockMvc
      .perform(
        post("/category")
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"name\":\"Test Category\"}")
      ) 
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.title").value(category.getTitle()));
  }

  @Test
  @WithMockUser(roles = { "ADMIN" })
  void testDeleteCategory() throws Exception {
    int categoryId = 1; 
    when(categoryService.getCategory(categoryId)).thenReturn(new Category());

    mockMvc
      .perform(
        delete("/category/{id}", categoryId)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = { "ADMIN" })
  void testUpdateCategory() throws Exception {
    int categoryId = 1; // Identifiant du Category à mettre à jour
    String updatedTitle = "Updated Category";

    // Création et configuration du Category et CategoryDTO retourné par le mock
    Category category = new Category();
    category.setId(categoryId);
    CategoryDTO categoryDTO = new CategoryDTO();
    categoryDTO.setTitle(updatedTitle); // Définir le titre attendu dans le DTO

    when(categoryService.updateCategory(eq(categoryId), any(CategoryDTO.class)))
      .thenReturn(categoryDTO);

    mockMvc
      .perform(
        put("/category/{id}", categoryId)
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"title\":\"" + updatedTitle + "\"}")
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.title").value(updatedTitle));
  }
}
