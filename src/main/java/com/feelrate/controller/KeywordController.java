package com.feelrate.controller;

import com.feelrate.document.RestaurantDocument;
import com.feelrate.dto.KeywordRequest;
import com.feelrate.service.RestaurantSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/keywords")
@RequiredArgsConstructor
public class KeywordController {
    private final RestaurantSearchService restaurantSearchService;
   /* @PostMapping
    public String receiveKeywords(@RequestBody KeywordRequest request){
        List<String> keywords = request.getKeywords();
        System.out.println("🔥 받은 키워드: " + keywords);
        return "키워드 수신 완료";
    }*/
   @PostMapping
   public List<RestaurantDocument> receiveKeywords(@RequestBody KeywordRequest request) {
       return restaurantSearchService.searchByKeywords(request.getKeywords());
   }

}
