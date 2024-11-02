package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

import java.util.Optional;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permisstionRepository;

    public PermissionService(PermissionRepository permisstionRepository) {
        this.permisstionRepository = permisstionRepository;
    }

    public boolean isPermissionExist(Permission permission) {
        return this.permisstionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(),
                permission.getMethod(), permission.getModule());
    }

    public Permission fetchPermissionById(long id) {
        Optional<Permission> permissionOptional = this.permisstionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            return permissionOptional.get();
        }
        return null;
    }

    public Permission create(Permission permission) {
        return this.permisstionRepository.save(permission);
    }

    public Permission update(Permission per) {
        Permission currentPer = this.fetchPermissionById(per.getId());
        if (currentPer != null) {
            currentPer.setName(per.getName());
            currentPer.setApiPath(per.getApiPath());
            currentPer.setMethod(per.getMethod());
            currentPer.setModule(per.getModule());
            return this.permisstionRepository.save(currentPer);
        }
        return null;
    }

    public ResultPaginationDTO fetchAllPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pagePermission = this.permisstionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pagePermission.getTotalPages());
        mt.setTotal(pagePermission.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pagePermission.getContent());
        return rs;
    }

    public void delete(long id) {
        // delete permission role
        Optional<Permission> permissionOptional = this.permisstionRepository.findById(id);
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        // delete permission
        this.permisstionRepository.delete(currentPermission);

    }

    public boolean isSameName(Permission reqPer) {
        Permission permissionDB = this.fetchPermissionById(reqPer.getId());
        if (permissionDB != null) {
            if (permissionDB.getName().equals(reqPer.getName())) {
                return true;
            }
        }
        return false;
    }

}
