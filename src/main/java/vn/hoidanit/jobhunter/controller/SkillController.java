package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("create a skill")
    public ResponseEntity<Skill> createNewSkill(@Valid @RequestBody Skill reqSkill) throws IdInvalidException {

        boolean skillExist = this.skillService.isSkillExist(reqSkill.getName());
        if (skillExist == true) {
            throw new IdInvalidException("skill da ton tai");
        }
        Skill skill = this.skillService.handleCreateSkill(reqSkill);
        return ResponseEntity.status(HttpStatus.CREATED).body(skill);
    }

    @PutMapping("/skills")
    @ApiMessage("update a skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill reqSkill) throws IdInvalidException {

        boolean skillExist = this.skillService.isSkillExist(reqSkill.getName());
        if (skillExist == true) {
            throw new IdInvalidException("skill da ton tai");
        }
        Skill skill = this.skillService.handleUpdateSkill(reqSkill);
        if (skill == null) {
            throw new IdInvalidException("skill co id " + reqSkill.getId() + " khong ton tai");
        }
        return ResponseEntity.ok(skill);
    }

    @GetMapping("/skills/{id}")
    @ApiMessage("fetch a skill by id")
    public ResponseEntity<Skill> getSkillById(@PathVariable("id") long id) throws IdInvalidException {

        Skill fetchSkill = this.skillService.fetchSkillById(id);
        if (fetchSkill == null) {
            throw new IdInvalidException("skill voi id: " + id + " khong ton tai");
        }

        // return ResponseEntity.ok(fetchUser);
        return ResponseEntity.status(HttpStatus.OK).body(fetchSkill);
    }

    @GetMapping("/skills")
    @ApiMessage("fetch all skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(
            @Filter Specification<Skill> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.fetchAllSkill(spec, pageable));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("delete a skill")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {

        // check id

        Skill currentSkill = this.skillService.fetchSkillById(id);
        if (currentSkill == null) {
            throw new IdInvalidException("skill voi id: " + id + " khong ton tai");
        }
        this.skillService.deleteSkill(id);
        // return ResponseEntity.ok(fetchUser);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
