package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permisstionRepository;

    public RoleService(RoleRepository roleRepository,
            PermissionRepository permisstionRepository) {
        this.roleRepository = roleRepository;
        this.permisstionRepository = permisstionRepository;
    }

    public boolean ExistsByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role create(Role r) {
        if (r.getPermissions() != null) {
            List<Long> reqPers = r.getPermissions().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Permission> dbPers = this.permisstionRepository.findByIdIn(reqPers);
            r.setPermissions(dbPers);
        }

        return this.roleRepository.save(r);
    }

    public Role fetchRoleById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        return null;
    }

    public Role update(Role r, Role roleInDB) {
        // check skills
        if (r.getPermissions() != null) {
            List<Long> reqPers = r.getPermissions().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Permission> dbPers = this.permisstionRepository.findByIdIn(reqPers);
            roleInDB.setPermissions(dbPers);
        }

        // update correct info
        roleInDB.setName(r.getName());
        roleInDB.setDescription(r.getDescription());
        roleInDB.setActive(r.isActive());

        return this.roleRepository.save(roleInDB);

    }

    public void delete(long id) {
        this.roleRepository.deleteById(id);
    }

    public ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> pageRole = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageRole.getTotalPages());
        mt.setTotal(pageRole.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pageRole.getContent());
        return rs;
    }

}
