package com.rakib.service;

import com.rakib.domain.Role;

import java.util.List;

public interface RoleService {
	Role saveRole(Role role) throws Exception;

	List<Role> getRole();
}
