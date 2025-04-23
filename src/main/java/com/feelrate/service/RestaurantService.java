package com.feelrate.service;

import com.feelrate.domain.Category;
import com.feelrate.domain.Restaurant;
import com.feelrate.repository.CategoryRepository;
import com.feelrate.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    public Restaurant save(Restaurant restaurant) {
        restaurant.setCreatedAt(java.time.LocalDateTime.now());
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> findByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));
        return restaurantRepository.findByCategory(category);
    }
}