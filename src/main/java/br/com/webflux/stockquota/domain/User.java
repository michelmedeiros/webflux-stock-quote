package br.com.webflux.stockquota.domain;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Document(indexName = "user_idx")
public class User {
    private Integer id;
    private String name;
    private String email;
}