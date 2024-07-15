package com.rootsshivasou.creation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rootsshivasou.creation.repository.CategoryRepository;
import com.rootsshivasou.moduleCommun.model.Category;
import com.rootsshivasou.moduleCommun.model.dto.CategoryDTO;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

     public List<Category> getAllCategories() {
        return (List<Category>) categoryRepository.findAll();
    }

    public Category getCategory(int id) {
        Optional<Category> optional = categoryRepository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * méthode qui sera utilisée pour les inserts et les updates
     * @param u
     * @return
     */
    public Category saveCategory(Category c) {
        return categoryRepository.save(c);
    }

    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

    public CategoryDTO updateCategory(int categoryId, CategoryDTO updatedCategoryDTO) {
        Optional<Category> optionalExistingCategory = categoryRepository.findById(categoryId);

        if (optionalExistingCategory.isPresent()) {
            Category existingCategory = optionalExistingCategory.get();

            // Mettre à jour les propriétés de l'utilisateur avec les nouvelles valeurs
            existingCategory.setTitle(updatedCategoryDTO.getTitle());

            // Enregistrer l'utilisateur mis à jour dans la base de données
            categoryRepository.save(existingCategory);

            // Retourner le DTO mis à jour
            return new CategoryDTO(existingCategory);
        } else {
            // L'utilisateur avec l'ID spécifié n'a pas été trouvé
            return null;
        }
    }
    
}