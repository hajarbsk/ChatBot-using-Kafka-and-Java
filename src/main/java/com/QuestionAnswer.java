package com;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
public class QuestionAnswer {

    private int id;
    private String question;
    private String response;

    // Constructeurs, getters et setters
    public QuestionAnswer() {}
    @JsonCreator
    public QuestionAnswer(@JsonProperty("question") String question,
                          @JsonProperty("answer") String response,
                          @JsonProperty("id") int id) {
        this.question = question;
        this.response = response;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
