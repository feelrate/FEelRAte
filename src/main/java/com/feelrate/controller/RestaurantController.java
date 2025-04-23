package com.feelrate.controller;

import com.feelrate.domain.Restaurant;
import com.feelrate.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(restaurantService.save(restaurant));
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.findAll());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Restaurant>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(restaurantService.findByCategoryId(categoryId));
    }
}