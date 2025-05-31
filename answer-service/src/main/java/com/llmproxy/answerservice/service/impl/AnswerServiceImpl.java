package com.llmproxy.answerservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.llmproxy.answerservice.dto.AnswerRequest;
import com.llmproxy.answerservice.dto.AnswerResponse;
import com.llmproxy.answerservice.service.AnswerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Value("${llm.api.key}")
    private String apiKey;

    @Override
    public AnswerResponse generateAnswer(AnswerRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", request.getQuestion());

        Map<String, Object> part = new HashMap<>();
        part.put("parts", List.of(textPart));

        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", List.of(part));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(apiUrl + apiKey, entity, String.class);
        String responseBody = responseEntity.getBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String generatedText = root.path("candidates").get(0)
                .path("content")
                .path("parts").get(0)
                .path("text")
                .asText();

        AnswerResponse answerResponse = new AnswerResponse();
        answerResponse.setAnswer(generatedText);
        return answerResponse;
    }
}
