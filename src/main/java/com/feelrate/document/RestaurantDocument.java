package com.feelrate.document;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDocument {

    @Id
    private String id;

    private List<String> keywords;
}