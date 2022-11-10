package com.jobmatching.resource;

import com.jobmatching.exception.BadRequestException;
import com.jobmatching.exception.NoJobFoundException;
import com.jobmatching.exception.NoWorkerFoundException;
import com.jobmatching.service.JobFinderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(path = "/findJobs")
public class JobFinderResource {

    private final JobFinderService jobFinderService;

    public JobFinderResource(JobFinderService jobFinderService) {
        this.jobFinderService = jobFinderService;
    }

    @GetMapping(path = "/{workerId}")
    public ResponseEntity<?> findJobs(@PathVariable() String workerId) {
        log.info("Finding jobs for userId: " + workerId);
        try {
            return new ResponseEntity<>(jobFinderService.findJobsForWorker(workerId), HttpStatus.OK);
        } catch (BadRequestException be) {
            final String errorMsg = "Invalid Request: Bad workerId: " + workerId;
            log.error(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        } catch (NoWorkerFoundException e) {
            final String errorMsg = "No workers found";
            log.error(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoJobFoundException e) {
            final String errorMsg = "No jobs found";
            log.error(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
