package ru.mikescherbakov.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.mikescherbakov.models.Question;

public interface QuestionRepository extends ElasticsearchRepository<Question, String> {
    @Query("{\"bool\": {\"must\": [{\"match\": {\"question.title\": \"?0\"}}]}}")
    Page<Question> findByTitleContainsIgnoreCase(String titlePart, Pageable pageable);
}
