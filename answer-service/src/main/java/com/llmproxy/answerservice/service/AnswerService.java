package com.llmproxy.answerservice.service;

import com.llmproxy.answerservice.dto.AnswerRequest;
import com.llmproxy.answerservice.dto.AnswerResponse;

public interface AnswerService {
    AnswerResponse generateAnswer(AnswerRequest request);
}
