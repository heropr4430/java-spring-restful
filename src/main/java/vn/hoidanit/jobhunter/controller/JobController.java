package vn.hoidanit.jobhunter.controller;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;

    }

    @PostMapping("/jobs")
    @ApiMessage("create a job")
    public ResponseEntity<ResCreateJobDTO> createNewJob(@Valid @RequestBody Job job) {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.create(job));
    }

    @PutMapping("/jobs")
    @ApiMessage("update a job")
    public ResponseEntity<ResUpdateJobDTO> update(@Valid @RequestBody Job job) throws IdInvalidException {
        Optional<Job> currentjob = this.jobService.fetchJobById(job.getId());
        if (!currentjob.isPresent()) {
            throw new IdInvalidException("Job not found");
        }

        return ResponseEntity.ok().body(this.jobService.update(job, currentjob.get()));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("delete a job")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if (!currentJob.isPresent()) {
            throw new IdInvalidException("job not found");
        }
        this.jobService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/jobs")
    @ApiMessage("get job with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllJob(@Filter Specification<Job> spec,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.jobService.fetchAll(spec, pageable));
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("get job by id")
    public ResponseEntity<Job> getJobById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> job = this.jobService.fetchJobById(id);
        if (!job.isPresent()) {
            throw new IdInvalidException("job not found");
        }
        return ResponseEntity.ok().body(job.get());
    }

}
