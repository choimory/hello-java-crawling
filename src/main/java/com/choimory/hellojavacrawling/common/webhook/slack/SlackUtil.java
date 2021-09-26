package com.choimory.hellojavacrawling.common.webhook.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SlackUtil {
    @Value("${webhook.slack.url}")
    private String url;
    private final ObjectMapper objectMapper;

    public void send(SlackDto param) throws JsonProcessingException {
        /*req header*/
        HttpHeaders requestHeader = new HttpHeaders();

        /*req body*/
        String requestBody = objectMapper.writeValueAsString(param);

        /*http request*/
        HttpEntity<?> request = new HttpEntity<>(requestBody, requestHeader);

        /*send*/
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }
}
