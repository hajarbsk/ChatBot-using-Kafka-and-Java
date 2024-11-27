package com;
import com.QuestionAnswer;
import com.Reponses;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class KnowledgeBase {

    private List<QuestionAnswer> data;

    public KnowledgeBase(String filePath) {
        loadKnowledgeBase(filePath);
    }

    private void loadKnowledgeBase(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            data = mapper.readValue(new File(filePath), new TypeReference<List<QuestionAnswer>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du chargement de la base de connaissances");
        }
    }

    public Optional<QuestionAnswer> findByQuestion(String userQuestion) {
        return data.stream()
                .filter(qa -> qa.getQuestion().equalsIgnoreCase(userQuestion))
                .findFirst();
    }

    public List<QuestionAnswer> getAllQuestionsAndAnswers() {
        return data;
    }
}

