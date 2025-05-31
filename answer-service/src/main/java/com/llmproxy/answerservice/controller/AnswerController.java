package com.llmproxy.answerservice.controller;

import com.llmproxy.answerservice.dto.AnswerRequest;
import com.llmproxy.answerservice.dto.AnswerResponse;
import com.llmproxy.answerservice.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping
    public AnswerResponse getAnswer(@RequestBody AnswerRequest request) {
        return answerService.generateAnswer(request);
    }
}
