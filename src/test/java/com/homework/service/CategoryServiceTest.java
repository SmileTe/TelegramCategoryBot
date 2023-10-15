package com.homework.service;

import com.homework.model.Category;
import com.homework.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    private Logger logger;
    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    private Category expectedCategory;


    @BeforeEach
    public void setup() {
        expectedCategory = new Category();
        expectedCategory.setName("test");
        expectedCategory.setParentName("parent");
    }

    @Test
    void addElement() {
        when(categoryRepository.save(any(Category.class))).thenReturn(expectedCategory);
        Category actualCategory = categoryService.addElement(expectedCategory.getName(), expectedCategory.getParentName());
        assertEquals(expectedCategory, actualCategory);

    }

   @Test
    void editElement() {
       Category updatedCategory = new Category();
       String newParentName = "new parent";
       updatedCategory.setName(expectedCategory.getName());
       updatedCategory.setParentName(newParentName);


       when(categoryRepository.findById(expectedCategory.getName())).thenReturn(Optional.of(expectedCategory));
       when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);

       Category result = categoryService.editElement(updatedCategory);

       assertNotNull(result);
       assertEquals(newParentName, result.getParentName());
       assertEquals(expectedCategory.getName(), result.getName());

       verify(categoryRepository, times(1)).save(updatedCategory);

   }

    @Test
    void removeElement() {

        List<Category> categories = new ArrayList<>();
        categories.add(expectedCategory);

        when(categoryRepository.getViewTreeById(any(String.class))).thenReturn(categories);
        doNothing().when(categoryRepository).deleteById(expectedCategory.getName());

        categoryService.removeElement(expectedCategory.getName());

        verify(categoryRepository, times(1)).deleteById(expectedCategory.getName());

    }

    @Test
    void getViewTree() {


        Category category = new Category();
        category.setName("test 2");
        category.setParentName("parent 2");


        List<Category> foundCategories = new ArrayList<>();
        foundCategories.add(expectedCategory);
        foundCategories.add(category);

        when(categoryRepository.getViewTree()).thenReturn(foundCategories);

        Collection<Category> result = categoryService.getViewTree();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(expectedCategory));
        assertTrue(result.contains(category));

    }
}