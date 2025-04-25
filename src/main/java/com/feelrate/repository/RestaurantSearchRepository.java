package com.feelrate.repository;

import com.feelrate.document.RestaurantDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantSearchRepository extends ElasticsearchRepository<RestaurantDocument, String> {
    List<RestaurantDocument> findByKeywordsIn(List<String> keywords);
}

