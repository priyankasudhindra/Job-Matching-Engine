package com.jobmatching.integration.service.implementation;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.jobmatching.domain.Job;
import com.jobmatching.integration.service.JobService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JobServiceImpl implements JobService {
    @Value(value = "${jobResourceUrl}")
    private String jobUrl;

    private final RestTemplate restTemplate;

    public JobServiceImpl(RestTemplate restTemplate) {
        super();
        this.restTemplate = restTemplate;
    }
    @Override
    public List<Job> getJobs() {

        ResponseEntity<List<Job>> entity = null;
        List<Job> jobs;
        try {
            entity = restTemplate.exchange(jobUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
            });
            if (!HttpStatus.OK.equals(entity.getStatusCode())) {
                log.error("No good response from Worker service. StatusCode={}", entity.getStatusCode());
                return getDefaultResponse();
            }

            jobs = entity.getBody();
            return jobs;
        } catch (HttpClientErrorException exception) {
            log.error("Exception occurred while retrieving jobs");

            int statusCode = exception.getRawStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED.value() || statusCode == HttpStatus.FORBIDDEN.value()) {
                log.error("unauthorized/forbidden: " + statusCode);
            }
            exception.printStackTrace();
        }

        if (entity == null) {
            log.error("No good response from Worker Service");
        }
        return getDefaultResponse();
    }

    private List<Job> getDefaultResponse() {
        return Collections.emptyList();
    }
}
