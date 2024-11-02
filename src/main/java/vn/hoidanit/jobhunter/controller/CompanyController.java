package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("create company success")
    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company reqCompany) {
        Company company = this.companyService.handleCreateCompany(reqCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @GetMapping("/companies")
    @ApiMessage("Fetch companies success")
    public ResponseEntity<ResultPaginationDTO> getCompany(
            @Filter Specification<Company> spec,
            Pageable pageable) {

        ResultPaginationDTO companies = this.companyService.handleGetCompany(spec, pageable);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("Fetch company by id success")
    public ResponseEntity<Company> getCompanyById(
            @PathVariable("id") long id) throws IdInvalidException {
        Optional<Company> company = this.companyService.findById(id);
        if (!company.isPresent()) {
            throw new IdInvalidException("job not found");
        }
        return ResponseEntity.ok(company.get());
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company reqCompany) {
        Company company = this.companyService.handleUpdateCompany(reqCompany);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }

}
