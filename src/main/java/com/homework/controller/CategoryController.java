package com.homework.controller;

import com.homework.model.Category;
import com.homework.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @Operation(summary = "Создать новую категорию",
            responses = {@ApiResponse(responseCode = "200", description = "Created"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")
            },
            tags = "Categories")

    @PostMapping("{name}/{parent_name}") //POST http://localhost:8080/categories
    public Category addElement(@PathVariable String name, @RequestParam(value = "parent_name",  required = false, defaultValue = "") String parentName) {
    if(parentName.isBlank()){
        return categoryService.addElement(name);
    }
        return categoryService.addElement(name, parentName);
    }
    @Operation(summary = "Изменить существующую категорию",
            responses = {@ApiResponse(responseCode = "200", description = "Created"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")

            },
            tags = "Categories")

    @PatchMapping //PUT http://localhost:8080/categories
    public Category editElement(@RequestBody Category category) {
        return categoryService.editElement(category);
    }

    @Operation(summary = "Удалить существующую категорию",
            responses = {@ApiResponse(responseCode = "200", description = "Created"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")
            },
            tags = "Categories")
    @DeleteMapping("{name}") //DELETE http://localhost:8080/categories/name
    public ResponseEntity<Void> removeElement(@PathVariable String name) {
        categoryService.removeElement(name);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить список категорий",
            responses = {@ApiResponse(responseCode = "200", description = "Done"),
                    @ApiResponse(responseCode = "404", description = "Not Found"),
                    @ApiResponse(responseCode = "400", description = "Bad Request")
            },
            tags = "Categories")
    @GetMapping //GET http://localhost:8080/categories
    public ResponseEntity<?> viewTree() {
            return ResponseEntity.ok(categoryService.getViewTree());
    }




}
