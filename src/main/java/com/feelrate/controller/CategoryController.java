package com.feelrate.controller;

import com.feelrate.domain.Category;
import com.feelrate.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> create(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.create(name));
    }

    @GetMapping
    public ResponseEntity<List<Category>> all() {
        return ResponseEntity.ok(categoryService.findAll());
    }
}
