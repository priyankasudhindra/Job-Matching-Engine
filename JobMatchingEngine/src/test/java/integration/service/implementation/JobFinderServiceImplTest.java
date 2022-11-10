package integration.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.jobmatching.exception.BadRequestException;
import com.jobmatching.exception.NoJobFoundException;
import com.jobmatching.exception.NoWorkerFoundException;
import com.jobmatching.integration.service.implementation.JobServiceImpl;
import com.jobmatching.integration.service.implementation.WorkerServiceImpl;
import com.jobmatching.service.implementation.JobFinderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobmatching.domain.Job;
import com.jobmatching.domain.Worker;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes={JobFinderServiceImpl.class})
@TestPropertySource(locations="classpath:custom.properties")
class JobFinderServiceImplTest {

    @Autowired
    private JobFinderServiceImpl jobFinderServiceImpl;
    @MockBean
    private WorkerServiceImpl workerService;
    @MockBean
    private JobServiceImpl jobService;

    private List<Job> jobs;
    private List<Worker> workers;
    private static final String WORKER_ID = "7";

    @BeforeEach
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        jobs = mapper.readValue(ResourceUtils.getFile("src/test/resource/jobs.json"), new TypeReference<>() {
        });
        workers = mapper.readValue(ResourceUtils.getFile("src/test/resource/workers.json"), new TypeReference<>() {
        });
    }

    @Test
    void findJobs() throws NoWorkerFoundException, NoJobFoundException, BadRequestException {
        when(jobService.getJobs()).thenReturn(jobs);
        when(workerService.getWorkers()).thenReturn(workers);

        Set<Job> jobs = jobFinderServiceImpl.findJobsForWorker(WORKER_ID);
        assertEquals(2, jobs.size());
    }

}
