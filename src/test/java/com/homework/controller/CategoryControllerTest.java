package com.homework.controller;

import com.homework.model.Category;
import com.homework.repository.CategoryRepository;
import com.homework.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addElement()  throws Exception{
        String name = "test";
        String parent_name = "parent";
        JSONObject categoryObject = new JSONObject();
        categoryObject.put("name", name);
        categoryObject.put("parent_name", parent_name);

        Category category = new Category();
        category.setName(name);
        category.setParentName(parent_name);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/categories/{name}/{parent_name}", category.getName(),category.getParentName())
                        .accept(MediaType.APPLICATION_JSON)
                     )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.parent_name").value(parent_name));


    }

    @Test
    void editElement () throws Exception{
        String name = "test";
        String parent_name = "parent";
        String newParent_name = "parent";


        JSONObject categoryObject = new JSONObject();
        categoryObject.put("name", name);
        categoryObject.put("parent_name", newParent_name);

        Category category = new Category();
        category.setName(name);
        category.setParentName(parent_name);


        Category newCategory = new Category();
        newCategory.setName(name);
        newCategory.setParentName(newParent_name);

        when(categoryRepository.findById(any(String.class))).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);


        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/categories")
                        .content(categoryObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                       )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.parent_name").value(newParent_name));

    }

    @Test
    void removeElement() throws Exception {
        String name = "test";
        String parent_name = "parent";

        Category category = new Category();
        category.setName(name);
        category.setParentName(parent_name);

        List<Category> categories = new ArrayList<>();
        categories.add(category);

        when(categoryRepository.getViewTreeById(any(String.class))).thenReturn(categories);
        doNothing().when(categoryRepository).deleteById(name);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/categories/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(categoryRepository, atLeastOnce()).deleteById(name);
    }

    @Test
    void viewTree() throws Exception{
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk());
    }
}