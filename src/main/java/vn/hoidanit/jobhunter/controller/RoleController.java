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
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("create a role")
    public ResponseEntity<Role> Create(@Valid @RequestBody Role role) throws IdInvalidException {

        if (this.roleService.ExistsByName(role.getName())) {
            throw new IdInvalidException("role voi name " + role.getName() + " da ton tai");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(role));
    }

    @PutMapping("/roles")
    @ApiMessage("update a role")
    public ResponseEntity<Role> update(@Valid @RequestBody Role role) throws IdInvalidException {
        Role currentRole = this.roleService.fetchRoleById(role.getId());
        if (currentRole == null) {
            throw new IdInvalidException("role not found");
        }

        // if (this.roleService.ExistsByName(role.getName())) {
        // throw new IdInvalidException("role voi name " + role.getName() + " da ton
        // tai");
        // }

        return ResponseEntity.ok().body(this.roleService.update(role, currentRole));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("delete a role")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id)
            throws IdInvalidException {

        if (this.roleService.fetchRoleById(id) == null) {
            throw new IdInvalidException("role voi id: " + id + " khong ton tai");
        }
        this.roleService.delete(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/roles")
    @ApiMessage("fetch all role")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(
            @Filter Specification<Role> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.getRoles(spec, pageable));
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("get a role")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") long id)
            throws IdInvalidException {
        Role role = this.roleService.fetchRoleById(id);
        if (role == null) {
            throw new IdInvalidException("role voi id: " + id + " khong ton tai");
        }

        return ResponseEntity.ok(role);
    }

}
