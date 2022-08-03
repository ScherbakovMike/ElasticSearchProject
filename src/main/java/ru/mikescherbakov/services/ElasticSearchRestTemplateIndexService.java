package ru.mikescherbakov.services;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;
import ru.mikescherbakov.models.Question;

@Service
public class ElasticSearchRestTemplateIndexService {

    private static final String QUESTION_INDEX = "questions";

    private final ElasticsearchOperations elasticsearchOperations;

    public ElasticSearchRestTemplateIndexService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public String createQuestionIndex(Question question) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(question.getId())
                .withObject(question).build();

        return elasticsearchOperations
                .index(indexQuery, IndexCoordinates.of(QUESTION_INDEX));
    }

    public SearchHits<Question> findByQuestionTitle(final String title) {
        Query searchQuery = new StringQuery(
                "{\"match\":{\"title\":{\"query\":\""+ title + "\"}}}\"");

        return elasticsearchOperations.search(
                searchQuery,
                Question.class,
                IndexCoordinates.of(QUESTION_INDEX));
    }
}
