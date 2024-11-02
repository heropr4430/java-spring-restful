package vn.hoidanit.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    public User findByEmail(String email);

    public boolean existsByEmail(String email);

    public User findByRefreshTokenAndEmail(String token, String email);

    public List<User> findByCompany(Company company);

}
