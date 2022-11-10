package com.jobmatching.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.jobmatching.domain.Job;
import com.jobmatching.exception.BadRequestException;
import com.jobmatching.exception.NoJobFoundException;
import com.jobmatching.exception.NoWorkerFoundException;
import com.jobmatching.integration.service.implementation.JobServiceImpl;
import com.jobmatching.util.DistanceCalculator;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jobmatching.domain.Worker;
import com.jobmatching.integration.service.implementation.WorkerServiceImpl;
import com.jobmatching.service.JobFinderService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JobFinderServiceImpl implements JobFinderService {

    private final WorkerServiceImpl workerService;
    private final JobServiceImpl jobService;
    @Value(value = "${maxJobSearchResults}")
    private Long maxJobSearchResults;

    public JobFinderServiceImpl(WorkerServiceImpl workerService, JobServiceImpl jobService) {
        super();
        this.workerService = workerService;
        this.jobService = jobService;
    }

    @Override
    public Set<Job> findJobsForWorker(String workerId)
            throws BadRequestException, NoWorkerFoundException, NoJobFoundException {

        if (workerId == null || "".equals(workerId) || !StringUtils.isNumeric(workerId))
            throw new BadRequestException();

        Set<Job> workerMatchedJobs;

        List<Worker> workers = workerService.getWorkers();

        if (workers == null || workers.isEmpty()) {
            log.error("No workers found");
            throw new NoWorkerFoundException();
        } else {
            Optional<Worker> workerOpt = workers.stream().filter(w -> w.getUserId().equals(workerId)).findAny();
            Worker worker;
            if (workerOpt.isPresent())
                worker = workerOpt.get();
            else {
                log.error("Given Worker {} not found in the system", workerId);
                throw new NoWorkerFoundException();
            }
            List<Job> jobs = jobService.getJobs();
            if (jobs == null || jobs.isEmpty()) {
                log.error("No jobs found");
                throw new NoJobFoundException();
            } else {
                workerMatchedJobs = findPotentialJobs(jobs, worker);
            }
        }
        return workerMatchedJobs;
    }

    private Set<Job> findPotentialJobs(List<Job> jobs, Worker worker) {

        log.info("Total Jobs: " + jobs.size());
        Set<Job> potentialJobs;

        // Check if the driver license requirement is fulfilled
        potentialJobs = jobs.stream().filter(job -> job.isDriverLicenseRequired() == worker.getHasDriversLicense())
                .collect(Collectors.toSet());

        // Check if the worker possesses the required skills
        potentialJobs = potentialJobs.stream().filter(job -> worker.getSkills().contains(job.getJobTitle()))
                .collect(Collectors.toSet());

        // Check for at least one certification
        potentialJobs = potentialJobs.stream().filter(job -> doesWorkerHasAtLeastOneCertificate(job, worker))
                .collect(Collectors.toSet());

        // Compute distance for the matched jobs
        potentialJobs = potentialJobs.stream()
                .filter(job -> DistanceCalculator.calculateDistance(worker.getJobSearchAddress().getLatitude(),
                        job.getLocation().getLatitude(), worker.getJobSearchAddress().getLongitude(),
                        job.getLocation().getLongitude(),
                        worker.getJobSearchAddress().getUnit()) <= worker.getJobSearchAddress().getMaxJobDistance())
                .collect(Collectors.toSet());

        // Find a maximum of 3 jobs
        potentialJobs = potentialJobs.stream().limit(maxJobSearchResults).collect(Collectors.toSet());
        log.info("Number of matched jobs: " + potentialJobs.size());

        return potentialJobs;
    }

    private boolean doesWorkerHasAtLeastOneCertificate(Job job, Worker worker) {
        Set<String> matchingCertificates = job.getRequiredCertificates().stream().distinct()
                .filter(worker.getCertificates()::contains).collect(Collectors.toSet());
        return !matchingCertificates.isEmpty();
    }
}
