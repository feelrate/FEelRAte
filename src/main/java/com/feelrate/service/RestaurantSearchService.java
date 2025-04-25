package com.feelrate.service;

import com.feelrate.document.RestaurantDocument;
import com.feelrate.repository.RestaurantSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantSearchService {
    private final RestaurantSearchRepository restaurantSearchRepository;

    public List<RestaurantDocument> searchByKeywords(List<String> keywords) {
        return restaurantSearchRepository.findByKeywordsIn(keywords);
    }
}
