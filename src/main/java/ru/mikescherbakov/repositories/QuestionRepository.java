package ru.mikescherbakov.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.mikescherbakov.models.Question;

public interface QuestionRepository extends ElasticsearchRepository<Question, String> {

    Page<Question> findByTitleContaining(String titlePart, Pageable pageable);
}
