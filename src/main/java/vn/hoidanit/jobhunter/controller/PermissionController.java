package vn.hoidanit.jobhunter.controller;

import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("create a permission")
    public ResponseEntity<Permission> create(@Valid @RequestBody Permission reqPer) throws IdInvalidException {

        boolean skillExist = this.permissionService.isPermissionExist(reqPer);
        if (skillExist == true) {
            throw new IdInvalidException("permission da ton tai");
        }
        Permission permission = this.permissionService.create(reqPer);
        return ResponseEntity.status(HttpStatus.CREATED).body(permission);
    }

    @PutMapping("/permissions")
    @ApiMessage("update a permission")
    public ResponseEntity<Permission> update(@Valid @RequestBody Permission reqPer) throws IdInvalidException {

        Permission currentPer = this.permissionService.fetchPermissionById(reqPer.getId());
        if (currentPer == null) {
            throw new IdInvalidException("Permission voi id: " + reqPer.getId() + " khong ton tai");
        }

        boolean skillExist = this.permissionService.isPermissionExist(reqPer);
        if (skillExist == true) {
            if (this.permissionService.isSameName(reqPer)) {
                throw new IdInvalidException("permission da ton tai");
            }
        }
        Permission permission = this.permissionService.update(reqPer);
        if (permission == null) {
            throw new IdInvalidException("Permission co id " + reqPer.getId() + " khong ton tai");
        }
        return ResponseEntity.ok().body(permission);
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("delete a permission")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {

        // check id

        Permission currentPer = this.permissionService.fetchPermissionById(id);
        if (currentPer == null) {
            throw new IdInvalidException("Permission voi id: " + id + " khong ton tai");
        }
        this.permissionService.delete(id);
        // return ResponseEntity.ok(fetchUser);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/permissions")
    @ApiMessage("fetch all permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPermissions(
            @Filter Specification<Permission> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.fetchAllPermissions(spec, pageable));
    }
}
