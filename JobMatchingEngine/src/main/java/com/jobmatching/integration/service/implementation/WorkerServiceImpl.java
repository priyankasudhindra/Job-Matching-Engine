package com.jobmatching.integration.service.implementation;

import java.util.Collections;
import java.util.List;

import com.jobmatching.integration.service.WorkerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.jobmatching.domain.Worker;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WorkerServiceImpl implements WorkerService {

    @Value(value = "${workerResourceUrl}")
    private String workerUrl;
    private final RestTemplate restTemplate;

    public WorkerServiceImpl(RestTemplate restTemplate) {
        super();
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Worker> getWorkers() {

        ResponseEntity<List<Worker>> entity = null;
        List<Worker> workers;
        try {
            entity = restTemplate.exchange(workerUrl, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {
                    });
            if (!HttpStatus.OK.equals(entity.getStatusCode())) {
                log.error("No good response from Worker service. StatusCode = {}", entity.getStatusCode());
                return getDefaultResponse();
            }
            workers = entity.getBody();
            return workers;
        } catch (HttpClientErrorException exception) {
            log.error("Exception occurred while retrieving workers");
            int statusCode = exception.getRawStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED.value() || statusCode == HttpStatus.FORBIDDEN.value()) {
                log.error(String.valueOf(statusCode));
            } else {
                log.error(String.valueOf(statusCode));
            }
            exception.printStackTrace();
        }

        if (entity == null) {
            log.error("No good response from Worker service");
        }
        return getDefaultResponse();
    }

    private List<Worker> getDefaultResponse() {
        return Collections.emptyList();
    }

}
