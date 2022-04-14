package com.ddraig.jwtMysql.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddraig.jwtMysql.entity.Role;
import com.ddraig.jwtMysql.entity.RoleName;
import com.ddraig.jwtMysql.entity.User;
import com.ddraig.jwtMysql.repository.RoleRepository;

@Service
public class RoleService {
	@Autowired
    RoleRepository roleRepository;
	
	public Optional<Role> findByName(RoleName roleName) {
		return roleRepository.findByName(roleName);
	}
}
