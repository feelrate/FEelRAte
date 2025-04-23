package com.feelrate.service;

import com.feelrate.domain.Restaurant;
import com.feelrate.domain.Review;
import com.feelrate.domain.User;
import com.feelrate.repository.RestaurantRepository;
import com.feelrate.repository.ReviewRepository;
import com.feelrate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public Review writeReview(Long userId, Long restaurantId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("식당 없음"));

        Review review = new Review();
        review.setUser(user);
        review.setRestaurant(restaurant);
        review.setContent(content);
        review.setCreatedAt(LocalDateTime.now());

        // NLP 감정 분석 연동 (임시 점수)
        double score = analyzeSentiment(content);
        review.setSentimentScore(score);

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("식당 없음"));
        return reviewRepository.findByRestaurant(restaurant);
    }

    private double analyzeSentiment(String content) {
        // 임시 감정 분석 로직 (예: 긍정 단어 포함 여부로 점수)
        if (content.contains("맛있")) return 0.9;
        if (content.contains("별로")) return 0.3;
        return 0.5; // 기본값
    }
}