package com.homework.service;

import com.homework.model.Category;
import com.homework.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addElement(String name) {
        Category category = new Category(name);
        logger.debug("Creating a new category:{}", category);
        final var save = categoryRepository.save(category);
        logger.debug("A new category{}", save);
        return save;
    }
    public Category addElement(String name, String parentName) {

        Category category = new Category(name, parentName);
        logger.debug("Creating a new category:{}", category);
        final var save = categoryRepository.save(category);
        logger.debug("A new category{}", save);
        return save;
    }

    public Category editElement(Category category) {
        logger.debug("Edit category:{}", category);
        if (categoryRepository.findById(category.getName()).isPresent()) {
            final var category1 = categoryRepository.save(category);
            logger.debug("Category (edit) is{}", category1);
            return category1;
        } else {
            logger.debug("No category found with id {}", category.getName());
            return null;
        }
    }

    public void removeElement(String name) {
        logger.debug("Delete category:{}", name);
        List<Category> categories = categoryRepository.getViewTreeById(name);
        Iterator<Category> categoryIterator = categories.iterator();
        while(categoryIterator.hasNext()) {
            Category nextCategory = categoryIterator.next();
            categoryRepository.deleteById(nextCategory.getName());
        }
    }
    @GetMapping
    public Collection<Category> getViewTree() {
        logger.debug("Collection all categories:{}");
        final var all = categoryRepository.getViewTree();
        logger.debug("All categories is{}", all);
        return all;
    }

}
