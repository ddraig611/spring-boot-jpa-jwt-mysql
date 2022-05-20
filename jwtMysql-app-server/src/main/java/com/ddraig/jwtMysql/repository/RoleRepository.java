package com.ddraig.jwtMysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddraig.jwtMysql.entity.Role;
import com.ddraig.jwtMysql.entity.RoleName;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
    
    Optional<Role> findByName(String roleName);
}
