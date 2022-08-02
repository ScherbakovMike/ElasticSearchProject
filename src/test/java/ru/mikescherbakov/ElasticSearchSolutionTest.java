package ru.mikescherbakov;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import ru.mikescherbakov.config.ElasticSearchConfig;
import ru.mikescherbakov.models.Question;
import ru.mikescherbakov.repositories.QuestionRepository;

@SpringBootTest
@ContextConfiguration(classes = ElasticSearchConfig.class)
class ElasticSearchSolutionTest {

    @Autowired
    QuestionRepository repository;

    @Test
    public void ifEntityStored_itExists() {
        var testTitle = "test title";
        var testContent = "test content";
        var entities = repository.findByTitleContainsIgnoreCase(testTitle, Pageable.unpaged());
        removeAllEntities(entities);

        entities = repository.findByTitleContainsIgnoreCase(testTitle, Pageable.unpaged());
        Assertions.assertEquals(entities.getTotalElements(), 0);

        var question = new Question();
        question.setTitle(testTitle);
        question.setContent(testContent);

        repository.save(question);

        entities = repository.findByTitleContainsIgnoreCase(testTitle, Pageable.unpaged());
        Assertions.assertEquals(entities.getTotalElements(), 1);

        removeAllEntities(entities);
    }

    private void removeAllEntities(Page<Question> entities) {
        entities.forEach(question -> {
            repository.delete(question);
        });
    }

}