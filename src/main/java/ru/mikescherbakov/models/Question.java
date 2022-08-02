package ru.mikescherbakov.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "questions")
@Getter @Setter
public class Question {
    @Id
    private Integer id;
    private String title;
    private String content;
    private byte[] image;
}
