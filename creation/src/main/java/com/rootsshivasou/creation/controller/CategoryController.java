package com.rootsshivasou.creation.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rootsshivasou.creation.service.CategoryService;
import com.rootsshivasou.moduleCommun.model.Category;
import com.rootsshivasou.moduleCommun.model.dto.CategoryDTO;

@RestController
@CrossOrigin(origins = "*") // URL qui pourront passer une requête
public class CategoryController {

    @Autowired
    CategoryService service;

    
    @GetMapping("/categories")
    public List<CategoryDTO> all() {
        List<CategoryDTO> list = new ArrayList<CategoryDTO>();
        for (Category category : service.getAllCategories()) {
            list.add(new CategoryDTO(category));
        }
        return list;
    }

    @GetMapping("/category/{id}")
    public CategoryDTO cat(@PathVariable("id") int identifier){
        return new CategoryDTO(service.getCategory(identifier));
    }

    /**
     * L'annotation requestbody permet de récurerer les infos écrits dans le corps
     * @param u
     * @return
     */
    @PostMapping("/category")
    public CategoryDTO insertCategory(@RequestBody Category c) {
        return new CategoryDTO(service.saveCategory(c));
    }

    @DeleteMapping("/category/{id}")
    public Boolean deleteCategory(@PathVariable("id") int id) {
        Category c = service.getCategory(id);
        if (c != null) {
            service.deleteCategory(id);
            return true;
        } else {
            return false;
        }
    }

   
    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable("id") int id, @RequestBody CategoryDTO updatedCategoryDTO) {
        CategoryDTO updatedCategory = service.updateCategory(id, updatedCategoryDTO);

        if (updatedCategory != null) {
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
}