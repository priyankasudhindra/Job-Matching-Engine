package com.jobmatching.service;

import java.util.Set;

import com.jobmatching.domain.Job;
import com.jobmatching.exception.BadRequestException;
import com.jobmatching.exception.NoJobFoundException;
import com.jobmatching.exception.NoWorkerFoundException;

public interface JobFinderService {
    public Set<Job> findJobsForWorker(String workerId) throws BadRequestException, NoWorkerFoundException, NoJobFoundException;

}
