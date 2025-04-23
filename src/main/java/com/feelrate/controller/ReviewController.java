package com.feelrate.controller;

import com.feelrate.domain.Review;
import com.feelrate.security.JwtProvider;
import com.feelrate.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtProvider jwtProvider;

    @PostMapping
    public ResponseEntity<Review> writeReview(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> body
    ) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtProvider.getUserId(token);
        Long restaurantId = Long.parseLong(body.get("restaurantId"));
        String content = body.get("content");

        Review saved = reviewService.writeReview(userId, restaurantId, content);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Review>> getReviewsByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(reviewService.getReviewsByRestaurant(restaurantId));
    }
}