package ru.mikescherbakov;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.test.context.ContextConfiguration;
import ru.mikescherbakov.config.ElasticSearchConfig;
import ru.mikescherbakov.models.Question;
import ru.mikescherbakov.repositories.QuestionRepository;
import ru.mikescherbakov.services.ElasticSearchRestTemplateIndexService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@ContextConfiguration(classes = ElasticSearchConfig.class)
class ElasticSearchSolutionTest {

    @Autowired
    QuestionRepository repository;

    @Autowired
    ElasticSearchRestTemplateIndexService service;

    @Test
    public void OnIndexingViaRepository_Success() throws InterruptedException {
        var testTitle = "title";
        var testContent = "content";
        var entities = repository.findByTitleContaining(testTitle, Pageable.unpaged());
        removeEntities(entities);

        assertEmptyIndex(testTitle);

        Question question = getQuestion(testTitle, testContent);
        repository.save(question);
        TimeUnit.SECONDS.sleep(2);

        entities = repository.findByTitleContaining(testTitle, Pageable.unpaged());
        Assertions.assertEquals(1, entities.getTotalElements());

        removeEntities(entities);
    }

    @Test
    public void OnIndexingViaRestTemplate_Success() throws InterruptedException {
        var testTitle = "title";
        var testContent = "content";
        var entities = repository.findByTitleContaining(testTitle, Pageable.unpaged());
        removeEntities(entities);

        assertEmptyIndex(testTitle);

        Question question = getQuestion(testTitle, testContent);
        service.createQuestionIndex(question);
        TimeUnit.SECONDS.sleep(2);

        var listOfQuestions = service.findByQuestionTitle(testTitle)
                .stream().map(SearchHit::getContent).toList();
        Assertions.assertEquals(1, listOfQuestions.size());

        removeEntities(entities);
    }

    private void assertEmptyIndex(String testTitle) {
        Page<Question> entities;
        entities = repository.findByTitleContaining(testTitle, Pageable.unpaged());
        Assertions.assertEquals(0, entities.getTotalElements());
    }

    private void removeEntities(Page<Question> entities) {
        repository.deleteAll(entities);
    }

    private Question getQuestion(String testTitle, String testContent) {
        var question = new Question();
        question.setId(UUID.randomUUID().toString());
        question.setTitle(testTitle);
        question.setContent(testContent);
        return question;
    }
}