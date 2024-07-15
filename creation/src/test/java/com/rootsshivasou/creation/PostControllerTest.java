package com.rootsshivasou.creation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rootsshivasou.creation.service.CategoryService;
import com.rootsshivasou.creation.service.PostService;
import com.rootsshivasou.creation.service.UserService;
import com.rootsshivasou.moduleCommun.model.Category;
import com.rootsshivasou.moduleCommun.model.Post;
import com.rootsshivasou.moduleCommun.model.User;
import com.rootsshivasou.moduleCommun.model.dto.PostDTO;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService postService;

   @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserService userService;

  @Test
  @WithMockUser(roles = { "USER" })
  void testAll() throws Exception {
    // Créez des mock Post et PostDTO pour simuler le comportement du service
    Post post1 = new Post(); // Initialisez vos objets Post selon votre modèle
    Post post2 = new Post();
    PostDTO postDTO1 = new PostDTO(post1);
    PostDTO postDTO2 = new PostDTO(post2);
    List<PostDTO> postDTOs = Arrays.asList(postDTO1, postDTO2);

    // Simulez le comportement de service.getAllPosts pour retourner une liste de posts
    when(postService.getAllPosts()).thenReturn(Arrays.asList(post1, post2));

    // Exécutez la requête GET et vérifiez les résultats
    mockMvc
      .perform(get("/posts").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.size()").value(postDTOs.size()));
  }

  @Test
  @WithMockUser(roles = { "USER" })
  void testGetPostById() throws Exception {
    int postId = 1;
    Post post = new Post(); 
    post.setId(postId); 
    PostDTO postDTO = new PostDTO(post); 

    when(postService.getPost(postId)).thenReturn(post);

    mockMvc
      .perform(
        get("/post/{id}", postId).contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(postId));
  }



  @Test
  @WithMockUser(roles = { "ADMIN" })
  void testDeletePost() throws Exception {
    int postId = 1;

    when(postService.getPost(postId)).thenReturn(new Post());
    doNothing().when(postService).deletePost(postId);

    mockMvc
      .perform(
        delete("/post/{id}", postId).contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk());
  }
}
