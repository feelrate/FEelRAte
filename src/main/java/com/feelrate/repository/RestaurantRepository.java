package com.feelrate.repository;

import com.feelrate.domain.Restaurant;
import com.feelrate.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByCategory(Category category); // 카테고리별 조회
}